package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class TethEndermanScreamGoal extends Goal {
    private final TethEnderman enderman;
    private LivingEntity target;
    private int screamTimer = 0;
    private static final int SCREAM_DURATION = 60;
    private static final int TELEPORT_INTERVAL = 2;
    private int damageTimer = 0;

    public TethEndermanScreamGoal(TethEnderman enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (enderman.getTarget() == null) return false;
        if (enderman.screamCooldown > 0) return false;
        return enderman.getRandom().nextFloat() < 0.005F; // 0.5% chance per tick
    }

    @Override
    public boolean canContinueToUse() {
        return target != null && target.isAlive() &&
                screamTimer < SCREAM_DURATION;
    }

    @Override
    public void start() {
        this.target = enderman.getTarget();
        this.screamTimer = 0;
        this.damageTimer = 0;

        // Play scream animation and sound
        enderman.triggerAnim("scream_controller", "scream");
        enderman.level().playSound(null, enderman.blockPosition(),
                SoundEvents.ENDERMAN_SCREAM, enderman.getSoundSource(), 2.5F, 1.0F);

        enderman.screamCooldown = 400; // 20 second cooldown
        enderman.getNavigation().stop();
    }

    @Override
    public void tick() {
        screamTimer++;

        if (target == null) return;

        enderman.getLookControl().setLookAt(target, 30.0F, 30.0F);

        // Teleport rapidly
        if (screamTimer % TELEPORT_INTERVAL == 0) {
            screamTeleport();
        }

        // Take damage over time
        damageTimer++;
        if (damageTimer >= 10) {
            enderman.hurt(enderman.damageSources().magic(), 1.0F);
            damageTimer = 0;
        }
    }

    @Override
    public void stop() {
        this.target = null;
        this.screamTimer = 0;
        this.damageTimer = 0;
    }

    private void screamTeleport() {
        double angle = (screamTimer * 0.5) % (Math.PI * 2);
        double distance = 6 + enderman.getRandom().nextDouble() * 6;
        double x = target.getX() + Math.cos(angle) * distance;
        double z = target.getZ() + Math.sin(angle) * distance;
        double y = target.getY();

        Vec3 teleportPos = new Vec3(x, y, z);
        teleportPos = findSafeTeleportPosition(teleportPos);
        enderman.teleport(teleportPos);
    }

    private Vec3 findSafeTeleportPosition(Vec3 pos) {
        // Similar to other goals
        return pos;
    }
}
