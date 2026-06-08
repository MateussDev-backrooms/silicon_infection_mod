package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModNetworking;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Gene;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSyncPacket;
import com.mateussdev.chemosyntehsis.Systems.GlobalWarming.GlobalWarmingData;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseTethered extends BaseSiliconite implements IGenomeModifiable {
    protected BaseTethered(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected int globalWarmingRate = 64;
    protected int chunk_count = 5;

    //Genome system
    public Gene currentGene = null;
    private Gene _oldGene = null;
    protected int metamorphosisTime = 40;

    public int fitnessPoints = 0;

    protected boolean explodeOnDeath() {
        return true;
    }

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
        controllers.add(new AnimationController<>(this, "reaction_controller", 1, event -> {

            return PlayState.CONTINUE;
        }));
    }



    @Override
    protected boolean destructiveTether() {
        return false;
    }

    @Override
    protected float getTetherChance() {
        return 0.6f;
    }

    @Override
    protected boolean isBrave() {
        return true;
    }

    private int t = 0;

    @Override
    public void tick() {
        t++;
        if (t % globalWarmingRate == 0) {
            if (level() instanceof ServerLevel slvl) {
                GlobalWarmingData data = GlobalWarmingData.get(slvl);
                data.addPoints(0.01f);
            }
        }


        for(String boneName : wobblyBones()) {
            Vector3f currBone = boneWobble.get(boneName);
            if(currBone != null) {
                currBone.lerp(new Vector3f(0), 0.2f);
            }
        }

        //Run the onTick for each mutation in the genome
        if(currentGene != null) {
            for(Mutation mutation : currentGene.mutations) {
                mutation.onTick(this);
            }
        }

        super.tick();
    }

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
    }

    public void turnIntoBiomush() {
        if (level() instanceof ServerLevel slvl) {
            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

            slvl.setBlock(blockPosition(), ModBlocks.BIOMUSH.get().defaultBlockState(), 2);
        }
    }

    public void splitIntoChunks(int count) {
        if (level() instanceof ServerLevel slvl) {
            slvl.playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

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

    @Override
    protected int evolvesAtMetabolism() {
        return 100;
    }

    public final Map<String, Vector3f> boneWobble = new HashMap<>();

    //Use function so it can be overrided
    public final List<String> wobblyBones() {
        return List.of("body", "head");
    }

    public String wobbleBoneName = "";

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(isDeadOrDying()) return false;

        boolean hurtResult = super.hurt(pSource, pAmount);

            triggerHitReaction(pSource, pAmount);
        if(hurtResult) {
//            wobbleBoneName = wobblyBones().get(random.nextInt(wobblyBones().size()));

            if (level() instanceof ServerLevel slvl) {
                slvl.playSound(null, blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.HOSTILE, 1f, 0.8f);
                StaticSiliconiteMethods.spawnBloodHit(slvl, position());
            }
        }

        return hurtResult;
    }

    public void triggerHitReaction(DamageSource source, float amount) {
        if (level().isClientSide && source.getEntity() != null) {
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

    // ===== Gene system interaction ===== //

    @Override
    public boolean applyGene(Gene gene) {
        this.currentGene = gene;
        if(this.level() instanceof ServerLevel slvl) {
            slvl.playSound(null, blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.HOSTILE);

            //Apply RenderLayers from all Gene mutations

            var server = this.getServer();
            if (server != null) {
                server.execute(() -> {
//                    StaticSiliconiteMethods.debugLog("Executing send block for gene mutations: {}"+ gene.mutations.size());
                    for (Mutation mutation : gene.mutations) {
//                        StaticSiliconiteMethods.debugLog("Checking mutation: {}"+ mutation.getClass().getSimpleName());
                        GeoRenderLayer layer = mutation.getMutationRenderLayer(null);
                        mutation.onAiRegisterGoals(this);
//                        StaticSiliconiteMethods.debugLog("Layer returned: {}"+ layer);
                        if (layer != null) {
//                            StaticSiliconiteMethods.debugLog("Sending packet for {}"+ mutation.getClass().getSimpleName());
                            ModNetworking.CHANNEL.send(
                                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this),
                                    new MutationSyncPacket(this.getId(), mutation.getClass().getSimpleName())
                            );
                        }
                    }
                });
            }
        }


        //Success
        return true;
    }

    @Override
    public Gene getGene() {
        return currentGene;
    }

    @Override
    public void clearAllGenes() {

    }

    @Override
    public void addFitnessPoints(int deltaPoints) {

    }

    @Override
    public void removeFitnessPoints(int deltaPoints) {

    }
}
