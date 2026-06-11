package com.mateussdev.chemosyntehsis.Entities.Homunculus.genome;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class DeliverToForcedTargetGoal extends Goal {
    private final GenomeCarrier carrier;
    private Mob target;

    public DeliverToForcedTargetGoal(GenomeCarrier carrier) {
        this.carrier = carrier;
    }

    @Override
    public boolean canUse() {
        if (carrier.level().isClientSide) return false;
        target = carrier.getForcedTarget((ServerLevel) carrier.level());
        return target != null && target.isAlive();
    }

    @Override
    public void start() {
        // Start moving directly toward the target
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) return;
        // Move directly toward target
        Vec3 toTarget = target.position().subtract(carrier.position()).normalize();
        carrier.setDeltaMovement(toTarget.scale(0.5));
        carrier.lookAt(target, 30, 30);

        // Check collision (distance < 1.5 blocks)
        if (carrier.distanceToSqr(target) < 2.25) {
            carrier.applyGenome(target);
            carrier.discard();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && !carrier.isRemoved();
    }
}