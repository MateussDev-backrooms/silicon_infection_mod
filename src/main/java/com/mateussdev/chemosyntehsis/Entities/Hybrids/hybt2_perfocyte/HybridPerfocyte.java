package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte;

import com.mateussdev.chemosyntehsis.Entities.generic.AI.*;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.*;

public class HybridPerfocyte extends BaseHybrid {
    public HybridPerfocyte(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new ImprovedFlyingMoveControl(this, 1f, true);
    }

    // Constants
    private static final float INITIAL_DASH_SPEED = 0.95f;
    private static final int DASH_DURATION = 30;
    private static final float BLOCK_DAMAGE = 3f;
    private static final float HORIZONTAL_DAMPING = 0.3f;
    private static final float MAX_ANGER_LEVEL = 10.0f;

    // Synced data
    private boolean isDashing = false;
    private int dashCooldown = 0;
    private int dashDuration = 0;
    private float angerLevel = 0.0f;

    //Destruction logic
    private Map<BlockPos, BlockDamageTracker> trackedDestructionBlocks = new HashMap<>();


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

    public class BlockDamageTracker {
        public float damageProgress;
        public BlockState state;
        public boolean hasDamaged;

        public int t;
        public boolean isGarbage; //Determines if it should be removed from the map

        public BlockDamageTracker(BlockState initialState, float initialDamage) {
            damageProgress = initialDamage;
            state = initialState;
            t = 0;
            hasDamaged = true;
            isGarbage = false;
        }

        public void update() {
            t++;
            if(t>100) {
                if(t%20 == 0 && damageProgress > 0) {
                    damageProgress -= 0.5f;
                    if(damageProgress <= 0) isGarbage = true;
                }
            }
        }

        public void damageBlock(BlockPos pos, ServerLevel slvl, float amount) {
            t=0; //Reset timer
            damageProgress += amount;
            if(damageProgress >= 10f) {
                slvl.destroyBlock(pos, false);
                isGarbage = true;
            }
        }
    }

