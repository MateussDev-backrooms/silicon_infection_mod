package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.phys.Vec3;

public class ImprovedFlyingMoveControl extends FlyingMoveControl {
    private final BaseSiliconite siliconite;
    private int floatDuration;

    public ImprovedFlyingMoveControl(BaseSiliconite siliconite1, float speedMultiplier, boolean noGravity) {
        super(siliconite1, 30, true);
        this.siliconite = siliconite1;
    }

    @Override
    public void tick() {
        if(siliconite instanceof HybridPerfocyte perfocyte) {
            // Don't move if dashing
            if (perfocyte.isDashing()) {
                return;
            }
        }

        if (this.operation == Operation.MOVE_TO) {
            Vec3 target = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
            Vec3 direction = target.subtract(siliconite.position()).normalize();

            // Move toward target
            siliconite.setDeltaMovement(siliconite.getDeltaMovement().add(direction.scale(0.05)));

            // Face movement direction
            if (siliconite.getDeltaMovement().lengthSqr() > 0.01) {
                Vec3 motion = siliconite.getDeltaMovement();
                siliconite.setYRot((float)(Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG));
                siliconite.yBodyRot = siliconite.getYRot();
            }

            // Check if close enough to target
            if (siliconite.distanceToSqr(target) < 2.0) {
                this.operation = Operation.WAIT;
            }
        }
    }
}
