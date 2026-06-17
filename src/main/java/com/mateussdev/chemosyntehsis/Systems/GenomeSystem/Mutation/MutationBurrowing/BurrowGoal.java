package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class BurrowGoal extends Goal{
    private final Mob mob;
    private final MutationBurrowing mutation;

    // How close a target needs to be before the mob unburrows to attack
    private static final double UNBURROW_RANGE_SQ = 12.0;

    public BurrowGoal(Mob mob, MutationBurrowing mutation) {
        this.mob = mob;
        this.mutation = mutation;
        // Takes over movement and look while active
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        // Only engage when fully surfaced, off cooldown, and no target nearby
        if (!mutation.isFullySurfaced()) return false;
        if (mutation.isBurrowCoolingDown()) return false;

        LivingEntity target = mob.getTarget();
        if (target == null || !target.isAlive()) return true; // No target — burrow

        // Target exists but is far away — still want to burrow and approach underground
        return mob.distanceToSqr(target) > UNBURROW_RANGE_SQ;
    }

    @Override
    public boolean canContinueToUse() {
        // Keep running while burrowing or underground
        if (mutation.burrowState == MutationBurrowing.BurrowState.SURFACE) return false;
        if (mutation.burrowState == MutationBurrowing.BurrowState.UNBURROWING) return false;

        return true;
    }

    @Override
    public void start() {
        mutation.startBurrowing(mob);
    }

    @Override
    public void tick() {
        if (!mutation.isFullyBurrowed()) return;

        LivingEntity target = mob.getTarget();

        if (target != null && target.isAlive()) {
            // Target found — navigate underground toward them at half speed
            mob.getLookControl().setLookAt(target, 30f, 30f);
            mob.getNavigation().moveTo(target, 1);

            // Close enough — unburrow to attack
            if (mob.distanceToSqr(target) <= UNBURROW_RANGE_SQ) {
                mutation.startUnburrowing(mob);
            }
        } else {
            // No target — wander slowly underground
            if (mob.getNavigation().isDone()) {
                double x = mob.getX() + (mob.getRandom().nextDouble() - 0.5) * 16;
                double z = mob.getZ() + (mob.getRandom().nextDouble() - 0.5) * 16;
                mob.getNavigation().moveTo(x, mob.getY(), z, 0.4);
            }
        }
    }

    @Override
    public void stop() {
        // If interrupted while underground, unburrow
        if (mutation.isFullyBurrowed()) {
            mutation.startUnburrowing(mob);
        }
    }
}
