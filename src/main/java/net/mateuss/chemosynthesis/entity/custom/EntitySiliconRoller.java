package net.mateuss.chemosynthesis.entity.custom;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import org.apache.logging.log4j.core.jmx.Server;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

public class EntitySiliconRoller extends Monster {
    public EntitySiliconRoller(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState AS_isIdle = new AnimationState();
    private int idleAnimTimeout = 0;
    public int shakeTimer = 0;

    private TetherMobAction tetherMobAction = new TetherMobAction();

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            setupAnimationStates();
        }

        if(this.isOnFire()) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            shakeTimer++;
        } else {
            shakeTimer = 0;
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.setAirSupply(this.getMaxAirSupply());
    }

    private void setupAnimationStates() {
        if(this.idleAnimTimeout <= 0) {
            this.idleAnimTimeout = this.random.nextInt(40)+80;
            this.AS_isIdle.start(this.tickCount);
        } else {
            --this.idleAnimTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f=0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }
    private boolean shouldTarget(LivingEntity entity) {

        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        // Check if the entity is not part of your mod
        return entityTypeKey == null || !entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }



    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(2, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.1D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[]{EntitySiliconRoller.class}));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, this::shouldTarget));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 12D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 4D);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.GUARDIAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.DRIPSTONE_BLOCK_PLACE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DRIPSTONE_BLOCK_FALL;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void remove(RemovalReason pReason) {
        tetherMobAction.spawnTripods((ServerLevel) this.level(), this, 1, 2);
        super.remove(pReason);
    }


    @Override
    public boolean killedEntity(ServerLevel pLevel, LivingEntity pEntity) {

        tetherMobAction.TetherMob(pLevel, pEntity, this, true);

        return super.killedEntity(pLevel, pEntity);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(this.isOnFire()) {

            spawnFlameParticles();

            int metabolismTransformTimer = this.getPersistentData().getInt("MetabolismTimer");
            metabolismTransformTimer++;
            this.getPersistentData().putInt("MetabolismTimer", metabolismTransformTimer);

            if(metabolismTransformTimer >= 40) {
                evolveIntoMetabolised();
            }
        } else {
            this.getPersistentData().putInt("MetabolismTimer", 0);
        }
    }

    private void spawnFlameParticles() {
        if(!this.level().isClientSide) {
            ServerLevel lvl = (ServerLevel) this.level();

            lvl.sendParticles(
                    ParticleTypes.FLAME,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    1, 0, 1, 0, 0.1
            );
        }
    }

    private void evolveIntoMetabolised() {
        if (!this.level().isClientSide) {

            Entity newEntity = new EntitySilipede(ModEntities.SILIPEDE.get(), this.level());
            newEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());


            this.discard();
            this.level().addFreshEntity(newEntity);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(pSource.is(DamageTypes.IN_FIRE) || pSource.is(DamageTypes.ON_FIRE) || pSource.is(DamageTypes.LAVA)
        || pSource.is(DamageTypes.HOT_FLOOR)) {
            return false;
        } else if(pSource.is(DamageTypes.MAGIC)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }
}
