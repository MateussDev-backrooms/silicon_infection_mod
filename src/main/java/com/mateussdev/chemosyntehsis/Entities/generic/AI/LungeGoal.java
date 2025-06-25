package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Predicate;

public class LungeGoal extends Goal {

    private final Mob mob;
    private LivingEntity target;
    private final double speed;
    private final double upwardBoost;
    private final double verticalFactor;
    private final Predicate<Integer> condition;
    private double outer_distance;
    private double inner_distance;

    public LungeGoal(Mob mob, double speed, double upwardBoost, double verticalFactor, double outer_distance, double inner_distance, Predicate<Integer> condition) {
        this.mob = mob;
        this.speed = speed;
        this.upwardBoost = upwardBoost;
        this.verticalFactor = verticalFactor;
        this.outer_distance = outer_distance;
        this.inner_distance = inner_distance;
        this.condition = condition;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        target = mob.getTarget();
        return target != null
                && mob.onGround()
                && (mob.distanceToSqr(target) < outer_distance*outer_distance && mob.distanceToSqr(target) >= inner_distance*inner_distance)
                && condition.test(0);
    }

    @Override
    public void start() {
        double dx = target.getX() - mob.getX();
        double dy = target.getY() - mob.getY();
        double dz = target.getZ() - mob.getZ();

        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (dist > 0.0D) {
            dx /= dist;
            dy /= dist;
            dz /= dist;

            mob.setDeltaMovement(dx * speed, dy * verticalFactor + upwardBoost, dz * speed);
            mob.hasImpulse = true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}
