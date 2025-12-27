package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseGib;
import com.mateussdev.chemosyntehsis.Entities.hybt1_thrombocyte.HybridThrombocyte;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CollectGibGoal extends Goal {

    private final HybridThrombocyte mob;
    private BaseGib target;



    public CollectGibGoal(HybridThrombocyte mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != null) return false;
        if (mob.isCarryingGib()) return false;

        List<BaseGib> gibs = mob.level().getEntitiesOfClass(
                BaseGib.class,
                mob.getBoundingBox().inflate(12)
        );

        double min_dst = Double.POSITIVE_INFINITY;

        //Linear distance check

        BaseGib best = null;
        if (gibs.isEmpty()) return false;
        for (BaseGib gib : gibs) {
            if(gib.distanceToSqr(mob.position()) < min_dst) {
                min_dst = gib.distanceToSqr(mob.position());
                best = gib;
            }
        }


        target = best;
        return true;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(target, 1.1D);
    }

    @Override
    public void tick() {
        if (target == null || !target.isAlive()) return;

        mob.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (mob.getNavigation().isDone()) {
            mob.getNavigation().moveTo(target, 1.1D);
        }

        if (mob.distanceTo(target) < 1.2D) {
            mob.pickUpGib(target);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.isCarryingGib() && target != null && target.isAlive();
    }
}

