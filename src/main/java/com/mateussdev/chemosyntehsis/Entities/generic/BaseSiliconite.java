package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalAttackGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalFleeGoal;
import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class BaseSiliconite extends Monster implements GeoEntity {
    protected BaseSiliconite(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    //##### ENTITY CUSTOM DATA #####//

    public static final EntityDataAccessor<Integer> METABOLISM_VALUE = SynchedEntityData.defineId(BaseSiliconite.class, EntityDataSerializers.INT);

    //This value determines how much energy the siliconite has left. When it reaches zero it turns vegetative
    //Optional as the function can be overriden to do nothing
    //By default slows down the siliconite by 75%
    public static final EntityDataAccessor<Integer> ENERGY = SynchedEntityData.defineId(BaseSiliconite.class, EntityDataSerializers.INT);

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
        controllers.add(new AnimationController<>(this, "movement", 0, event ->
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
    private boolean shouldTargetMob(LivingEntity entity) {
        //Targets entities outside of this mod by comparing their namespace

        //TODO: Add ability to blacklist mobs and/or namespaces

        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return !entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

    @Override
    protected void registerGoals() {
        //Default settings override when new behavior is required

        // - GOALS
        //Attack targets (condition customizeable)
        this.goalSelector.addGoal(1, new ConditionalAttackGoal(this, 1.2f, true, this::shouldAttackTarget));
        //Flee players (condition customizeable)
        this.goalSelector.addGoal(0, new ConditionalFleeGoal(this, LivingEntity.class, 16.0f, 1.3d, 1.5d, this::shouldFlee));

        //Avoid water (No float task cuz they are immune to water damage)
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        // - TARGETS
        if(shouldAlertOthersOnHurt()) {
            //get aggressive and alert
            this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[]{BaseSiliconite.class}));
        } else {
            //only get aggressive
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        }

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, this::shouldTargetMob));
    }

    // - Entity base behavior

    private int t = 0;
    @Override
    public void baseTick() {
        super.baseTick();
        super.baseTick();

        //prevent drowning
        this.setAirSupply(this.getMaxAirSupply());

        if (!this.level().isClientSide) {
            t++;

            //metabolism stuffs
            if (t % 40 == 0) {
                //add to metabolism
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain());
            }
            //increase metabolism every tick by a lot if on fire
            if (isOnFire()) {
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain() * 5);
                entityData.set(ENERGY, entityData.get(ENERGY) + 1);

            }

            //evolution
            if (entityData.get(METABOLISM_VALUE) >= evolvesAtMetabolism()) {
                entityData.set(METABOLISM_VALUE, 0);
                this.evolve();
            }

            //energy stuffs
            if (entityData.get(ENERGY) <= 0 && entityData.get(ENERGY) > -999) {
                this.vegetate();
                entityData.set(ENERGY, -999);
            }
            if (t % 20 == 0) {
                //remove energy
                entityData.set(ENERGY, entityData.get(ENERGY) - 1);
            }
        }
    }

    //##### ===== #####//

    //##### ENTITY OVERRIDES N FUNCTIONALITY #####//

    //stuff that overrides upper classes like sounds behaviors and interaction with the world


    //##### ===== #####//


    //##### CUSTOMIZEABLE STUFF #####//

    //stuff here can be overriden to customize the behavior or the siliconite
    //should alert nearby siliconites when hurt

    //AI
    protected boolean shouldAlertOthersOnHurt () {
        return false;
    }

    protected boolean shouldAttackTarget ( boolean _idk){
        //Default behavior is to stop when health is below 30%
        //ignore the boolean arg it's always true. I just didn't figure predicates properly yet
        return getHealth() / getMaxHealth() > 0.5f;
    }

    protected boolean shouldFlee ( Object _idk){
        //Default behavior is to flee when health is below 30% as well as requiring the target to not be null
        return getHealth() / getMaxHealth() < 0.5f && getTarget() != null;
    }

    //Stats n stuff
    protected int getMetabolismGain() {
        //TODO implement different gain depending on biome temperature, time of day and Y level
        return 1;
    }

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

    //##### ===== #####//


    //##### SAVING AND LOADING #####//
    @Override
    public void addAdditionalSaveData (CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("metabolism_value", entityData.get(METABOLISM_VALUE));
        tag.putInt("energy", entityData.get(ENERGY));
    }

    @Override
    public void readAdditionalSaveData (CompoundTag tag){
        super.readAdditionalSaveData(tag);
        entityData.set(METABOLISM_VALUE, tag.getInt("metabolism_value"));
        entityData.set(ENERGY, tag.getInt("energy"));
    }

    @Override
    protected void defineSynchedData () {
        super.defineSynchedData();
        this.entityData.define(METABOLISM_VALUE, 0);
        this.entityData.define(ENERGY, 100);
    }

    //##### ===== #####//
}
