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
    public final float attack_distance;

    public FloatingLungeGoal(BaseSiliconite siliconite, float attack_distance) {
        this.siliconite = siliconite;
        this.attack_distance = attack_distance;
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

                if (this.chargeTime >= 20 && siliconite.distanceToSqr(target) <= attack_distance*attack_distance) {
                    level.playSound(null, siliconite.blockPosition(), SoundEvents.BAT_TAKEOFF, SoundSource.HOSTILE, 1f, 1f);
                    Vec3 targetVec = target.getEyePosition().subtract(siliconite.position());
                    Vec3 dashVec = targetVec.normalize().scale(1.3f);

                    siliconite.setDeltaMovement(dashVec);
                    siliconite.hasImpulse = true;
                    this.chargeTime = -40;
                }
            } else if (this.chargeTime > 0) {
                --this.chargeTime;
            }
        }
    }
}
