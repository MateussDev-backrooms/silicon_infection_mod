package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class TethEndermanChaseGoal extends Goal {
    private final TethEnderman enderman;
    private LivingEntity target;
    private int teleportCooldown = 0;
    private int afterTeleportStunT = 0;

    public TethEndermanChaseGoal(TethEnderman enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = enderman.getTarget();
        return target != null && target.isAlive() &&
                enderman.distanceToSqr(target) > 4.0D;
    }

    @Override
    public void start() {
        this.target = enderman.getTarget();
        this.teleportCooldown = 0;
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) return;

        //Stun right after teleporting
        if(afterTeleportStunT > 0) {
            afterTeleportStunT--;
            enderman.getNavigation().stop();
        }

        // Look at target
        enderman.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Move towards target
        enderman.getNavigation().moveTo(target, 1.2D);

        // Teleport when close enough
        if (teleportCooldown > 0) {
            teleportCooldown--;
        }

        if (enderman.distanceToSqr(target) < 64.0D && teleportCooldown == 0) {
            teleportBehindTarget(target);
            teleportCooldown = 40; // 2 second cooldown
        }
    }

    private void teleportBehindTarget(LivingEntity target) {
        Vec3 lookVec = target.getLookAngle().scale(-1);
        Vec3 teleportPos = target.position().add(lookVec.x * 4, 0, lookVec.z * 4);
        teleportPos = findSafeTeleportPosition(teleportPos);
        enderman.teleport(teleportPos);
        afterTeleportStunT = 20;
    }

    private Vec3 findSafeTeleportPosition(Vec3 pos) {
        // Simplified - you'll want better logic here
        for (int i = 0; i < 5; i++) {
            if (enderman.level().isEmptyBlock(enderman.blockPosition().atY((int)pos.y - i - 1))) {
                return new Vec3(pos.x, pos.y - i, pos.z);
            }
        }
        return pos;
    }
}
