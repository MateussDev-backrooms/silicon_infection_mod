package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.Bee;
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
        return siliconite.getNavigation().isDone() && siliconite.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return siliconite.getNavigation().isInProgress() &&
                siliconite.getTarget() == null;
    }

    @Override
    public void start() {
        RandomSource rng = siliconite.getRandom();
        Vec3 viewVector = siliconite.getViewVector(0.0F);
        Vec3 target = HoverRandomPos.getPos(siliconite, Mth.floor(horizontal_range), Mth.floor(vertical_range), viewVector.x, viewVector.z, ((float)Math.PI / 2F), 3, 1);
        if(target == null) {
            target = AirAndWaterRandomPos.getPos(siliconite, Mth.floor(horizontal_range), Mth.floor(vertical_range), -2, viewVector.x, viewVector.z, ((float)Math.PI / 2F));
        }

        siliconite.getNavigation().moveTo(siliconite.getNavigation().createPath(BlockPos.containing(target), 1), 0.5f);
    }
}
