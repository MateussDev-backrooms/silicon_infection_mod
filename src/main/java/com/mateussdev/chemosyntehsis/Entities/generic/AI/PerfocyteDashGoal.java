package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

// Improved movement goal
public class PerfocyteDashGoal extends Goal {
    private final HybridPerfocyte perfocyte;
    private int chargeTime = 0;
    private static final int MAX_CHARGE_TIME = 25;

    public PerfocyteDashGoal(HybridPerfocyte perfocyte) {
        this.perfocyte = perfocyte;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = perfocyte.getTarget();
        return target != null &&
                target.isAlive() &&
                perfocyte.distanceToSqr(target) <= 256.0 && // 16 blocks
                perfocyte.getDashCooldown() <= 0;
    }

    @Override
    public void start() {
        this.chargeTime = 0;
        perfocyte.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = perfocyte.getTarget();
        if (target == null || !target.isAlive()) {
            return;
        }

        // Look at target during charge
        perfocyte.getLookControl().setLookAt(target);

        chargeTime++;

        // Visual charge effect
        if (chargeTime % 5 == 0 && perfocyte.level() instanceof ServerLevel slvl) {
            Vec3 pos = perfocyte.position().add(0, perfocyte.getEyeHeight(), 0);
            Vec3 look = perfocyte.getLookAngle();

            // Spawn charge particles
            for (int i = 0; i < 3; i++) {
                Vec3 offset = look.scale(0.5 + i * 0.3)
                        .add((slvl.random.nextDouble() - 0.5) * 0.2,
                                (slvl.random.nextDouble() - 0.5) * 0.2,
                                (slvl.random.nextDouble() - 0.5) * 0.2);

                slvl.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                        pos.x + offset.x, pos.y + offset.y, pos.z + offset.z,
                        1, 0, 0, 0, 0);
            }
        }

        // Dash when fully charged
        if (chargeTime >= MAX_CHARGE_TIME) {
            perfocyte.startDash(target);
            this.chargeTime = 0;
        }
    }

    @Override
    public void stop() {
        this.chargeTime = 0;
    }
}
