package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FloatingSiliconiteChaseGoal extends Goal {
    public final BaseSiliconite mob;

    public LivingEntity target;

    public FloatingSiliconiteChaseGoal(BaseSiliconite mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        LivingEntity potentialTarget = mob.getTarget();
        return potentialTarget != null && potentialTarget.isAlive();
    }

    @Override
    public void tick() {
        target = mob.getTarget();
        if (target == null) return;

        Vec3 mobPos = mob.position();
        Vec3 targetPos = target.getEyePosition();
        Vec3 direction = targetPos.subtract(mobPos).normalize().scale(0.1);
        float speed = mob.getSpeed();

        mob.setDeltaMovement(
                direction.x * speed,
                direction.y * 0.5 * speed, // dampen vertical for smoother hover
                direction.z * speed
        );

        if (mob.distanceTo(target) < 1.5f) {
            mob.doHurtTarget(target);
        }
    }
}
