package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalAttackGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalFleeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;

public class BaseSiliconite extends Monster implements GeoEntity {
    protected BaseSiliconite(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    //##### ENTITY CUSTOM DATA #####//

    public static final EntityDataAccessor<Integer> METABOLISM_VALUE = SynchedEntityData.defineId(BaseSiliconite.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> BROKEN_OFF_BULBS_VALUE = SynchedEntityData.defineId(BaseSiliconite.class, EntityDataSerializers.INT);

    //##### ===== #####//


    //##### ANIMATION STUFF #####//

    private final AnimatableInstanceCache anim_cache = AzureLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return anim_cache;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(
                    // If moving, play the walking animation
                    event.isMoving() ? RawAnimation.begin().thenLoop("walk"):
                            // If not moving, play the idle animation
                            RawAnimation.begin().thenLoop("idle"));
        }));
    }


    //##### ===== #####//

    //##### GENERAL ENTITY LOGIC #####//

    // - Entity AI

    public static final Set<EntityType<? extends LivingEntity>> BLACKLISTED_MOBS = Set.of(
            EntityType.CREEPER,
            EntityType.BAT
    );


    protected boolean shouldTargetMob(LivingEntity entity) {
        //Don't target mobs of this namespace
        if(StaticSiliconiteMethods.isMobFromChemosynthesisMod(entity)) return false;

        //Don't target blacklisted mobs
        return !BLACKLISTED_MOBS.contains(entity);
    }

    @Override
    protected void registerGoals() {
        //Default settings override when new behavior is required

        // - GOALS
        if(!isBrave()) {
            //Attack targets (condition customizeable)
            this.goalSelector.addGoal(1, new ConditionalAttackGoal(this, 1.0f, true, this::shouldAttackTarget));
            //Flee players (condition customizeable)
            this.goalSelector.addGoal(0, new ConditionalFleeGoal(this, LivingEntity.class, 16.0f, 1.2d, 1.3d, this::shouldFlee));
        } else {
            //if brave it will always attack no matter the health/should flee conditions
            this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.0f, true));
        }

        //Avoid water (No float task cuz they are immune to water damage)
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        // - TARGETS
        if(shouldAlertOthersOnHurt()) {
            //get aggressive and alert
            this.targetSelector.addGoal(1, (new HurtByNonSiliconiteGoal(this, new Class[0])).setAlertOthers(new Class[]{BaseSiliconite.class}));
        } else {
            //only get aggressive
            this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));
        }

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, this::shouldTargetMob));
    }

    // - Entity base behavior

    private int t = 0;
    @Override
    public void baseTick() {
        super.baseTick();

        //prevent drowning
        this.setAirSupply(this.getMaxAirSupply());

        if (!this.level().isClientSide) {
            t++;

            //Metabolism stuffs
            if (t % 40 == 0) {
                //add to metabolism
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain());
            }
            //increase metabolism every tick by a lot if on fire
            if (isOnFire()) {
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain() * 5);

            }

            //Evolution
            if (entityData.get(METABOLISM_VALUE) >= evolvesAtMetabolism()) {
                entityData.set(METABOLISM_VALUE, 0);
                this.evolve();
            }
        }
    }

    //##### ===== #####//

    //##### ENTITY OVERRIDES N FUNCTIONALITY #####//
    //stuff that overrides upper classes like sounds behaviors and interaction with the world

    //Custom hurt mechanics

    private int brokenOffBulbs = 0;
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        //only get dealt 10% of fire damage
        if(pSource.is(DamageTypeTags.IS_FIRE) || pSource.is(DamageTypeTags.BURNS_ARMOR_STANDS) || pSource.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            pAmount *= 0.1F;
        }

        if(level () instanceof ServerLevel slvl ) {

            //Break off bulb
            if(slvl.random.nextFloat() < getBulbBreakoffChance()) {


                if(entityData.get(BROKEN_OFF_BULBS_VALUE) < getBulbCount()) {
                    BulbProjectileEntity shard = new BulbProjectileEntity(level(),this);
                    shard.shoot(
                            level().random.triangle(0, 1),
                            level().random.triangle(0.2, 1),
                            level().random.triangle(0, 1),
                            0.4f,
                            10.0f
                    );
                    level().addFreshEntity(shard);
                    entityData.set(BROKEN_OFF_BULBS_VALUE, entityData.get(BROKEN_OFF_BULBS_VALUE) + 1);
                }
            }
        }

        return super.hurt(pSource, pAmount);
    }

    //On hurt others
    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean success = super.doHurtTarget(pEntity);
        if(success) {
            //Check if attacking entity
            if(pEntity instanceof LivingEntity le) {
                //Add energy on hurting target
                //TODO add Tethered effect when hurt
                //TODO ride/attach to entity that can be tethered
            }
        }
        return success;
    }

    @Override
    public void awardKillScore(Entity killed, int score, DamageSource source) {
        super.awardKillScore(killed, score, source);

        if(level().random.nextFloat() < getTetherChance()) {
            if (!this.level().isClientSide && killed instanceof LivingEntity victim && source.getEntity() == this) {
                if (StaticSiliconiteMethods.isTetherable(victim)) {
                    StaticSiliconiteMethods.tetherMob((ServerLevel) level(), victim);

                    if(destructiveTether()) {
                        this.remove(RemovalReason.DISCARDED);
                    }
                }
            }
        }
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    //##### ===== #####//


    //##### CUSTOMIZEABLE STUFF #####//

    //stuff here can be overriden to customize the behavior or the siliconite
    //should alert nearby siliconites when hurt

    //AI
    protected boolean shouldAlertOthersOnHurt () { return false; }

    //Determines if the entity will run away for its life when at low health. Cannot and should not change in gameplay
    protected boolean isBrave () { return false; }

    protected boolean shouldAttackTarget ( boolean _idk){
        //Default behavior is to stop when health is below 30%
        //ignore the boolean arg it's always true. I just didn't figure predicates properly yet
        return getHealth() / getMaxHealth() > 0.4f;
    }

    protected boolean shouldFlee ( Object _idk){
        //Default behavior is to flee when health is below 30% as well as requiring the target to not be null
        return getHealth() / getMaxHealth() < 0.4f && getTarget() != null;
    }

    //Stats n stuff
    protected int getMetabolismGain() {
        //TODO implement different gain depending on biome temperature, time of day and Y level
        return 1;
    }

    //Defines the chance between 0 and 1 for this siliconite to tether a mob that can be tethered
    protected float getTetherChance() { return 1.0F; }

    //Defines the chance between 0 and 1 for a bulb to break off on attack
    protected float getBulbBreakoffChance() { return 0.4F; }

    public int getBulbCount() { return 0; }

    public int getBrokenOffBulbs() { return entityData.get(BROKEN_OFF_BULBS_VALUE); }

    public GeoBone[] getBulbsArray(GeoModel<?> model) {
      GeoBone[] bulbs = {

      };
      return bulbs;
    };

    //Defines whether the siliconite will destroy itself after tethering
    protected boolean destructiveTether() { return true; }

    protected int evolvesAtMetabolism() {
        //Defines at what point the organism evolves
        return 100;
    }

    public void evolve() {
        //This runs when the metabolism reaches the required point amount and lets the organism to evolve
        //Override functionality here
    }

    public void vegetate() {
        //This runs when the energy is very low, requiring the organism to adapt to conserve energy
        //Override functionality here
    }

    public void tether(LivingEntity target) {
        //This runs when the siliconite wishes to tether a mob
        //Default functionality is to tether the mob and discard if destructiveTether is true
        //Override when necessary
        if(level() instanceof ServerLevel slvl) {
            StaticSiliconiteMethods.tetherMob(slvl, target);
            //discard if destructive
            if(destructiveTether()) {
                this.discard();
            }
        }

    }

    //##### ===== #####//


    //##### SAVING AND LOADING #####//
    @Override
    public void addAdditionalSaveData (CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("metabolism_value", entityData.get(METABOLISM_VALUE));
        tag.putInt("broken_off_bulbs_value", entityData.get(BROKEN_OFF_BULBS_VALUE));
    }

    @Override
    public void readAdditionalSaveData (CompoundTag tag){
        super.readAdditionalSaveData(tag);
        entityData.set(METABOLISM_VALUE, tag.getInt("metabolism_value"));
        entityData.set(BROKEN_OFF_BULBS_VALUE, tag.getInt("broken_off_bulbs_value"));
    }

    @Override
    protected void defineSynchedData () {
        super.defineSynchedData();
        this.entityData.define(METABOLISM_VALUE, 0);
        this.entityData.define(BROKEN_OFF_BULBS_VALUE, 0);
    }

    //##### ===== #####//
}
