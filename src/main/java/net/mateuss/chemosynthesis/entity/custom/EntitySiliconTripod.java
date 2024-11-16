package net.mateuss.chemosynthesis.entity.custom;

import net.mateuss.chemosynthesis.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class EntitySiliconTripod extends Monster {
    public EntitySiliconTripod(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState AS_isIdle = new AnimationState();
    private int idleAnimTimeout = 0;

    private int growTimer = 1800; //1:30 mins to grow into a roller

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        this.setAirSupply(this.getMaxAirSupply());

        if(!this.level().isClientSide()) {
            --this.growTimer;
            if(this.growTimer <= 0) {
                EntitySiliconRoller roller = ModEntities.SILICON_ROLLER.get().create(this.level());
                if(roller != null) {
                    roller.moveTo(this.getX()+this.random.nextDouble(), this.getY(), this.getZ()+this.random.nextDouble());
                    this.level().addFreshEntity(roller);
                    ServerLevel lvl = (ServerLevel) this.level();
                    lvl.sendParticles(
                            ParticleTypes.EXPLOSION,
                            this.getX() + 0.5,
                            this.getY() + 0.5,
                            this.getZ() + 0.5,
                            1,          // Number of particles per spawn call
                            0,    // X offset for randomness
                            0,    // Y offset
                            0,    // Z offset
                            0.1         // Speed of the particle
                    );
                    this.discard();
                }
            }
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
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.0d));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 3D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_DAMAGE, 1D);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.GUARDIAN_AMBIENT_LAND;
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
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public static class FleePlayerGoal extends Goal {
        private final EntitySiliconTripod mob;
        private final double speed;
        private final double fleeDistance;
        private final PathNavigation pathNavigation;
        private Path path;

        public FleePlayerGoal(EntitySiliconTripod mob, double speed, double fleeDistance) {
            this.mob = mob;
            this.speed = speed;
            this.fleeDistance = fleeDistance;
            this.pathNavigation = mob.getNavigation();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            // Check for nearby players and if the mob's size is too small
            return mob.level().getNearestPlayer(mob, fleeDistance) != null;

        }

        @Override
        public void start() {
            Player nearestPlayer = mob.level().getNearestPlayer(mob, fleeDistance);
            if (nearestPlayer != null) {
                Vec3 $$0 = DefaultRandomPos.getPosAway(this.mob, 16, 7,
                        nearestPlayer.position());
                if ($$0 != null) {
                    this.path = this.pathNavigation.createPath($$0.x, $$0.y, $$0.z, 0);
                }

                // Move the mob away from the player
                this.pathNavigation.moveTo(this.path, speed);
            }
        }

        @Override
        public boolean canContinueToUse() {
            // Continue fleeing while the player is still close
            return mob.level().getNearestPlayer(mob, fleeDistance) != null;
        }
    }
}