    // ===== AI n pathfinding ===== //

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new PerfocyteDashGoal(this));
        this.goalSelector.addGoal(1, new PerfocyteLookAtTargetGoal(this));
        this.goalSelector.addGoal(2, new FloatingSiliconiteRandomStrollGoal(this, 18f, 8f));

        // Seek out targets (no line of sight requirement)
        this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this, Player.class, 0, false, false, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 0, false, false,
                StaticSiliconiteMethods::shouldAttackMob));
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

    private List<BlockPos> cleanerJobs = new ArrayList<>();
    private void addCleanerJob(BlockPos toClean) {
        if(!cleanerJobs.contains(toClean)) {
            cleanerJobs.add(toClean);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            if(dashCooldown > 0) dashCooldown--;

            //Looking
            if(getTarget() != null) {
                lookAt(getTarget(), 30, 30);
            }

//            double $$0 = this.position().add(getDeltaMovement()).x - this.getX();
//            double $$1 = this.position().add(getDeltaMovement()).y - this.getY();
//            double $$2 = this.position().add(getDeltaMovement()).z - this.getZ();
//            double $$3 = Math.sqrt($$0 * $$0 + $$2 * $$2);
//            float $$10 = (float)(-(Mth.atan2(-$$1, $$3) * 57.2957763671875));
//            this.setXRot($$10);

            //Block destruction tracking
            updateTrackedBlocks();

            //Dash logic
            if(isDashing) {
                dashDuration++;

                if(tickCount % 2 == 0) {
                    blockDestructionCheck(slvl);
                    areaDamageEntities(slvl);
                }

                // Apply air resistance
                Vec3 currentMotion = this.getDeltaMovement();
                this.setDeltaMovement(currentMotion.scale(0.98));

                //End dash due to inactivity
                if(dashDuration > DASH_DURATION) {
                    endDash();
                }
            } else {
                if(angerLevel > 0) {
                    angerLevel -= 0.002f;
                }
            }

            //Anger effect
            if(angerLevel > MAX_ANGER_LEVEL/2f) {
                if(tickCount % 40 == 0) {
                    slvl.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            this.position().x,
                            this.position().y,
                            this.position().z,
                            15,
                            this.getBoundingBox().getXsize()/2,
                            this.getBoundingBox().getYsize()/2,
                            this.getBoundingBox().getZsize()/2,
                            0.3f
                    );
                }
            }
        }
    }

    private void updateTrackedBlocks() {
        // Only update every 5 ticks
        if (tickCount % 5 != 0) return;

        Iterator<Map.Entry<BlockPos, BlockDamageTracker>> iterator =
                trackedDestructionBlocks.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<BlockPos, BlockDamageTracker> entry = iterator.next();
            BlockDamageTracker tracker = entry.getValue();

            tracker.update();
//            StaticSiliconiteMethods.debugLog(trackedDestructionBlocks.size()+" - tracked destruction of blocks");

//            if (tracker.isGarbage) {
//                iterator.remove(); // Remove directly
//                // Clean up any remaining particles or effects
//            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(level() instanceof ServerLevel slvl) {
            //Float upward when on ground
            BlockHitResult raycast = slvl.clip(new ClipContext(this.position(), this.position().add(getDeltaMovement()).scale(10f), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
            if(raycast.getBlockPos().getCenter().distanceTo(this.position()) <= 0.6f) {
                setDeltaMovement(getDeltaMovement().add(0, 0.05, 0));
            }
        }
    }

    public void startDash(LivingEntity target) {
        if(isDashing || dashCooldown > 0) return;

        isDashing = true;

        Vec3 dashDelta = target.getEyePosition().subtract(this.position());
        Vec3 dashVelocity = dashDelta.normalize().scale(INITIAL_DASH_SPEED);

        setDeltaMovement(dashVelocity);
//        this.getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("dash_controller").stop();
        triggerAnim("dash_controller", "dash");

        playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 0.3f);
        playSound(SoundEvents.BAT_TAKEOFF, 0.8f, 0.5f);

        dashCooldown = Math.max(10, 50 - (int)(angerLevel * 5));
    }

    public void endDash() {
        isDashing = false;
        dashDuration = 0;
        hasCollided = false;
    }

    boolean hasCollided = false;
    Vec3 collisionNormal = Vec3.ZERO;

    public void blockDestructionCheck(ServerLevel slvl) {
        AABB destructionHitbox = this.getBoundingBox().inflate(0.2);

        //Single hit logic
//        if(!slvl.getBlockStatesIfLoaded(destructionHitbox).toList().isEmpty())
        int amount_checked = 0;
        for(BlockPos pos : BlockPos.betweenClosed(
                BlockPos.containing(destructionHitbox.minX - 0.5, destructionHitbox.minY - 0.5, destructionHitbox.minZ - 0.5),
                BlockPos.containing(destructionHitbox.maxX + 0.5, destructionHitbox.maxY + 0.5, destructionHitbox.maxZ + 0.5)
        )) {
            amount_checked++;
            //TEMP - destroy every block in the hitbox
            BlockState state = slvl.getBlockState(pos);

            if(state.isAir() || state.getDestroySpeed(slvl, pos) < 0) {
                continue;
            }

            if(state.getDestroySpeed(slvl, pos) < 0.6) {
                slvl.destroyBlock(pos, false);
                continue;
            }

            //Destruction logic
            damageBlock(pos.immutable(), state, slvl);

            slvl.playSound(null, pos,
                    SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR,
                    SoundSource.HOSTILE, 1.5f, 0.8f + random.nextFloat() * 0.3f);

            hasCollided = true;

        }

//        StaticSiliconiteMethods.debugLog(amount_checked+" - amount of blocks iterated to see for collisions");
        //Calculate normal vector
        BlockHitResult raycast = slvl.clip(new ClipContext(this.position(), this.position().add(getDeltaMovement()).scale(10f), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

        Vec3i normal = raycast.getDirection().getNormal();
        collisionNormal = new Vec3(normal.getX(), normal.getY(), normal.getZ());

        if(hasCollided) {
            if(raycast.getType() == HitResult.Type.MISS) collisionNormal = new Vec3(0, 1, 0);
            //Bounce the perfocyte away
            bounceAway(collisionNormal);
            increaseAnger(0.8f);
            endDash();
        }
    }

    private void bounceAway(Vec3 normal) {
        Vec3 currentDelta = getDeltaMovement();
        Vec3 launchVector = currentDelta.subtract(normal.scale(2 * currentDelta.dot(normal)));
        Vec3 launchVelocity = launchVector.normalize().scale(INITIAL_DASH_SPEED*HORIZONTAL_DAMPING);

        setDeltaMovement(launchVelocity);
    }

    private void damageBlock(BlockPos pos, BlockState state, ServerLevel slvl) {

        //Figure out how much damage to give to the block
        float damage = BLOCK_DAMAGE / state.getDestroySpeed(slvl, pos);

        //Check if we already interacted with the block
        if(trackedDestructionBlocks.containsKey(pos)) {
            //CHANGE DESTRUCTION DATA HERE
            BlockDamageTracker trackedBlock = trackedDestructionBlocks.get(pos);
            trackedBlock.damageBlock(pos, slvl, damage);
        } else {
            //Add block to be tracked and damage it
            BlockDamageTracker newTracker = new BlockDamageTracker(state, 0f);
            trackedDestructionBlocks.put(pos, newTracker);
            newTracker.damageBlock(pos, slvl, damage);
        }

        //Particle effect
        BlockParticleOption blockParticleOption = new BlockParticleOption(ParticleTypes.BLOCK, state);
        slvl.sendParticles(blockParticleOption,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                10, 0.5, 0.5, 0.5, 0.2f);
    }

    private void areaDamageEntities(ServerLevel slvl) {
        AABB entityAreaHitbox = this.getBoundingBox().inflate(0.3);
        for (LivingEntity entity : slvl.getEntitiesOfClass(
                LivingEntity.class, entityAreaHitbox, e -> e != this && e.isAlive())) {

            if (StaticSiliconiteMethods.shouldAttackMob(entity)) {
                float impactForce = (float) this.getDeltaMovement().length();

                Vec3 knockback = this.getDeltaMovement().normalize().scale(impactForce * 0.5);

                if(entity instanceof Player player) {
                    if(player.isBlocking()) {
                        increaseAnger(0.8f);
                        if (!player.getUseItem().isEmpty()) {
                            player.getUseItem().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                        }

                        this.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                net.minecraft.sounds.SoundEvents.SHIELD_BREAK,
                                net.minecraft.sounds.SoundSource.PLAYERS,
                                1.0F, 1.0F);
                        this.setDeltaMovement(player.getLookAngle().scale(INITIAL_DASH_SPEED*HORIZONTAL_DAMPING));
                        player.setDeltaMovement(entity.getDeltaMovement().add(knockback.scale(0.3f)));
                        endDash();
                        continue;
                    }
                }
                entity.setDeltaMovement(entity.getDeltaMovement().add(knockback));
                entity.hurtMarked = true;

                boolean didDamage = doHurtTarget(entity);

                if (didDamage) {
                    angerLevel = 0.0f;
                    playSound(SoundEvents.PLAYER_ATTACK_STRONG, 1.0f, 0.8f);
                } else {
                    //Dodged - get angrier
                    increaseAnger(0.8f);
                }
                break;
            }
        }
    }

    private void increaseAnger(float amount) {
        angerLevel = Math.min(MAX_ANGER_LEVEL, angerLevel + amount);
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
        tag.putBoolean("is_dashing", this.isDashing);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.angerLevel = tag.getFloat("anger_level");
        this.dashCooldown = tag.getInt("dash_cooldown");
        this.isDashing = tag.getBoolean("is_dashing");
    }

    // ===== GETTERS ===== //

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
        return this.distanceToSqr(entity) <= 1024.0; // 32 blocks squared
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new PerfocyteBodyRotationControl(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
        controllers.add(new AnimationController<>(this, "dash_controller", 5, event -> PlayState.STOP).triggerableAnim("dash", RawAnimation.begin().thenPlay("dash")).setAnimationSpeed(1.7f));
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    class PerfocyteBodyRotationControl extends BodyRotationControl {
        public PerfocyteBodyRotationControl(Mob pMob) {
            super(pMob);
        }

        public void clientTick() {
            HybridPerfocyte.this.yHeadRot = HybridPerfocyte.this.yBodyRot;
            HybridPerfocyte.this.yBodyRot = HybridPerfocyte.this.getYRot();
        }
    }


}
