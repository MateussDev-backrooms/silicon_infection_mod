package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModNetworking;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalAttackGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalFleeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Gene;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IHomunculus;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GlobalWarming.GlobalWarmingData;
import com.mateussdev.chemosyntehsis.Util.Packets.MutationSyncPacket;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;

import java.util.*;

public class BaseTethered extends BaseSiliconite implements IGenomeModifiable {
    protected BaseTethered(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    // ===== CONSTANTS ===== //
    protected static final int IDLE_GLOBAL_WARMING_RATE = 64;

    //Genome system
    public Gene currentGene = null;
    private Gene _oldGene = null;
    protected int metamorphosisTime = 40;


    // ==== Animations ===== //
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            //death anim
            if (this.isDeadOrDying()) {
                return event.setAndContinue(RawAnimation.begin().then("death", Animation.LoopType.HOLD_ON_LAST_FRAME));
            }

            return event.setAndContinue(
                    event.isMoving() ? RawAnimation.begin().thenLoop("walk") :
                            RawAnimation.begin().thenLoop("idle"));
        }));

        //Bone wobbling
        controllers.add(new AnimationController<>(this, "reaction_controller", 1, event -> PlayState.CONTINUE));
    }

    // ===== AI stuffs ===== //


    @Override
    protected void registerGoals() {
        registerDefaultGoals();
    }

    public void registerDefaultGoals() {
        this.goalSelector.removeAllGoals(g -> true);
        this.targetSelector.removeAllGoals(g -> true);

        //Default siliconite AI
        if (!isBrave()) {
            this.goalSelector.addGoal(1, new ConditionalAttackGoal(this, 1.0f, true, this::shouldAttackTarget));
            this.goalSelector.addGoal(0, new ConditionalFleeGoal(this, LivingEntity.class, 16.0f, 1.2d, 1.3d, this::shouldFlee));
        } else {
            this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0f, true));
        }

        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));


        if (shouldAlertOthersOnHurt()) {
            this.targetSelector.addGoal(1, (new HurtByNonSiliconiteGoal(this, new Class[0])).setAlertOthers(new Class[]{BaseSiliconite.class}));
        } else {
            this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));
        }

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ===== Customization ===== //

    protected boolean explodeOnDeath() { return true; }

    @Override
    protected boolean destructiveTether() { return false; }

    @Override
    protected float getTetherChance() { return 0.6f; }

    @Override
    public GeoBone[] getBulbsArray(GeoModel<?> model) { return new GeoBone[0]; }

    @Override
    protected boolean isBrave() { return true; }

    @Override
    protected int evolvesAtMetabolism() { return 300; }

    // ===== Default functionality ===== //
    private int tick = 0;

    @Override
    public void tick() {
        tick++;
        if (tick % IDLE_GLOBAL_WARMING_RATE == 0) {
            if (this.level() instanceof ServerLevel slvl) {
                GlobalWarmingData data = GlobalWarmingData.get(slvl);
                data.addPoints(0.01f);
            }
        }

        //Only update the bones on the client
        if (this.level().isClientSide) {
            for (String boneName : wobblyBones()) {
                Vector3f currBone = boneWobble.get(boneName);
                if (currBone != null) {
                    currBone.lerp(new Vector3f(0), 0.2f);
                }
            }
        }

        //Run the onTick for each mutation in the genome
        if (currentGene != null) {
            for (Mutation mutation : currentGene.mutations) {
                mutation.onTick(this);
            }
        }

        super.tick();
    }

    // ===== Death ===== //

    protected int deathTime = 0;
    @Override
    public void tickDeath() {
        if (explodeOnDeath()) {
            ++this.deathTime;
            this.hurtMarked = true;

            if (this.level() instanceof ServerLevel slvl) {
                if (this.deathTime == 40) {
                    this.splitIntoChunks(5);
                    this.releaseGasIntoAtmosphere(slvl, 1f);
                    this.level().broadcastEntityEvent(this, (byte) 60);
                    this.remove(RemovalReason.KILLED);
                }
            }

        } else {
            super.tickDeath();
        }

        //Run the onTickDeath for each mutation in the genome
        if (currentGene != null) {
            for (Mutation mutation : currentGene.mutations) {
                mutation.onTickDeath(this);
            }
        }
    }

    public void turnIntoBiomush() {
        if (this.level() instanceof ServerLevel slvl) {
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());
            slvl.setBlock(blockPosition(), ModBlocks.BIOMUSH.get().defaultBlockState(), 2);
        }
    }

    public void splitIntoChunks(int count) {
        if (this.level() instanceof ServerLevel slvl) {
            slvl.playSound(null, blockPosition(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 1f, 1f);

            //Particles
            SiliconiteParticles.spawnBloodBurst(slvl, blockPosition());

            //Spawn chunks
            for (int i = 0; i < count; i++) {
                if (slvl.random.nextFloat() < 0.33f) {
                    ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                    chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble() * 2f - 1f) * 0.1f, (slvl.random.nextDouble()) * 0.8f, (slvl.random.nextDouble() * 2f - 1f) * 0.1f));
                    slvl.addFreshEntity(chunkOfFlesh);
                } else {
                    GibFlesh gib = ModEntities.GIB_FLESH.get().create(slvl);
                    gib.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    gib.addDeltaMovement(new Vec3((slvl.random.nextDouble() * 2f - 1f) * 0.4f, (slvl.random.nextDouble()) * 0.5f, (slvl.random.nextDouble() * 2f - 1f) * 0.4f));
                    slvl.addFreshEntity(gib);
                }
            }

        }
    }


    //===== Bone wobble on hurt =====//
    public static final Map<String, Vector3f> boneWobble = new HashMap<>();

    //Use function so it can be overridden
    public static List<String> wobblyBones() {
        return List.of("body", "head");
    }

    //Move the bones on the client. Do not do this on the server duh
    public void triggerHitReaction(DamageSource source, float amount) {
        if (this.level().isClientSide && source.getEntity() != null) {
            float impactStrength = 90f * amount;
            float x = (random.nextFloat() - 0.5f) * impactStrength;
            float y = (random.nextFloat() - 0.5f) * impactStrength;
            float z = (random.nextFloat() - 0.5f) * impactStrength;
            Vector3f wobbleDir = new Vector3f(x, y, z);
            for (String boneName : wobblyBones()) {
                boneWobble.put(boneName, wobbleDir.mul(1 + random.nextFloat() * 0.33f));
            }
            setDeltaMovement(getDeltaMovement().add(wobbleDir.x, wobbleDir.y, wobbleDir.z));
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (isDeadOrDying()) return false;

        //Calculate the actual damage received
        float cumulativeDamageMultiplier = 1.0f;
        if (currentGene != null) {
            for (Mutation mutation : currentGene.mutations) {
                cumulativeDamageMultiplier *= mutation.onHurt(this, pSource, pAmount);
            }
        }
        boolean hurtResult = cumulativeDamageMultiplier > 0 && super.hurt(pSource, pAmount * cumulativeDamageMultiplier);

        if (cumulativeDamageMultiplier > 0) {
            triggerHitReaction(pSource, pAmount);
            if (hurtResult) {
                if (this.level() instanceof ServerLevel slvl) {
                    slvl.playSound(null, blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.HOSTILE, 1f, 0.8f);
                    SiliconiteParticles.spawnBloodHit(slvl, position());
                }
            }
        }

        //Penalty for getting damaged
        reportFitness(-pAmount * cumulativeDamageMultiplier / 2);

        return hurtResult;
    }


    // ===== Gene system interaction ===== //

    private static final EntityDataAccessor<CompoundTag> MUTATION_TYPES =
            SynchedEntityData.defineId(BaseTethered.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Optional<UUID>> GENE_ORIGIN =
            SynchedEntityData.defineId(BaseTethered.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    public boolean applyGene(Gene gene) {
        if (this.level() instanceof ServerLevel slvl) {
            slvl.playSound(null, blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.HOSTILE);

            if (currentGene != null) {

                //Run onRemove to reset any other values
                for (Mutation mutation : currentGene.mutations) {
                    if (mutation.canMutateMob(this)) {
                        mutation.onRemove(this);
                    }
                }

                //Reset AI back to default
                registerDefaultGoals();
            }

            this.currentGene = gene;


            //Change attributes
            gene.attributeMutation.applyAttributesSequentially(this);

            //Sync health to max health
            this.setHealth(this.getMaxHealth());

            //Then change the AI by running onInit()
            List<ResourceLocation> mutationTypeIds = new ArrayList<>();
            for (Mutation mutation : gene.mutations) {
                if (mutation.canMutateMob(this)) {
                    mutation.onInit(this);
                    if (mutation.hasRenderLayer()) {
                        mutationTypeIds.add(mutation.getTypeId());
                    }
                }
            }
            this.updateMutationTypes(mutationTypeIds);

            var server = this.getServer();
            if (server != null) {
                server.execute(() -> {
                    for (Mutation mutation : gene.mutations) {
                        if (!mutation.canMutateMob(this)) continue;

                        if (mutation.hasRenderLayer()) {
                            ModNetworking.CHANNEL.send(
                                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
                                    new MutationSyncPacket(this.getId(), mutation.getTypeId())
                            );
                        }
                    }
                });
            }
        }

        return true;
    }

    @Override
    public Gene getGene() {
        return currentGene;
    }

    @Override
    public void setGeneOrigin(UUID homunculusId) {
        if (homunculusId != null) {
            this.entityData.set(GENE_ORIGIN, Optional.of(homunculusId));
        }
    }

    @Override
    public UUID getGeneOrigin() {
        return this.entityData.get(GENE_ORIGIN).orElse(null);
    }

    @Override
    public void reportFitness(float points) {
        if (!(level() instanceof ServerLevel slvl)) return;

        UUID originId = getGeneOrigin();
        if (originId == null) return;

        Entity homunculus = slvl.getEntity(originId);
        if (homunculus instanceof IHomunculus he) {
            he.getHomunculusBrain().addFitnessToGene(this.currentGene.id, points);
        }
    }


    public void updateMutationTypes(List<ResourceLocation> types) {
        CompoundTag tag = new CompoundTag();
        //Store count and each ID as string
        tag.putInt("count", types.size());
        for (int i = 0; i < types.size(); i++) {
            tag.putString("type_" + i, types.get(i).toString());
        }
        this.entityData.set(MUTATION_TYPES, tag);
    }

    @Override
    public boolean hasMutationType(ResourceLocation typeId) {
        CompoundTag tag = this.entityData.get(MUTATION_TYPES);
        int count = tag.getInt("count");
        for (int i = 0; i < count; i++) {
            String stored = tag.getString("type_" + i);
            if (stored.equals(typeId.toString())) {
                return true;
            }
        }
        return false;
    }

    // ===== Saving loading ===== //
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MUTATION_TYPES, new CompoundTag());
        this.entityData.define(GENE_ORIGIN, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (currentGene != null) {
            tag.put("genome", currentGene.serialize());
            tag.putUUID("origin_homunculus", this.entityData.get(GENE_ORIGIN).get());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        //Load
        if (tag.contains("genome")) {
            currentGene = Gene.deserialize(tag.getCompound("genome"));
            this.entityData.set(GENE_ORIGIN, Optional.of(tag.getUUID("origin_homunculus")));
            if (currentGene != null) {
                List<ResourceLocation> types = new ArrayList<>();

                //Reset AI
                registerDefaultGoals();
                for (Mutation mutation : currentGene.mutations) {
                    mutation.onInit(this);
                    if (mutation.hasRenderLayer()) {
                        types.add(mutation.getTypeId());
                    }
                }

                updateMutationTypes(types);

                if (this.level() instanceof ServerLevel slvl) {
                    slvl.getServer().execute(() -> {
                        for (Mutation mutation : currentGene.mutations) {
                            if (mutation.hasRenderLayer()) {
                                ModNetworking.CHANNEL.send(
                                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
                                        new MutationSyncPacket(this.getId(), mutation.getTypeId())
                                );
                            }
                        }
                    });
                }
            }
        }
    }

    //Adding fitness

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        //Give fitness point to the amount of damage the mob dealt to another mob
        reportFitness((float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
        return super.doHurtTarget(pEntity);
    }

    @Override
    public void awardKillScore(Entity pKilled, int pScoreValue, DamageSource pSource) {
        if (pKilled instanceof LivingEntity le) {
            //Add tons of fitness points on kill
            reportFitness(le.getMaxHealth() * 2);
        }
        super.awardKillScore(pKilled, pScoreValue, pSource);
    }
}
