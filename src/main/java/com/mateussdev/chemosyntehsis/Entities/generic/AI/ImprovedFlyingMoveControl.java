package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;

public class ImprovedFlyingMoveControl extends FlyingMoveControl {
    private final HybridPerfocyte perfocyte;
    private float speedMultiplier;

    public ImprovedFlyingMoveControl(HybridPerfocyte perfocyte, float speedMultiplier, boolean noGravity) {
        super(perfocyte, 20, true);
        this.perfocyte = perfocyte;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void tick() {
        if (perfocyte.isDashing()) {
            // Dashing overrides normal movement
            return;
        }

        if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            double dx = this.wantedX - perfocyte.getX();
            double dy = this.wantedY - perfocyte.getY();
            double dz = this.wantedZ - perfocyte.getZ();
            double distSqr = dx * dx + dy * dy + dz * dz;

            if (distSqr < 2.5000003E-7F) {
                perfocyte.setYya(0.0F);
                perfocyte.setZza(0.0F);
                return;
            }

            float yRot = (float)(Mth.atan2(dz, dx) * Mth.RAD_TO_DEG) - 90.0F;
            perfocyte.setYRot(this.rotlerp(perfocyte.getYRot(), yRot, 90.0F));

            float speed = (float)(this.speedModifier * 0.5d * speedMultiplier);

            if (perfocyte.isInWater()) {
                speed *= 0.02F;
            }

            perfocyte.setSpeed(speed);

            double horizontalDist = Math.sqrt(dx * dx + dz * dz);
            float xRot = (float)(-(Mth.atan2(dy, horizontalDist) * Mth.RAD_TO_DEG));
            perfocyte.setXRot(this.rotlerp(perfocyte.getXRot(), xRot, 5.0F));

            perfocyte.setYya(dy > 0.0 ? speed : -speed);
        } else {
            perfocyte.setYya(0.0F);
            perfocyte.setZza(0.0F);
        }
    }
}
