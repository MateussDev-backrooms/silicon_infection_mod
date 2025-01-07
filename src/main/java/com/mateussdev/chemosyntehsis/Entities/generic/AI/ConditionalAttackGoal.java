package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

import java.util.function.Predicate;

public class ConditionalAttackGoal extends MeleeAttackGoal {

    private final Predicate<Boolean> condition;

    public ConditionalAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen, Predicate<Boolean> condition) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.condition = condition;
    }

    @Override
    public boolean canUse() {
        return condition.test(true) && super.canUse();
    }
}
