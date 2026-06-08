package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ErythrocyteRetreatGoal extends Goal {
    private final HybridErythrocyte erythrocyte;
    private int retreatTimer = 0;
    private static final int RETREAT_DURATION = 100; // 5 seconds

    public ErythrocyteRetreatGoal(HybridErythrocyte erythrocyte) {
        this.erythrocyte = erythrocyte;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return erythrocyte.getStateManager().getState() == ErythrocyteStateManager.State.RETREATING;
    }

    @Override
    public boolean canContinueToUse() {
        return retreatTimer < RETREAT_DURATION;
    }

    @Override
    public void start() {
        retreatTimer = 0;
        erythrocyte.getNavigation().stop();

        // Move away from the target
        LivingEntity target = erythrocyte.getTarget();
        if (target != null) {
            double awayX = erythrocyte.getX() - target.getX();
            double awayZ = erythrocyte.getZ() - target.getZ();
            double distance = Math.sqrt(awayX * awayX + awayZ * awayZ);

            if (distance > 0) {
                double moveX = erythrocyte.getX() + (awayX / distance) * 10;
                double moveZ = erythrocyte.getZ() + (awayZ / distance) * 10;
                erythrocyte.getNavigation().moveTo(moveX, erythrocyte.getY(), moveZ, 1.2);
            }
        }
    }

    @Override
    public void tick() {
        retreatTimer++;

        if (retreatTimer >= RETREAT_DURATION) {
            erythrocyte.getStateManager().setState(ErythrocyteStateManager.State.IDLE);
            erythrocyte.setTarget(null); // Clear target after retreating
        }
    }

    @Override
    public void stop() {
        retreatTimer = 0;
    }
}
