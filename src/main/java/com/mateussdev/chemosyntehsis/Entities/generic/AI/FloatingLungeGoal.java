package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FloatingLungeGoal extends Goal {
    private final BaseSiliconite siliconite;
    public int chargeTime;

    public FloatingLungeGoal(BaseSiliconite siliconite) {
        this.siliconite = siliconite;
    }

    public boolean canUse() {
        return this.siliconite.getTarget() != null;
    }

    public void start() {
        this.chargeTime = 0;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.siliconite.getTarget();
        if (target != null) {
            if (this.siliconite.hasLineOfSight(target)) {
                Level level = this.siliconite.level();
                ++this.chargeTime;

                if (this.chargeTime == 20) {
                    level.playSound(null, siliconite.blockPosition(), SoundEvents.BAT_TAKEOFF, SoundSource.HOSTILE, 1f, 1f);
                    siliconite.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 10.0f);
                    this.chargeTime = -40;
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }
        }
    }
}
