package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HomingMissileGoal extends Goal {

    private final BaseSiliconite siliconite;
    private LivingEntity target;
    private final float speed;

    public HomingMissileGoal(BaseSiliconite siliconite, float speed) {
        this.siliconite = siliconite;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        target = siliconite.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() && siliconite.getTarget() == target;
    }

    @Override
    public void tick() {
        if (target == null) return;

        // Look at target
        siliconite.getLookControl().setLookAt(target, 30f, 30f);

        // Compute direction vector toward target center
        Vec3 carrierPos = siliconite.position().add(0, siliconite.getBbHeight() / 2f, 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2f, 0);
        Vec3 direction = targetPos.subtract(carrierPos).normalize();

        // Directly set delta movement — this is what makes it feel like a missile
        siliconite.setDeltaMovement(direction.scale(speed));
        siliconite.hasImpulse = true;
    }
}