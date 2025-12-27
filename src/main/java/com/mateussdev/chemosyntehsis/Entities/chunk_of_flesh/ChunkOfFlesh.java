package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.SeekAndEatBiomushGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.BusBuilder;

import java.sql.Array;
import java.util.List;
import java.util.Set;

import static com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods.spawnBloodBurst;

public class ChunkOfFlesh extends BaseSiliconite {
    public ChunkOfFlesh(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public int evolution_t = 0;
    public boolean mustEvolve = false;
    public boolean doBurrowAnim = false;

    public static final EntityDataAccessor<Integer> IS_BURROWING = SynchedEntityData.defineId(ChunkOfFlesh.class, EntityDataSerializers.INT);

    private static final int MERGE_COUNT = 5;
    private static final double MERGE_RADIUS = 3.5D;

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 2D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 3D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            if (entityData.get(IS_BURROWING) == 1) {
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("burrow"));
            }

            return event.setAndContinue(
                    // If moving, play the walking animation


                    event.isMoving() ? RawAnimation.begin().thenLoop("walk"):
                            // If not moving, play the idle animation
                            RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SeekAndEatBiomushGoal(this, ModBlocks.BIOMUSH.get()));
    }

    @Override
    protected float getTetherChance() {
        return 0.0f;
    }

    @Override
    protected float getBulbBreakoffChance() {
        return 0.0f;
    }

    public void consumeBiomush() {

        evolveIntoHybrid();
        this.discard();
    }

    private boolean _brw = true;
    @Override
    public void tick() {
        super.tick();
        if (entityData.get(IS_BURROWING) == 1 && _brw) {
            level().playSound(this, this.blockPosition(), SoundEvents.WARDEN_DIG, SoundSource.HOSTILE, 1f, 1f);
            _brw = false;
        }

        if (mustEvolve && evolution_t++ > 20) {
            mergeIntoCluster();
        }

        if(tickCount % 20 == 0) {
            if (!level().isClientSide && !this.mustEvolve) {
                List<ChunkOfFlesh> nearby = level().getEntitiesOfClass(
                        ChunkOfFlesh.class,
                        this.getBoundingBox().inflate(MERGE_RADIUS),
                        c -> c != this && !c.mustEvolve
                );

                if (nearby.size() + 1 >= MERGE_COUNT) {
                    initiateMerge(nearby);
                }
            }
        }
    }

    private void initiateMerge(List<ChunkOfFlesh> others) {
        this.mustEvolve = true;

        for (ChunkOfFlesh c : others) {
            c.mustEvolve = true;
        }

        if (level() instanceof ServerLevel slvl) {
            Vec3 center = this.position();

            // Suck the others inward (visual feedback)
            for (ChunkOfFlesh c : others) {
                Vec3 dir = center.subtract(c.position()).normalize();
                c.setDeltaMovement(dir.scale(0.25));
                c.setBurrowAnimation(true);
            }

            spawnBloodBurst(slvl, blockPosition());

            // Delay actual merge slightly for drama
            slvl.scheduleTick(this.blockPosition(), Blocks.AIR, 20);
        }
    }

    private void mergeIntoCluster() {
        if (!(level() instanceof ServerLevel slvl)) return;

        List<ChunkOfFlesh> all = slvl.getEntitiesOfClass(
                ChunkOfFlesh.class,
                this.getBoundingBox().inflate(MERGE_RADIUS),
                c -> c.mustEvolve
        );

        // Only ONE chunk does the spawn
        if (all.stream().anyMatch(c -> c.getId() < this.getId())) return;

        // Effects
        spawnBloodBurst(slvl, this.blockPosition());
        slvl.playSound(null, blockPosition(), SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE, 1f, 3f);

        // Spawn Cluster
        ClusterOfFlesh cluster = ModEntities.CLUSTER_OF_FLESH.get().create(slvl);
        cluster.moveTo(this.getX(), this.getY(), this.getZ());
        slvl.addFreshEntity(cluster);

        // Consume all chunks
        for (ChunkOfFlesh c : all) {
            c.discard();
        }
    }



    @SafeVarargs
    public static EntityType<? extends BaseHybrid>[] createHybridPool(EntityType<? extends BaseHybrid>... types) {
        return types;
    }


    private static final EntityType<? extends BaseHybrid>[] HYBRID_EVOLUTION_RESULTS = createHybridPool(
            ModEntities.THROMBOCYTE.get(),
            ModEntities.ERYTHROCYTE.get(),
            ModEntities.ASTROCYTE.get()
    );
    public void evolveIntoHybrid() {
        if(this.level() instanceof ServerLevel slvl) {
            spawnBloodBurst(slvl, this.blockPosition());
            slvl.playSound(null, this.blockPosition(), SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE);

            BaseHybrid result = HYBRID_EVOLUTION_RESULTS[slvl.random.nextInt(HYBRID_EVOLUTION_RESULTS.length)].create(slvl);
            if(result == null) return;

            result.moveTo(this.getX(), this.getY(), this.getZ());
            slvl.addFreshEntity(result);
        }
    }

    public void setBurrowAnimation(boolean bool) {
        doBurrowAnim = bool;
        entityData.set(IS_BURROWING, bool ? 1:0);
        if(!bool) _brw = true;
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        if(entityData.get(IS_BURROWING) == 1) {
            super.push(0d, 0d, 0d);
        } else {
            super.push(pX, pY, pZ);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("is_burrowing", entityData.get(IS_BURROWING));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(IS_BURROWING, 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_BURROWING, 0);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        entityData.set(IS_BURROWING, 0);
        return super.hurt(pSource, pAmount);
    }
}
