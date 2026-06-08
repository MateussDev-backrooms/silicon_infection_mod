package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class PerfocyteTunnelGoal extends Goal {
    private final HybridPerfocyte perfocyte;
    private BlockPos targetTunnelPos = null;
    private int tunnelTime = 0;

    public PerfocyteTunnelGoal(HybridPerfocyte perfocyte) {
        this.perfocyte = perfocyte;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return perfocyte.getTarget() == null &&
                perfocyte.tickCount % 40 == 0 && // Check occasionally
                perfocyte.getRandom().nextFloat() < 0.3f; // 30% chance
    }

    @Override
    public void start() {
        // Find a direction to tunnel
        Vec3 randomDir = new Vec3(
                perfocyte.getRandom().nextDouble() - 0.5,
                perfocyte.getRandom().nextDouble() * 0.3 - 0.1, // Slightly upward bias
                perfocyte.getRandom().nextDouble() - 0.5
        ).normalize();

        // Look for solid block in that direction
        for (int i = 3; i < 8; i++) {
            BlockPos checkPos = perfocyte.blockPosition().offset(
                    (int)(randomDir.x * i),
                    (int)(randomDir.y * i),
                    (int)(randomDir.z * i)
            );

            if (!perfocyte.level().isEmptyBlock(checkPos) &&
                    perfocyte.level().getBlockState(checkPos).getDestroySpeed(
                            perfocyte.level(), checkPos) >= 0) {
                targetTunnelPos = checkPos;
                break;
            }
        }

        tunnelTime = 0;
    }

    @Override
    public void tick() {
        if (targetTunnelPos == null) {
            stop();
            return;
        }

        tunnelTime++;

        // Move toward the block
        perfocyte.getLookControl().setLookAt(
                targetTunnelPos.getX() + 0.5,
                targetTunnelPos.getY() + 0.5,
                targetTunnelPos.getZ() + 0.5,
                10.0F, 40.0F);

        // Make a small dash toward the block
        if (tunnelTime == 10 && perfocyte.getDashCooldown() <= 0) {
            Vec3 toPos = Vec3.atCenterOf(targetTunnelPos).subtract(perfocyte.position());
            perfocyte.setDeltaMovement(toPos.normalize().scale(0.8));
            perfocyte.hasImpulse = true;

            // Damage the block
            if (perfocyte.level() instanceof ServerLevel slvl) {
                slvl.destroyBlockProgress(perfocyte.getId(), targetTunnelPos, 5);
                slvl.playSound(null, targetTunnelPos,
                        SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR,
                        SoundSource.HOSTILE, 0.8f, 0.9f);
            }
        }

        if (tunnelTime > 30) {
            stop();
        }
    }

    @Override
    public void stop() {
        targetTunnelPos = null;
        tunnelTime = 0;
    }
}