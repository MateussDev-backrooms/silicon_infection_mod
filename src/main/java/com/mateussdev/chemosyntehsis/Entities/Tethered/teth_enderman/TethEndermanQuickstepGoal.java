package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class TethEndermanQuickstepGoal extends Goal {
    private final TethEnderman enderman;
    private LivingEntity target;
    private int chargeTime = 0;
    private int teleportCount = 0;
    private int localTick = 0; // Use a local tick instead of entity tickCount
    private static final int CHARGE_DURATION = 10;
    private static final int MAX_TELEPORTS = 15;

    public TethEndermanQuickstepGoal(TethEnderman enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (enderman.getTarget() == null) return false;
        if(enderman.isPassenger()) return false;
        return enderman.distanceToSqr(enderman.getTarget()) < 256D && enderman.distanceToSqr(enderman.getTarget()) > 36D && enderman.quickstepCooldown <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() &&
                teleportCount < MAX_TELEPORTS;
    }

    @Override
    public void start() {
        this.target = enderman.getTarget();
        this.chargeTime = 0;
        this.teleportCount = 0;
        // Play charge animation
        enderman.triggerAnim("quickstep_controller", "quickstep_begin");
        enderman.playSound(SoundEvents.ENDERMAN_SCREAM);
        enderman.playSound(SoundEvents.IRON_GOLEM_ATTACK);
        enderman.getNavigation().stop();
    }

    @Override
    public void tick() {
        localTick++; // Increment local tick
        if (target == null) return;

        enderman.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (chargeTime < CHARGE_DURATION) {
            chargeTime++;
            return;
        }

        // Execute quickstep teleports
        if (localTick % 1 == 0 && teleportCount < MAX_TELEPORTS) {
            quickstepTeleport();
            teleportCount++;
            if(teleportCount == MAX_TELEPORTS) {
                enderman.teleport(target.position());
                enderman.triggerAnim("quickstep_controller", "quickstep_end");
                enderman.ramIntoTarget(target);
                enderman.quickstepCooldown = 200;
            }
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.chargeTime = 0;
        this.teleportCount = 0;
    }

    private void quickstepTeleport() {
        double angle = enderman.getRandom().nextDouble() * Math.PI * 2;
        double distance = 5 + enderman.getRandom().nextDouble() * 12;
        double x = target.getX() + Math.cos(angle) * distance;
        double z = target.getZ() + Math.sin(angle) * distance;
        double y = target.getY();

        Vec3 teleportPos = new Vec3(x, y, z);
        enderman.teleport(teleportPos);

        // Try to attack after teleport
        if (enderman.distanceToSqr(target) < 4.0D) {
            enderman.doHurtTarget(target);
        }
    }
}
