package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
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

    public FloatingSiliconiteRandomStrollGoal(BaseSiliconite siliconite, float horizontal_range, float vertical_range) {
        this.siliconite = siliconite;
        this.horizontal_range = horizontal_range;
        this.vertical_range = vertical_range;
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
        float targeting_factor = 1.0f;
        if(this.siliconite.getTarget() != null) targeting_factor = 0.4f;
        double $$1 = this.siliconite.getX() + (double)((rng.nextFloat() * 2.0F - 1.0F) * (horizontal_range*targeting_factor));
        double $$2 = this.siliconite.getY() + (double)((rng.nextFloat() * 2.0F - 1.0F) * (vertical_range*targeting_factor));
        double $$3 = this.siliconite.getZ() + (double)((rng.nextFloat() * 2.0F - 1.0F) * (horizontal_range*targeting_factor));
        this.siliconite.getMoveControl().setWantedPosition($$1, $$2, $$3, 1.0);
    }
}
