package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.GlowLichenBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FloatingSiliconiteRandomStrollGoal extends Goal {
    private final BaseSiliconite siliconite;
    private final float horizontal_range;
    private final float vertical_range;
    private int cooldown = 0;

    public FloatingSiliconiteRandomStrollGoal(BaseSiliconite siliconite, float horizontal_range, float vertical_range) {
        this.siliconite = siliconite;
        this.horizontal_range = horizontal_range;
        this.vertical_range = vertical_range;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        // Don't wander when we have a target
        if (siliconite.getTarget() != null) {
            return false;
        }

        // Don't wander when dashing
        if (siliconite instanceof HybridPerfocyte perfocyte && perfocyte.isDashing()) {
            return false;
        }

        return siliconite.getRandom().nextInt(20) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return siliconite.getMoveControl().hasWanted() &&
                siliconite.getTarget() == null;
    }

    @Override
    public void start() {
        RandomSource rng = siliconite.getRandom();
        double targetX = siliconite.getX() + (rng.nextDouble() - 0.5) * horizontal_range;
        double targetY = siliconite.getY() + (rng.nextDouble() - 0.5) * vertical_range;
        double targetZ = siliconite.getZ() + (rng.nextDouble() - 0.5) * horizontal_range;

        // Clamp Y to reasonable bounds
        targetY = Mth.clamp(targetY, siliconite.level().getMinBuildHeight() + 4,
                siliconite.level().getMaxBuildHeight() - 4);

        siliconite.getMoveControl().setWantedPosition(targetX, targetY, targetZ, 0.5);
        cooldown = 100 + rng.nextInt(100); // Wait before next wander
    }

    @Override
    public void stop() {
        siliconite.getMoveControl().setWantedPosition(
                siliconite.getX(), siliconite.getY(), siliconite.getZ(), 0);
    }
}
