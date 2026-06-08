package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ErythrocyteDeployMobGoal extends Goal {
    private final HybridErythrocyte erythrocyte;
    private static final double DEPLOY_RANGE = 6.0;

    public ErythrocyteDeployMobGoal(HybridErythrocyte erythrocyte) {
        this.erythrocyte = erythrocyte;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = erythrocyte.getTarget();
        if (target == null || !target.isAlive()) return false;

        return erythrocyte.getFirstPassenger() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return erythrocyte.getFirstPassenger() != null;
    }

    @Override
    public void start() {
        super.start();
        // Move towards attack target

        LivingEntity attackTarget = erythrocyte.getTarget();

        if(attackTarget != null) {
            erythrocyte.getLookControl().setLookAt(attackTarget, 30.0F, 30.0F);
            erythrocyte.getNavigation().moveTo(attackTarget.getX(), attackTarget.getY() + 4f, attackTarget.getZ(), 0.75);
//            erythrocyte.level().addParticle(ParticleTypes.HEART, attackTarget.getX(), attackTarget.getY(), attackTarget.getZ(), 1f, 1f, 1f);
//            StaticSiliconiteMethods.debugLog("Deploying...");


//            // Check if close enough to deploy
//            if (erythrocyte.distanceToSqr(attackTarget) < DEPLOY_RANGE * DEPLOY_RANGE) {
//                erythrocyte.getNavigation().stop();
//                erythrocyte.ejectPassengers();
//            }
        }
    }

    @Override
    public void tick() {
        super.tick();

    }
}
