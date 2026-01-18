package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

// Improved movement goal
public class PerfocyteDashGoal extends Goal {
    private final HybridPerfocyte perfocyte;
    private int chargeTime = 0;
    private static final int MAX_CHARGE_TIME = 30;
    private static final int COOLDOWN_AFTER_HIT = 20;

    public PerfocyteDashGoal(HybridPerfocyte perfocyte) {
        this.perfocyte = perfocyte;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = perfocyte.getTarget();
        if (target == null || !target.isAlive()) return false;
        if (perfocyte.isDashing()) return false;

        // Check if we should dash (not on cooldown, and target is reasonably close)
        return perfocyte.getDashCooldown() <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return perfocyte.isDashing();
    }

    @Override
    public void start() {
        perfocyte.getNavigation().stop();
        LivingEntity target = perfocyte.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        perfocyte.getLookControl().setLookAt(target);

        perfocyte.startDash(target);
    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return false;
    }
}
