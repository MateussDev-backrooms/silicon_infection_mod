package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte;

import com.mateussdev.chemosyntehsis.Entities.generic.AI.*;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class HybridErythrocyte extends BaseHybrid {
    private final ErythrocyteStateManager stateManager = new ErythrocyteStateManager(this);

    public HybridErythrocyte(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new ImprovedFlyingMoveControl(this, 1f, true);
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected void registerGoals() {
//        this.goalSelector.addGoal(1, new ErythrocyteRetreatGoal(this)); // Highest priority when retreating
        this.goalSelector.addGoal(0, new ErythrocyteDeployMobGoal(this));
        this.goalSelector.addGoal(1, new ErythrocytePickUpMobGoal(this));
//        this.goalSelector.addGoal(2, new FloatingLookAtTargetGoal(this));
        this.goalSelector.addGoal(2, new FloatingSiliconiteRandomStrollGoal(this, 7f, 4f));

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return true; // Can pathfind anywhere
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean isNoGravity() {
        return !isDeadOrDying();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        super.tick();
//        stateManager.tick();
        if(level() instanceof ServerLevel slvl) {
            //Float upward when on ground
            float minFlyDist = this.getTarget() == null ? 1f : getTarget().getBbHeight();
            BlockHitResult raycastDown = slvl.clip(new ClipContext(this.position(), this.position().add(0, -minFlyDist, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if(raycastDown.getType() != HitResult.Type.MISS) {
                setDeltaMovement(getDeltaMovement().add(0, 0.05, 0));
            }

            //Float down when hitting ceiling
            BlockHitResult raycastUp = slvl.clip(new ClipContext(this.position(), this.position().add(0, 1, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if(raycastUp.getType() != HitResult.Type.MISS) {
                setDeltaMovement(getDeltaMovement().add(0, -0.05, 0));
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(level() instanceof ServerLevel slvl) {

            if(getTarget() != null) {
                if(distanceTo(getTarget()) <= 2 && getFirstPassenger() != null) {
                    ejectPassengers();
                }
            } else {
                if(!getPassengers().isEmpty()) {
                    ejectPassengers();
                }
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        if(this.getFirstPassenger() != null) {
            return -getFirstPassenger().getBbHeight();
        }
        return -1.7;
    }

    public ErythrocyteStateManager getStateManager() {
        return stateManager;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }


    private int deathTime = 0;
    @Override
    protected void tickDeath() {
        if (this.level() instanceof ServerLevel slvl) {
            ++this.deathTime;
            this.setNoGravity(false);

            if (this.deathTime >= 60) {
                slvl.explode(this, this.getX(), this.getY(), this.getZ(), 1.5f, Level.ExplosionInteraction.MOB);
                StaticSiliconiteMethods.spawnBloodBurst(slvl, this.blockPosition());
                this.discard();
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        ejectPassengers();
        return super.hurt(pSource, pAmount);
    }
}
