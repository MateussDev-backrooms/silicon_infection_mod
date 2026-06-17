package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SweepingSwimAttackGoal extends MeleeAttackGoal {
    private final Mob mob;

    // How wide the sweep arc is in blocks
    private static final double SWEEP_RADIUS = 2.5;

    public SweepingSwimAttackGoal(Mob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super((PathfinderMob) mob, speedModifier, followingTargetEvenIfNotSeen);
        this.mob = mob;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distSq) {
        double reachSq = this.getAttackReachSqr(target);

        if (distSq <= reachSq && this.isTimeToAttack()) {
            this.resetAttackCooldown();
            mob.doHurtTarget(target);

            // Sweep: hit everything else in a radius around the primary target
            if (mob.isInWater()) {
                performSweep(target);
            }
        }
    }

    private void performSweep(LivingEntity primaryTarget) {
        AABB sweepBox = mob.getBoundingBox().inflate(SWEEP_RADIUS);
        List<LivingEntity> nearby = mob.level().getEntitiesOfClass(
                LivingEntity.class,
                sweepBox,
                e -> e != mob && e != primaryTarget && mob.hasLineOfSight(e)
        );

        for (LivingEntity victim : nearby) {
            mob.doHurtTarget(victim);
        }
    }

    // Only reduce cooldown when in water — slower on land
    @Override
    protected int getTicksUntilNextAttack() {
        return mob.isInWater() ? 15 : 25;
    }
}
