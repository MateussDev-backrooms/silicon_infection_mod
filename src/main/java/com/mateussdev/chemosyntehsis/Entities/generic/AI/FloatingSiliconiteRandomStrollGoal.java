package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FloatingSiliconiteRandomStrollGoal extends Goal {
    private final BaseSiliconite siliconite;

    public FloatingSiliconiteRandomStrollGoal(BaseSiliconite siliconite) {
        this.siliconite = siliconite;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        MoveControl moveControl = this.siliconite.getMoveControl();
        if (!moveControl.hasWanted()) {
            return true;
        } else {
            double dx = moveControl.getWantedX() - this.siliconite.getX();
            double dy = moveControl.getWantedY() - this.siliconite.getY();
            double dz = moveControl.getWantedZ() - this.siliconite.getZ();
            double dst_squared = dx * dx + dy * dy + dz * dz;
            return dst_squared < 1.0 || dst_squared > 3600.0;
        }
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void start() {
        RandomSource rng = this.siliconite.getRandom();
        double $$1 = this.siliconite.getX() + (double)((rng.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double $$2 = this.siliconite.getY() + (double)((rng.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double $$3 = this.siliconite.getZ() + (double)((rng.nextFloat() * 2.0F - 1.0F) * 16.0F);
        this.siliconite.getMoveControl().setWantedPosition($$1, $$2, $$3, 1.0);
    }
}
