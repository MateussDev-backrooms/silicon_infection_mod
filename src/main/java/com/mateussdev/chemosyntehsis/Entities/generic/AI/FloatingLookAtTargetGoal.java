package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FloatingLookAtTargetGoal extends Goal{
    private final BaseSiliconite siliconite;

    public FloatingLookAtTargetGoal(BaseSiliconite siliconite) {
        this.siliconite = siliconite;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean canUse() {
        return true;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        if (this.siliconite.getTarget() == null) {
            Vec3 deltaMovement = this.siliconite.getDeltaMovement();
            this.siliconite.setYRot(-((float) Mth.atan2(deltaMovement.x, deltaMovement.z)) * 57.295776F);
            this.siliconite.yBodyRot = this.siliconite.getYRot();
        } else {
            LivingEntity target = this.siliconite.getTarget();
            double dst = 32.0;
            if (target.distanceToSqr(this.siliconite) < dst*dst) {
                double dx = target.getX() - this.siliconite.getX();
                double dz = target.getZ() - this.siliconite.getZ();
                this.siliconite.setYRot(-((float)Mth.atan2(dx, dz)) * 57.295776F);
                this.siliconite.yBodyRot = this.siliconite.getYRot();
            }
        }

    }
}
