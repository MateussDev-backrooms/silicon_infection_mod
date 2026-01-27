package com.mateussdev.chemosyntehsis.Entities.generic;

import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BaseMetabolized extends BaseTethered{
    protected BaseMetabolized(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected boolean isDodging = false;
    protected int dodgeAnim = 0;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 1, event ->
        {
            //death anim
            if(this.isDeadOrDying()) {
                return event.setAndContinue(RawAnimation.begin().then("death", Animation.LoopType.PLAY_ONCE));
            }

            if(isDodging) {
                switch(dodgeAnim) {
                    case 0:
                        return event.setAndContinue(RawAnimation.begin().then("dodge_1", Animation.LoopType.PLAY_ONCE));
                    case 1:
                        return event.setAndContinue(RawAnimation.begin().then("dodge_2", Animation.LoopType.PLAY_ONCE));
                }
            }



            return event.setAndContinue(
                    // If moving, play the walking animation
                    event.isMoving() ? RawAnimation.begin().thenLoop("walk"):
                            // If not moving, play the idle animation
                            RawAnimation.begin().thenLoop("idle"));
        }));
    }

    ///DOOOODGE mechanic
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(random.nextFloat() < 0.75f) {
            //Check Dodge conditions
            if(
                    (pSource.getEntity() instanceof LivingEntity || pSource.getDirectEntity() != null)
                    && !isPlayerCriticalHit(pSource) && onGround()
            ) {
                //DOOOOOOODGEEE
                triggerDodge(pSource);

                return false;
            }
        }
        return super.hurt(pSource, pAmount);
    }

    int dodgeTick = 0;

    protected void triggerDodge(DamageSource source) {
        isDodging = true;
        if(source.getEntity() instanceof LivingEntity att) {
            Vec3 rel = this.position().subtract(att.position());
            this.setYRot(att.yBodyRot - 180);
        }

        dodgeAnim = random.nextBoolean() ? 0 : 1;

        dodgeTick = 0;

        Vec3 pushDir = Vec3.ZERO;

        if (source.getEntity() != null) {
            pushDir = calculateDodgeDir(source.getEntity().getLookAngle().scale(-1), 60.0);
        } else {
            pushDir = this.getLookAngle().scale(-1);
        }

        this.getAnimatableInstanceCache()
                .getManagerForId(this.getId()).getAnimationControllers().get("movement").forceAnimationReset();
        this.setDeltaMovement(pushDir.scale(1.05));
        this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE);
        if(this.level() instanceof ServerLevel slvl) {
            slvl.sendParticles(
                    ParticleTypes.POOF,
                    this.getX() + 0.5,
                    this.getY() + 0.5,
                    this.getZ() + 0.5,
                    5,
                    0,
                    0,
                    0,
                    0.1
            );
        }
        this.hasImpulse = true;
    }

    public Vec3 calculateDodgeDir(Vec3 attackerLook, double coneAngleDeg) {
        Vec3 forward = attackerLook.normalize();

        Vec3 up = Math.abs(forward.y) < 0.99 ? new Vec3(0, 1, 0) : new Vec3(1, 0, 0);
        Vec3 right = forward.cross(up).normalize();

        double angleRad = Math.toRadians(coneAngleDeg);
        double yaw = (random.nextDouble() * 2 - 1) * angleRad;

        return forward
                .scale(-0.2)
                .add(right.scale(Math.sin(yaw)))
                .normalize();
    }

    //DOOOODGE conditions
    protected boolean isPlayerCriticalHit(DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return false;

        return player.fallDistance > 0.0F
                && !player.onGround()
                && !player.isInWater()
                && !player.isPassenger();
    }


    @Override
    public void tick() {
        super.tick();

        if (isDodging) {
            ++dodgeTick;

            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.getNavigation().stop();


            if (dodgeTick > 10) {
                isDodging = false;
                dodgeTick = 0;
            }
        }
    }

    public void tickDeath() {
        if(explodeOnDeath()) {
            ++this.deathTime;

            if(this.level() instanceof ServerLevel slvl) {
                if (this.deathTime == 40) {
                    turnIntoBiomush();
                    this.releaseGasIntoAtmosphere(slvl, 1f);
                    this.level().broadcastEntityEvent(this, (byte)60);
                    this.remove(RemovalReason.KILLED);
                }
            }

        } else {
            super.tickDeath();
        }
    }
}
