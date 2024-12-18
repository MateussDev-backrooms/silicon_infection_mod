package net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid;


import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

public class HomunculoidBase extends Monster {
    public final AnimationState AS_isIdle = new AnimationState();
    private int idleAnimTimeout = 0;

    public HomunculoidBase(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            setupAnimationStates();
        }
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


    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        this.setDeltaMovement(0, 0, 0);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void heal(float pHealAmount) {
        super.heal(pHealAmount);

        if(this.level() instanceof ServerLevel level) {
            for (int i = 0; i < 80; i++) {  // Spawn multiple particles for effect
                double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
                double offsetY = level.random.nextDouble() * 0.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;

                level.sendParticles(
                        ParticleTypes.CRIMSON_SPORE,
                        this.getX(),
                        this.getY() + 1,
                        this.getZ(),
                        1,          // Number of particles per spawn call
                        offsetX,    // X offset for randomness
                        offsetY,    // Y offset
                        offsetZ,    // Z offset
                        0.1         // Speed of the particle
                );
            }

            level.playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.MUD_PLACE, SoundSource.HOSTILE, 1f, 1f);
        }

    }
}
