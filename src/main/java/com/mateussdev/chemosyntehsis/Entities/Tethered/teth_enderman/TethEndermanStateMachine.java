package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseMetabolized;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TethEndermanStateMachine {
    private final TethEnderman enderman;
    private TethEnderman.TethEndermanState currentState = TethEnderman.TethEndermanState.CHASE;
    private int stateTimer = 0;
    private LivingEntity carriedEntity = null;
    private int screamCooldown = 0;
    private int quickstepTeleportCount = 0;

    // State parameters
    private static final int QUICKSTEP_CHARGE_TIME = 20; // 1 second charge
    private static final int QUICKSTEP_TELEPORT_COUNT = 15;
    private static final int SCREAM_DURATION = 60; // 3 seconds
    private static final int SCREAM_COOLDOWN = 400; // 20 seconds
    private static final int SCREAM_TELEPORT_INTERVAL = 1;

    public TethEndermanStateMachine(TethEnderman enderman) {
        this.enderman = enderman;
    }

    public void tick() {
        stateTimer++;

        // Handle cooldowns
        if (screamCooldown > 0) {
            screamCooldown--;
        }

        // Check for state transitions
        checkStateTransitions();

        // Execute current state behavior
        switch (currentState) {
            case CHASE:
                executeChase();
                break;
            case QUICKSTEP:
                executeQuickstep();
                break;
            case CARRYING_MOB:
                executeCarryingMob();
                break;
            case SCREAM:
                executeScream();
                break;
            case STUNNED:
                executeStunned();
                break;
        }
    }

    private void checkStateTransitions() {
        LivingEntity target = enderman.getTarget();

        // Can't change state while stunned
        if (currentState == TethEnderman.TethEndermanState.STUNNED) {
            if (stateTimer > 40) { // 2 second stun
                changeState(TethEnderman.TethEndermanState.CHASE);
            }
            return;
        }

        // Check health for carrying mob state
        if (currentState != TethEnderman.TethEndermanState.CARRYING_MOB &&
                currentState != TethEnderman.TethEndermanState.SCREAM &&
                enderman.getHealth() < enderman.getMaxHealth() * 0.5f &&
                target != null) {

            // Find nearby mobs to carry
            List<LivingEntity> nearbyMobs = findMobsToCarry();
            if (!nearbyMobs.isEmpty() && enderman.getRandom().nextFloat() < 0.02f) {
                carriedEntity = nearbyMobs.get(0);
                changeState(TethEnderman.TethEndermanState.CARRYING_MOB);
                return;
            }
        }

        // Randomly enter quickstep when chasing
        if (currentState == TethEnderman.TethEndermanState.CHASE &&
                target != null &&
                enderman.distanceTo(target) < 8f &&
                enderman.getRandom().nextFloat() < 0.01f) {
            changeState(TethEnderman.TethEndermanState.QUICKSTEP);
            return;
        }

        // Random scream cooldown
        if (screamCooldown == 0 &&
                currentState != TethEnderman.TethEndermanState.SCREAM &&
                enderman.getRandom().nextFloat() < 0.005f) {
            changeState(TethEnderman.TethEndermanState.SCREAM);
            return;
        }

        // Return to chase from other states if conditions met
        if (currentState == TethEnderman.TethEndermanState.QUICKSTEP && quickstepTeleportCount >= QUICKSTEP_TELEPORT_COUNT) {
            changeState(TethEnderman.TethEndermanState.CHASE);
        } else if (currentState == TethEnderman.TethEndermanState.SCREAM && stateTimer > SCREAM_DURATION) {
            changeState(TethEnderman.TethEndermanState.CHASE);
        } else if (currentState == TethEnderman.TethEndermanState.CARRYING_MOB && carriedEntity == null) {
            changeState(TethEnderman.TethEndermanState.CHASE);
        }
    }

    private void executeChase() {
        LivingEntity target = enderman.getTarget();
        if (target == null) return;

        // Normal chasing
        enderman.getNavigation().moveTo(target, 1.2f);

        // Teleport when close enough
        if (enderman.distanceTo(target) < 8f && enderman.getRandom().nextFloat() < 0.1f) {
            teleportBehindTarget(target);
        }

        // Attack when in range
        if (enderman.distanceTo(target) < 2f) {
            enderman.doHurtTarget(target);
        }
    }

    private void executeQuickstep() {
        LivingEntity target = enderman.getTarget();
        if (target == null) {
            changeState(TethEnderman.TethEndermanState.CHASE);
            return;
        }

        if (stateTimer == 1) {
            // Start charging animation
            enderman.triggerAnim("quickstep_controller", "quickstep_begin");
        } else if (stateTimer > QUICKSTEP_CHARGE_TIME) {
            // Execute rapid teleports
            if ((stateTimer - QUICKSTEP_CHARGE_TIME) % 2 == 0 &&
                    quickstepTeleportCount < QUICKSTEP_TELEPORT_COUNT) {

                quickstepTeleport(target);
                quickstepTeleportCount++;
            }
        }
    }

    private void executeCarryingMob() {
        LivingEntity target = enderman.getTarget();

        if (stateTimer == 1) {
            // Pick up the mob
            if (carriedEntity != null) {
                carriedEntity.startRiding(enderman);
                // Make it invisible or set a flag
            }
        } else if (stateTimer == 20) {
            // Teleport near player
            if (target != null) {
                Vec3 teleportPos = findTeleportPositionNearTarget(target);
                enderman.teleport(teleportPos);
            }
        } else if (stateTimer > 40) {
            // Drop the mob
            if (carriedEntity != null && carriedEntity.isPassenger()) {
                carriedEntity.stopRiding();
                // Reset any flags
            }
            carriedEntity = null;
        }
    }

    private void executeScream() {
        LivingEntity target = enderman.getTarget();
        if (target == null) {
            changeState(TethEnderman.TethEndermanState.CHASE);
            return;
        }

        if (stateTimer == 1) {
            // Play scream animation and sound
            enderman.triggerAnim("scream_controller", "scream");
            enderman.level().playSound(null, enderman.blockPosition(),
                    SoundEvents.ENDERMAN_SCREAM, enderman.getSoundSource(), 2.5f, 1.0f);
            screamCooldown = SCREAM_COOLDOWN;
        }

        // Rapid teleport and take damage
        if (stateTimer % SCREAM_TELEPORT_INTERVAL == 0) {
            screamTeleport(target);
            // Take damage during scream
            enderman.hurt(enderman.damageSources().magic(), 1f);
        }
    }

    private void executeStunned() {
        // Stop movement
        enderman.getNavigation().stop();
    }

    private void changeState(TethEnderman.TethEndermanState newState) {
        this.currentState = newState;
        this.stateTimer = 0;
        this.quickstepTeleportCount = 0;

        // Exit logic for current state
        switch (currentState) {
            case CARRYING_MOB:
                if (carriedEntity != null && carriedEntity.isPassenger()) {
                    carriedEntity.stopRiding();
                }
                carriedEntity = null;
                break;
        }
    }

    // Helper methods
    private void teleportBehindTarget(LivingEntity target) {
        Vec3 lookVec = target.getLookAngle().scale(-1);
        Vec3 teleportPos = target.position().add(lookVec.x * 2, 0, lookVec.z * 2);

        // Find safe position
        teleportPos = findSafeTeleportPosition(teleportPos);
        enderman.teleport(teleportPos);
    }

    private void quickstepTeleport(LivingEntity target) {
        // Teleport randomly around target
        double angle = enderman.getRandom().nextDouble() * Math.PI * 2;
        double distance = 5 + enderman.getRandom().nextDouble() * 4;
        double x = target.getX() + Math.cos(angle) * distance;
        double z = target.getZ() + Math.sin(angle) * distance;
        double y = findGroundLevel(x, target.getY(), z);

        Vec3 teleportPos = new Vec3(x, y, z);
        teleportPos = findSafeTeleportPosition(teleportPos);
        enderman.teleport(teleportPos);
    }

    private void screamTeleport(LivingEntity target) {
        // Teleport in a circle around target
        double angle = (stateTimer * 0.5) % (Math.PI * 2);
        double distance = 4 + enderman.getRandom().nextDouble() * 3;
        double x = target.getX() + Math.cos(angle) * distance;
        double z = target.getZ() + Math.sin(angle) * distance;
        double y = findGroundLevel(x, target.getY(), z);

        Vec3 teleportPos = new Vec3(x, y, z);
        teleportPos = findSafeTeleportPosition(teleportPos);
        enderman.teleport(teleportPos);
    }

    private List<LivingEntity> findMobsToCarry() {
        AABB searchBox = enderman.getBoundingBox().inflate(10);
        return enderman.level().getEntitiesOfClass(LivingEntity.class, searchBox,
                entity -> {
                    // Check if entity is BaseTethered or BaseMetabolized
                    // You'll need to adjust this based on your actual class hierarchy
                    return entity != enderman &&
                            entity != enderman.getTarget() &&
                            (entity instanceof BaseTethered || entity instanceof BaseMetabolized);
                });
    }

    private Vec3 findTeleportPositionNearTarget(LivingEntity target) {
        // Find position within 8 blocks of target
        double angle = enderman.getRandom().nextDouble() * Math.PI * 2;
        double distance = 4 + enderman.getRandom().nextDouble() * 4;
        double x = target.getX() + Math.cos(angle) * distance;
        double z = target.getZ() + Math.sin(angle) * distance;
        double y = findGroundLevel(x, target.getY(), z);

        return new Vec3(x, y, z);
    }

    private Vec3 findSafeTeleportPosition(Vec3 pos) {
        // Simple ground finding - you might want to improve this
        if (enderman.level().isEmptyBlock(enderman.blockPosition().atY((int)pos.y))) {
            for (int i = 0; i < 5; i++) {
                if (!enderman.level().isEmptyBlock(enderman.blockPosition().atY((int)pos.y - i - 1))) {
                    return new Vec3(pos.x, pos.y - i, pos.z);
                }
            }
        }
        return pos;
    }

    private double findGroundLevel(double x, double y, double z) {
        // Find a suitable Y position
        for (int i = 0; i < 10; i++) {
            if (!enderman.level().isEmptyBlock(enderman.blockPosition().atY((int)y - i))) {
                return y - i + 1;
            }
        }
        return y;
    }

    // Getters and setters
    public TethEnderman.TethEndermanState getCurrentState() {
        return currentState;
    }

    public void setStunned() {
        changeState(TethEnderman.TethEndermanState.STUNNED);
    }
}
