package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;

import java.util.List;

public class ChunkOfFlesh extends BaseSiliconite {
    public ChunkOfFlesh(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.tetherChance = 0f;
        this.bulbBreakoffChance = 0f;
    }

    protected int evolution_t = 0;
    private int clusterCooldown = 200;
    public boolean mustEvolve = false;
    public boolean doBurrowAnim = false;

    public static final EntityDataAccessor<Boolean> IS_BURROWING = SynchedEntityData.defineId(ChunkOfFlesh.class, EntityDataSerializers.BOOLEAN);

    private static final int MERGE_COUNT = 5;
    private static final double MERGE_RADIUS = 3.5D;

    private static final List<EntityType<? extends BaseHybrid>> HYBRID_EVOLUTION_RESULTS = List.of(
            ModEntities.THROMBOCYTE.get(),
            ModEntities.ERYTHROCYTE.get(),
            ModEntities.ASTROCYTE.get()
    );

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
            if (entityData.get(IS_BURROWING)) {
                return event.setAndContinue(RawAnimation.begin().thenPlayAndHold("burrow"));
            }

            return event.setAndContinue(
                    event.isMoving() ? RawAnimation.begin().thenLoop("walk") :
                            RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    protected void registerGoals() {
        //Default settings override when new behavior is required

        // - GOALS
        this.goalSelector.addGoal(0, new SeekAndEatBiomushGoal(this, ModBlocks.BIOMUSH.get()));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Monster>(this, Monster.class, 16.0f, 1.2d, 1.3d, this::shouldFlee));

        //Avoid water (No float task cuz they are immune to water damage)
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        //Seek out
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
        this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public GeoBone[] getBulbsArray(GeoModel<?> model) {
        return new GeoBone[0];
    }

    public void consumeBiomush() {
        evolveIntoHybrid();
        this.discard();
    }

    private boolean _brw = true;

    @Override
    public void tick() {
        super.tick();
        if (entityData.get(IS_BURROWING) && _brw) {
            this.level().playSound(this, this.blockPosition(), SoundEvents.WARDEN_DIG, SoundSource.HOSTILE, 1f, 1f);
            _brw = false;
        }

        if (mustEvolve && evolution_t++ > 20) {
            mergeIntoCluster();
        }

        if (tickCount % 60 == 0) {
            if (this.level() instanceof ServerLevel slvl && !this.mustEvolve && --clusterCooldown <= 0 && !entityData.get((IS_BURROWING))) {
                List<ChunkOfFlesh> nearby = this.level().getEntitiesOfClass(
                        ChunkOfFlesh.class,
                        this.getBoundingBox().inflate(MERGE_RADIUS),
                        c -> c != this && !c.mustEvolve && !c.entityData.get(IS_BURROWING)
                );

                if (nearby.size() + 1 >= MERGE_COUNT) {
                    initiateMerge(nearby);
                }
            }
        }
    }

    private void initiateMerge(List<ChunkOfFlesh> others) {
        if (!(this.level() instanceof ServerLevel slvl)) return;

        this.mustEvolve = true;
        for (ChunkOfFlesh c : others) { c.mustEvolve = true; }

        Vec3 center = this.position();

        for (ChunkOfFlesh c : others) {
            Vec3 dir = center.subtract(c.position());
            c.setDeltaMovement(dir);
            c.setBurrowAnimation(true);
        }

        SiliconiteParticles.spawnBloodBurst(slvl, blockPosition());

        slvl.scheduleTick(this.blockPosition(), Blocks.AIR, 20);
    }

    private void mergeIntoCluster() {
        if (!(level() instanceof ServerLevel slvl)) return;

        List<ChunkOfFlesh> all = slvl.getEntitiesOfClass(
                ChunkOfFlesh.class,
                this.getBoundingBox().inflate(MERGE_RADIUS),
                c -> c.mustEvolve
        );

        if (all.stream().anyMatch(c -> c.getId() < this.getId())) return;

        // Effects
        SiliconiteParticles.spawnBloodBurst(slvl, this.blockPosition());
        slvl.playSound(null, blockPosition(), SoundEvents.MUD_FALL, SoundSource.HOSTILE, 1f, 3f);

        // Spawn Cluster
        ClusterOfFlesh cluster = ModEntities.CLUSTER_OF_FLESH.get().create(slvl);
        cluster.moveTo(this.getX(), this.getY(), this.getZ());
        slvl.addFreshEntity(cluster);

        // Consume all chunks
        for (ChunkOfFlesh c : all) {
            c.discard();
        }
    }


    public void evolveIntoHybrid() {
        if (!(this.level() instanceof ServerLevel slvl)) return;

        SiliconiteParticles.spawnBloodBurst(slvl, this.blockPosition());
        slvl.playSound(null, this.blockPosition(), SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE);

        BaseHybrid result = HYBRID_EVOLUTION_RESULTS.get(slvl.random.nextInt(HYBRID_EVOLUTION_RESULTS.size())).create(slvl);
        if (result == null) return;

        result.moveTo(this.getX(), this.getY(), this.getZ());
        slvl.addFreshEntity(result);
    }

    public void setBurrowAnimation(boolean bool) {
        doBurrowAnim = bool;
        entityData.set(IS_BURROWING, bool);
        if (!bool) _brw = true;
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        if (entityData.get(IS_BURROWING)) {
            super.push(0d, 0d, 0d);
        } else {
            super.push(pX, pY, pZ);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("is_burrowing", entityData.get(IS_BURROWING));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(IS_BURROWING, tag.getBoolean("is_burrowing"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_BURROWING, false);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        entityData.set(IS_BURROWING, false);
        return super.hurt(pSource, pAmount);
    }
}
