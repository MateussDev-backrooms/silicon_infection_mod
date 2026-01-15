package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte;

import com.mateussdev.chemosyntehsis.Entities.generic.AI.*;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

public class HybridPerfocyte extends BaseHybrid {
    public HybridPerfocyte(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new ImprovedFlyingMoveControl(this, 0.8f, true);
    }

    // Constants
    private static final float MAX_ANGER_LEVEL = 5.0f;
    private static final float ANGLE_LIMIT = 45.0f; // Max 45 degree angle for tunnels
    private static final float DASH_SPEED = 0.95f;
    private static final int MAX_DASH_DURATION = 40; // 2 seconds at 20 TPS
    private static final float BLOCK_DAMAGE_PER_TICK = 0.05f; // Progress per collision tick

    // Synced data
    private float angerLevel = 0.0f;
    private int dashCooldown = 0;
    private int attackCooldown = 0;
    private boolean isDashing = false;
    private int dashDuration = 0;
    private LivingEntity dashTarget = null;
    private Vec3 dashDirection = Vec3.ZERO;

    // Block breaking
    private BlockPos lastDamagedBlock = null;
    private float accumulatedDamage = 0.0f;


    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 38D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 40D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    // ===== AI n pathfinding ===== //

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new PerfocyteDashGoal(this));
        this.goalSelector.addGoal(1, new PerfocyteTunnelGoal(this));
        this.goalSelector.addGoal(2, new PerfocyteLookAtTargetGoal(this));
        this.goalSelector.addGoal(2, new FloatingSiliconiteRandomStrollGoal(this, 7f, 4f));

        // Seek out targets (no line of sight requirement)
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this, Player.class, 10, true, false, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 10, true, false,
                entity -> entity instanceof Player || StaticSiliconiteMethods.shouldAttackMob(entity)));
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

    // ===== LOGIC ===== //

    @Override
    public void tick() {
        super.tick();
        // Update cooldowns
        if (dashCooldown > 0) dashCooldown--;
        if (attackCooldown > 0) attackCooldown--;

        // Handle dashing state
        if (isDashing) {
            dashDuration++;

            // Check for block collisions
            checkBlockCollisions();

            // Check for entity collisions
            checkEntityCollisions();

            // End dash after duration
            if (dashDuration > getMaxDashDuration() ||
                    (dashTarget != null && dashTarget.isDeadOrDying())) {
                endDash(false);
            }

            // Apply dash movement
            if (!level().isClientSide) {
                Vec3 motion = dashDirection.scale(DASH_SPEED * (1.0f + angerLevel * 0.1f));
                this.setDeltaMovement(motion);
                this.hasImpulse = true;
            }
        } else if (dashDuration > 0) {
            // Apply friction after dash
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            dashDuration = Math.max(0, dashDuration - 1);
        }

        // Gradually reduce anger when not dashing
        if (!isDashing && angerLevel > 0 && tickCount % 20 == 0) {
            angerLevel = Math.max(0, angerLevel - 0.2f);
        }
    }

    private boolean hasHit = false;
    private void checkBlockCollisions() {
        if (!(level() instanceof ServerLevel slvl)) return;

        // Check blocks around the entity
        AABB collisionBox = this.getBoundingBox().inflate(0.3);
        BlockPos.betweenClosedStream(
                BlockPos.containing(collisionBox.minX, collisionBox.minY, collisionBox.minZ),
                BlockPos.containing(collisionBox.maxX, collisionBox.maxY, collisionBox.maxZ)
        ).forEach(blockPos -> {
            BlockState state = slvl.getBlockState(blockPos);

            // Skip air and unbreakable blocks
            if (state.isAir() || state.getDestroySpeed(slvl, blockPos) < 0) {
                return;
            }

            // Calculate damage based on block hardness and anger level
            float damage = BLOCK_DAMAGE_PER_TICK;
            float destroySpeed = state.getDestroySpeed(slvl, blockPos);

            if (destroySpeed > 0) {
                damage /= destroySpeed; // Harder blocks take longer
            }

            accumulatedDamage += damage;

            // Show block breaking progress
            int progress = Mth.clamp((int)(accumulatedDamage * 10), 0, 10);
            if (progress > 0) {
                slvl.destroyBlockProgress(this.getId(), blockPos, progress);
            }

            // Break the block when damage is sufficient
            if (accumulatedDamage >= 1.0f) {
                if (slvl.destroyBlock(blockPos, false, this)) {
                    // Block broken - play effect
                    slvl.playSound(null, blockPos,
                            state.getSoundType().getBreakSound(),
                            SoundSource.BLOCKS, 1.0f, 0.9f + random.nextFloat() * 0.2f);


                }
                accumulatedDamage = 0.0f;
            }
            hasHit = true;
        });

        if(hasHit) {
            setDeltaMovement(getDeltaMovement().scale(-1f));
            hasHit = false;
        }
    }

    private void checkEntityCollisions() {
        if (!(level() instanceof ServerLevel serverLevel) || dashTarget == null) return;

        AABB collisionBox = this.getBoundingBox().inflate(0.5);
        for (LivingEntity entity : serverLevel.getEntitiesOfClass(
                LivingEntity.class, collisionBox, e -> e != this && e.isAlive())) {

            if (entity == dashTarget) {
                // Successfully hit target
                boolean didDamage = doHurtTarget(entity);
                if (didDamage) {
                    // Reset anger on successful hit
                    angerLevel = 0.0f;
                    endDash(true);
                    playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1.0f, 0.8f);
                } else {
                    // Target blocked or dodged - increase anger
                    angerLevel = Math.min(MAX_ANGER_LEVEL, angerLevel + 1.0f);

                    // Deflect based on target's look direction
                    Vec3 deflect = entity.getLookAngle().scale(-0.5);
                    this.setDeltaMovement(this.getDeltaMovement().add(deflect));
                }
                break;
            }
        }
    }

    public void startDash(LivingEntity target) {
        if (dashCooldown > 0 || isDashing) return;

        this.dashTarget = target;
        this.isDashing = true;
        this.dashDuration = 0;
        this.accumulatedDamage = 0.0f;

        // Calculate dash direction with angle limit
        Vec3 toTarget = target.getEyePosition().subtract(this.position());

        // Enforce 45-degree maximum angle for climbable tunnels
        double horizontalDist = Math.sqrt(toTarget.x * toTarget.x + toTarget.z * toTarget.z);
        double maxVertical = horizontalDist * Math.tan(Math.toRadians(ANGLE_LIMIT));

        if (Math.abs(toTarget.y) > maxVertical) {
            toTarget = new Vec3(
                    toTarget.x,
                    Math.signum(toTarget.y) * maxVertical,
                    toTarget.z
            );
        }

        this.dashDirection = toTarget.normalize();

        // Play dash sound
        playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 0.4f);

        // Cooldown based on anger level (angrier = shorter cooldown)
        dashCooldown = Math.max(10, 40 - (int)(angerLevel * 4));
    }

    private void endDash(boolean success) {
        this.isDashing = false;
        this.dashTarget = null;

        if (!success && dashDuration > 20) {
            // Failed dash - increase anger based on how long we dashed
            angerLevel = Math.min(MAX_ANGER_LEVEL, angerLevel + (dashDuration / 40.0f));
        }

        // Reset movement
        this.setDeltaMovement(this.getDeltaMovement().scale(0.3));
    }


    // ===== DATA ===== //

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("anger_level", this.angerLevel);
        tag.putInt("dash_cooldown", this.dashCooldown);
        tag.putInt("attack_cooldown", this.attackCooldown);
        tag.putBoolean("is_dashing", this.isDashing);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.angerLevel = tag.getFloat("anger_level");
        this.dashCooldown = tag.getInt("dash_cooldown");
        this.attackCooldown = tag.getInt("attack_cooldown");
        this.isDashing = tag.getBoolean("is_dashing");
    }

    // ===== GETTERS ===== //
    private int getMaxDashDuration() {
        return MAX_DASH_DURATION + (int)(angerLevel * 5);
    }

    public float getAngerLevel() {
        return angerLevel;
    }

    public boolean isDashing() {
        return isDashing;
    }

    public int getDashCooldown() {
        return dashCooldown;
    }

    public RandomSource getRandom() { return random; }

    // Override to see through blocks
    @Override
    public boolean hasLineOfSight(Entity entity) {
        // Always return true for targets within range (x-ray vision)
        return this.distanceToSqr(entity) <= 1024.0; // 32 blocks squared
    }

    @Override
    public boolean isNoGravity() {
        return true;
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
}
