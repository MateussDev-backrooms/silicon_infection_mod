package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PerfocyteLookAtTargetGoal extends Goal {
    private final HybridPerfocyte perfocyte;

    public PerfocyteLookAtTargetGoal(HybridPerfocyte perfocyte) {
        this.perfocyte = perfocyte;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = perfocyte.getTarget();
        if (target != null && target.isAlive()) {
            perfocyte.getLookControl().setLookAt(target, 30.0F, 30.0F);
        } else {
            // Look in movement direction
            Vec3 motion = perfocyte.getDeltaMovement();
            if (motion.lengthSqr() > 0.01) {
                float yRot = (float)(Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG);
                perfocyte.setYRot(yRot);
                perfocyte.yBodyRot = perfocyte.getYRot();
            }
        }
    }
}
