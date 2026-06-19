package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalAttackGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalFleeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.GlobalWarming.GlobalWarmingData;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseSiliconite extends Monster implements GeoEntity {

    protected BaseSiliconite(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    // ===== ENTITY CUSTOM DATA ===== //

    public static final EntityDataAccessor<Integer> METABOLISM_VALUE = SynchedEntityData.defineId(BaseSiliconite.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BROKEN_OFF_BULBS_VALUE = SynchedEntityData.defineId(BaseSiliconite.class, EntityDataSerializers.INT);

    // ===== ENTITY CONSTANTS ===== //
    //TODO: Hook up with config
    private static final float FLEE_HEALTH_THRESHOLD = 0.4f;
    private static final float TETHER_HEALTH_REQUIREMENT = 0.15f;
    private static final float FIRE_DAMAGE_MULTIPLIER = 0.05f;

    // ===== ENTITY VARS ===== //
    //Change in mob constructor
    public int bulbCount = 0;
    protected float bulbBreakoffChance = 0.4f;

    protected float tetherChance = 1.0f;
    protected boolean discardOnTether = false;

    protected int metabolismEvolutionThreshold = 100;

    // ===== ANIMATION STUFF ===== //
    private final AnimatableInstanceCache anim_cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return anim_cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
                event.setAndContinue(
                        event.isMoving() ?
                                RawAnimation.begin().thenLoop("walk") : //Walk anim when moving
                                RawAnimation.begin().thenLoop("idle")))); //Idle anim when not moving
    }

    // ===== GENERAL ENTITY LOGIC ===== //

    @Override
    protected void registerGoals() {
        //Default settings override when new behavior is required

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

    @Override
    public void baseTick() {
        super.baseTick();

        if(!canDrown()) {
            //prevent drowning
            this.setAirSupply(this.getMaxAirSupply());
        }

        //Prevent targeting tethered mobs
        if(this.getTarget() != null) {
            if(this.getTarget() instanceof Mob mob && UniversalTethering.isTethered(mob)) {
                this.setTarget(null);
                //Stop the target selector
                if(this.targetSelector.getRunningGoals().findFirst().isPresent()) {
                    this.targetSelector.getRunningGoals().findFirst().get().stop();
                }
            }
        }

        if (this.level() instanceof ServerLevel slvl) {

            //Metabolism stuffs
            if (tickCount % 40 == 0) {
                //add to metabolism
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain());
            }
            //increase metabolism every tick by a lot if on fire
            if (isOnFire()) {
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain() * 2);

            }

            //Evolution
            if (entityData.get(METABOLISM_VALUE) >= metabolismEvolutionThreshold) {
                entityData.set(METABOLISM_VALUE, 0);
                this.evolve();
            }
        }
    }

    // ===== ENTITY OVERRIDES N FUNCTIONALITY ===== //
    //stuff that overrides upper classes like sounds behaviors and interaction with the world

    //Custom hurt mechanics

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        //only get dealt 5% of fire damage
        float damageMultiplier = 1f;
        if (pSource.is(DamageTypeTags.IS_FIRE)) {
            damageMultiplier = FIRE_DAMAGE_MULTIPLIER;
        }

        boolean wasHurt = super.hurt(pSource, pAmount * damageMultiplier);

        if(wasHurt) {
            if (this.level() instanceof ServerLevel slvl) {
                //Break off bulb
                if (slvl.random.nextFloat() < bulbBreakoffChance) {
                    if (entityData.get(BROKEN_OFF_BULBS_VALUE) < bulbCount) {
                        BulbProjectileEntity shard = new BulbProjectileEntity(this.level(), this);
                        shard.shoot(
                                this.level().random.triangle(0, 1),
                                this.level().random.triangle(0.2, 1),
                                this.level().random.triangle(0, 1),
                                0.4f,
                                10.0f
                        );
                        this.level().addFreshEntity(shard);
                        entityData.set(BROKEN_OFF_BULBS_VALUE, entityData.get(BROKEN_OFF_BULBS_VALUE) + 1);
                    }
                }

                //Blood effect
                SiliconiteParticles.spawnBloodHit(slvl, this.position());
            }
        }

        return wasHurt;
    }

    //On hurt others
    @Override
    public boolean doHurtTarget(Entity pEntity) {
        //Check if attacking entity
        if (pEntity instanceof Mob mob) {
            if((mob.getHealth()-this.getAttributeValue(Attributes.ATTACK_DAMAGE))/mob.getMaxHealth() < TETHER_HEALTH_REQUIREMENT) {
                if(this.level() instanceof ServerLevel slvl) {
                    UniversalTethering.tryTetherMob(mob, slvl);
                    if (discardOnTether) {
                        this.remove(RemovalReason.DISCARDED);
                    }
                    return false;
                }
            }
        }
        return super.doHurtTarget(pEntity);
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }


    // ===== CUSTOMIZABLE STUFF ===== //

    //stuff here can be overridden to customize the behavior or the siliconite
    //should alert nearby siliconites when hurt

    //AI
    protected boolean shouldAlertOthersOnHurt() {
        return false;
    }

    //Determines if the entity will run away for its life when at low health. Cannot and should not change in gameplay
    protected boolean isBrave() {
        return false;
    }

    protected boolean shouldAttackTarget(boolean _idk) {
        //Default behavior is to stop when health is below
        //ignore the boolean arg it's always true. I just didn't figure predicates properly yet
        return getHealth() / getMaxHealth() > FLEE_HEALTH_THRESHOLD;
    }

    protected boolean shouldFlee(Object _idk) {
        //Default behavior is to flee when health is below FLEE_THRESHOLD as well as requiring the target to not be null
        return getHealth() / getMaxHealth() < FLEE_HEALTH_THRESHOLD && getTarget() != null;
    }

    //Stats n stuff
    protected int getMetabolismGain() {
        //TODO implement different gain depending on biome temperature, time of day and Y level
        return 1;
    }

    protected boolean canDrown() {
        return false;
    }

    protected void releaseGasIntoAtmosphere(ServerLevel pLevel, float amount) {
        GlobalWarmingData globalWarmingData = GlobalWarmingData.get(pLevel);
        globalWarmingData.addPoints(amount);
        pLevel.playSound(null, this.blockPosition(), SoundEvents.CANDLE_EXTINGUISH, SoundSource.HOSTILE);
    }

    //Defines the chance between 0 and 1 for this siliconite to tether a mob that can be tethered
    @Deprecated
    protected float getTetherChance() {
        return 1.0F;
    }

    //Defines the chance between 0 and 1 for a bulb to break off on attack
    @Deprecated
    protected float getBulbBreakoffChance() {
        return 0.4F;
    }

    @Deprecated
    public int getBulbCount() {
        return 0;
    }

    public int getBrokenOffBulbs() {
        return entityData.get(BROKEN_OFF_BULBS_VALUE);
    }

    public abstract GeoBone[] getBulbsArray(GeoModel<?> model) ;

    //Defines whether the siliconite will destroy itself after tethering
    @Deprecated
    protected boolean destructiveTether() {
        return true;
    }

    @Deprecated
    protected int evolvesAtMetabolism() {
        //Defines at what point the organism evolves
        return 100;
    }

    public void evolve() {
        //This runs when the metabolism reaches the required point amount and lets the organism evolve
        //Override functionality here
    }


    // ===== SAVING AND LOADING ===== //
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("metabolism_value", entityData.get(METABOLISM_VALUE));
        tag.putInt("broken_off_bulbs_value", entityData.get(BROKEN_OFF_BULBS_VALUE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(METABOLISM_VALUE, tag.getInt("metabolism_value"));
        entityData.set(BROKEN_OFF_BULBS_VALUE, tag.getInt("broken_off_bulbs_value"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(METABOLISM_VALUE, 0);
        this.entityData.define(BROKEN_OFF_BULBS_VALUE, 0);
    }
}
