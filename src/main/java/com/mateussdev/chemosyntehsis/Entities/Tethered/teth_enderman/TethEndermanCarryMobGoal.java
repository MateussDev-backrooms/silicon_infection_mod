package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseMetabolized;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class TethEndermanCarryMobGoal extends Goal {
    private final TethEnderman enderman;
    private LivingEntity target;
    private LivingEntity mobToCarry;
    private int carryTimer = 0;

    public TethEndermanCarryMobGoal(TethEnderman enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (enderman.getTarget() == null) return false;
        if (enderman.getRandom().nextFloat() > 0.3F) return false; // 2% chance per tick when canUse is called

        return findMobToCarry() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return mobToCarry != null && mobToCarry.isAlive();
    }

    @Override
    public void start() {
        this.target = enderman.getTarget();
        this.mobToCarry = findMobToCarry();
        this.carryTimer = 0;
    }

    @Override
    public void tick() {

        if (mobToCarry == null || !mobToCarry.isAlive()) return;

        Vec3 currentPos = null;

        if(carryTimer == 0) {
            //Find and teleport to mob
            currentPos = enderman.position();
            enderman.teleport(mobToCarry.position());
            if(enderman.distanceTo(mobToCarry) < 2d) {
                mobToCarry.startRiding(enderman);
            }
        }
        else if(carryTimer == 20) {
            enderman.teleport(currentPos);
        }
        else if(carryTimer == 40) {
            enderman.teleport(findTeleportPositionNearTarget(target));
            enderman.ejectPassengers();
        }
        carryTimer++;
    }

    @Override
    public void stop() {
        this.target = null;
        this.mobToCarry = null;
        this.carryTimer = 0;
        enderman.ejectPassengers();
    }

    private LivingEntity findMobToCarry() {
        AABB searchBox = enderman.getBoundingBox().inflate(30.0D);
        List<LivingEntity> nearbyMobs = enderman.level().getEntitiesOfClass(LivingEntity.class, searchBox, entity -> {
            // Check if it's a mob we can carry
            return entity != enderman && entity != enderman.getTarget() && (entity instanceof BaseTethered || entity instanceof BaseMetabolized || entity instanceof SiliconRoller) && entity.distanceTo(enderman) > 8d;
        });

        return nearbyMobs.isEmpty() ? null : nearbyMobs.get(0);
    }

    private Vec3 findTeleportPositionNearTarget(LivingEntity target) {
        double angle = enderman.getRandom().nextDouble() * Math.PI * 2;
        double distance = 4 + enderman.getRandom().nextDouble() * 4;
        double x = target.getX() + Math.cos(angle) * distance;
        double z = target.getZ() + Math.sin(angle) * distance;
        double y = target.getY();

        return new Vec3(x, y, z);
    }
}