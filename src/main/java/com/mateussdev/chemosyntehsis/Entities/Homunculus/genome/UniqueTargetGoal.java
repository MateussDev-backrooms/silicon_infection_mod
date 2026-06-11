package com.mateussdev.chemosyntehsis.Entities.Homunculus.genome;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class UniqueTargetGoal extends NearestAttackableTargetGoal<Mob> {
    private static final int CHECK_RADIUS = 32; // consider carriers within this range
    private final GenomeCarrier thisCarrier;

    public UniqueTargetGoal(GenomeCarrier carrier) {
        super(carrier, Mob.class, 10, true, false, null);
        this.thisCarrier = carrier;
        // Override the predicate to filter out already-targeted mobs
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance())
                .selector(this::isNotAlreadyTargeted);
    }


    private boolean isNotAlreadyTargeted(LivingEntity target) {
        if (!(target instanceof Mob mobTarget)) return false;
        // Check if any other GenomeCarrier (different from this one) currently has this mob as target
        AABB area = thisCarrier.getBoundingBox().inflate(CHECK_RADIUS);
        List<GenomeCarrier> carriers = thisCarrier.level().getEntitiesOfClass(GenomeCarrier.class, area,
                carrier -> carrier != thisCarrier && carrier.getTarget() == mobTarget);
        return carriers.isEmpty() && thisCarrier.shouldTargetForMutation(targetMob);
    }

    @Override
    public boolean canUse() {
        // Refresh the target condition each tick
        this.targetConditions.selector(this::isNotAlreadyTargeted);
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        // Also ensure the target hasn't become taken by another carrier after we started chasing
        if (this.target != null && !isNotAlreadyTargeted(this.target)) {
            this.target = null;
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    protected void findTarget() {
        this.target = this.mob.level().getNearestEntity(
                this.mob.level().getEntitiesOfClass(this.targetType, this.getTargetSearchArea(this.getFollowDistance()),
                        (e) -> true),
                this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }
}