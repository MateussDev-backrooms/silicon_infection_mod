package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class FloatingMoveControl extends MoveControl {
        private final BaseSiliconite siliconite;
        private int floatDuration;

        public FloatingMoveControl(BaseSiliconite siliconite) {
            super(siliconite);
            this.siliconite = siliconite;
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.siliconite.getRandom().nextInt(5) + 2;
                    Vec3 delta = new Vec3(this.wantedX - this.siliconite.getX(), this.wantedY - this.siliconite.getY(), this.wantedZ - this.siliconite.getZ());
                    double length = delta.length();
                    delta = delta.normalize();
                    if (this.canReach(delta, Mth.ceil(length))) {
                        this.siliconite.setDeltaMovement(this.siliconite.getDeltaMovement().add(delta.scale(0.05)));
                    } else {
                        this.operation = Operation.WAIT;
                    }
                }

            }
        }

        private boolean canReach(Vec3 pPos, int pLength) {
            AABB box = this.siliconite.getBoundingBox();

            for(int i = 1; i < pLength; ++i) {
                box = box.move(pPos);
                if (!this.siliconite.level().noCollision(this.siliconite, box)) {
                    return false;
                }
            }

            return true;
        }
}
