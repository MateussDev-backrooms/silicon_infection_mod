package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class TethEndermanTeleportWhenSeen extends Goal {
        private final TethEnderman enderman;
        @Nullable
        private LivingEntity target;

        public TethEndermanTeleportWhenSeen(TethEnderman pEnderman) {
            this.enderman = pEnderman;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            this.target = this.enderman.getTarget();
            if (!(this.target instanceof Player)) {
                return false;
            } else {
                double d0 = this.target.distanceToSqr(this.enderman);
                return d0 > 256.0 ? false : this.enderman.isLookingAtMe((Player)this.target);
            }
        }

        public void start() {

            this.enderman.getNavigation().stop();
            double angle = enderman.getRandom().nextDouble() * Math.PI * 2;
            double distance = 5 + enderman.getRandom().nextDouble() * 12;
            double x = target.getX() + Math.cos(angle) * distance;
            double z = target.getZ() + Math.sin(angle) * distance;
            double y = target.getY();

            Vec3 teleportPos = new Vec3(x, y, z);
            enderman.teleport(teleportPos);
        }
}
