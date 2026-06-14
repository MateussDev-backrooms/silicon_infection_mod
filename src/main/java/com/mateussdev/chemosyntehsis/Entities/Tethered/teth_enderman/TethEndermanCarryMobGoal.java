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
    //TODO: COMPLETE REWRITE THIS CODE IS SHEEEIIIIT
    private final TethEnderman enderman;
    private LivingEntity mainTarget; // The player/enemy we are attacking
    private LivingEntity mobToCarry; // The projectile we are picking up
    private int carryTimer = 0;
    private Vec3 originPosition; // WHERE WE STOOD BEFORE MOVING (Crucial!)

    public TethEndermanCarryMobGoal(TethEnderman enderman) {
        this.enderman = enderman;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Must have a main target (Player)
        if (enderman.getTarget() == null) return false;

        // Only trigger occasionally (e.g., 2% chance)
        if (enderman.getRandom().nextFloat() > 0.02F) return false;

        return findMobToCarry() != null;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue as long as the projectile mob is alive and we haven't finished the attack cycle
        // If you want them to keep walking until they hit the player, you can remove the timer check here.
        return mobToCarry != null && mobToCarry.isAlive();
    }

    @Override
    public void start() {
        this.mainTarget = enderman.getTarget();
        this.mobToCarry = findMobToCarry();
        this.carryTimer = 0;

        // CRITICAL: Save the position we are currently standing at!
        // We will return here after picking up the mob.
        this.originPosition = enderman.position();
    }

    @Override
    public void tick() {
        carryTimer++;

        // 1. TELEPORT TO MOB AND PICK IT UP (Tick 0)
        if (carryTimer == 1) {
            if (mobToCarry != null && mobToCarry.isAlive()) {
                // Teleport to the mob
                enderman.teleport(mobToCarry.position());
                // Mount the mob (The mob rides the Enderman)
                mobToCarry.startRiding(enderman);
            }
        }

        // 2. TELEPORT BACK TO ORIGIN (Tick 20) - Short pause to let mount happen
        else if (carryTimer == 20) {
            if (originPosition != null) {
                enderman.teleport(originPosition);
            }
        }

        // 3. MOVE TOWARDS MAIN TARGET (Tick 40+)
        // We wait a bit after returning before charging
        else if (carryTimer > 40) {
            if (mainTarget != null && mainTarget.isAlive()) {
                // Start navigation towards the player
                // This will carry the "mobToCarry" along with it
                enderman.getNavigation().moveTo(mainTarget, 1.5D);
            }
        }
    }

    @Override
    public void stop() {
        // When we stop (goal interrupted or attack finished), eject the passenger
        enderman.ejectPassengers();
        this.mobToCarry = null;
        this.carryTimer = 0;
    }

    private LivingEntity findMobToCarry() {
        AABB searchBox = enderman.getBoundingBox().inflate(30.0D);
        List<LivingEntity> nearbyMobs = enderman.level().getEntitiesOfClass(LivingEntity.class, searchBox, entity -> {
            // Check filters
            return entity != enderman &&
                    entity != enderman.getTarget() &&
                    (entity instanceof BaseTethered
                            || entity instanceof BaseMetabolized
                            || entity instanceof SiliconRoller) &&
                    !(entity instanceof TethEnderman) &&
                    entity.distanceTo(enderman) > 8d; // Don't pick up stuff right next to us
        });

        return nearbyMobs.isEmpty() ? null : nearbyMobs.get(0);
    }
}