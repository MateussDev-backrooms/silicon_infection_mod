package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

import java.util.function.Predicate;

public class ConditionalFleeGoal<T extends PathfinderMob> extends AvoidEntityGoal<T> {

    private final Predicate<Boolean> condition;

    public ConditionalFleeGoal(PathfinderMob pMob, Class<T> pEntityClassToAvoid, float pMaxDistance, double pWalkSpeedModifier, double pSprintSpeedModifier, Predicate<Boolean> condition) {
        super(pMob, pEntityClassToAvoid, pMaxDistance, pWalkSpeedModifier, pSprintSpeedModifier);
        this.condition = condition;
    }


    @Override
    public boolean canUse() {
        return condition.test(true) && super.canUse();
    }
}
