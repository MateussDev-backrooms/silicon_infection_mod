package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.phys.Vec3;

public class ImprovedFlyingMoveControl extends FlyingMoveControl {
    private final HybridPerfocyte perfocyte;
    private int floatDuration;

    public ImprovedFlyingMoveControl(HybridPerfocyte perfocyte, float speedMultiplier, boolean noGravity) {
        super(perfocyte, 30, true);
        this.perfocyte = perfocyte;
    }

    @Override
    public void tick() {
        // Don't move if dashing
        if (perfocyte.isDashing()) {
            return;
        }

        if (this.operation == Operation.MOVE_TO) {
            Vec3 target = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
            Vec3 direction = target.subtract(perfocyte.position()).normalize();

            // Move toward target
            perfocyte.setDeltaMovement(perfocyte.getDeltaMovement().add(direction.scale(0.05)));

            // Face movement direction
            if (perfocyte.getDeltaMovement().lengthSqr() > 0.01) {
                Vec3 motion = perfocyte.getDeltaMovement();
                perfocyte.setYRot((float)(Mth.atan2(motion.x, motion.z) * Mth.RAD_TO_DEG));
                perfocyte.yBodyRot = perfocyte.getYRot();
            }

            // Check if close enough to target
            if (perfocyte.distanceToSqr(target) < 2.0) {
                this.operation = Operation.WAIT;
            }
        }
    }
}
