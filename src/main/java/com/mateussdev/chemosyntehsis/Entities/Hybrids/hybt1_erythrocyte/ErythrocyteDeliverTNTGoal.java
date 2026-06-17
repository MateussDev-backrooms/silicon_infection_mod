package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class ErythrocyteDeliverTNTGoal extends Goal {

    private final HybridErythrocyte erythrocyte;

    private PrimedTnt targetTnt = null;
    private Player tntOwner   = null;

    // How close we need to get to the TNT to pick it up
    private static final double PICKUP_REACH_SQ = 2.25; // 1.5 blocks
    // How close we deliver to the player
    private static final double DELIVER_REACH_SQ = 9.0; // 3 blocks
    // Search radius for TNT
    private static final double SEARCH_RADIUS = 32.0;

    // Small cooldown so canUse doesn't scan every tick
    private int searchCooldown = 0;

    public ErythrocyteDeliverTNTGoal(HybridErythrocyte erythrocyte) {
        this.erythrocyte = erythrocyte;
        // Highest flag priority — interrupts move and look from other goals
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    // ===== Goal lifecycle ===== //

    @Override
    public boolean canUse() {
        // Already carrying something — don't interfere
        if (erythrocyte.getFirstPassenger() != null) return false;

        // Throttle the scan
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }
        searchCooldown = 10;

        PrimedTnt found = findNearbyTnt();
        if (found == null) return false;

        targetTnt  = found;
        tntOwner   = getTntOwner(found);
        return tntOwner != null && !targetTnt.isPassenger();
    }

    @Override
    public boolean canContinueToUse() {
        // Stop if TNT is gone (exploded or picked up)
        if (targetTnt == null || !targetTnt.isAlive()) return false;

        // If we're carrying the TNT, continue until we reach the player
        if (erythrocyte.getFirstPassenger() == targetTnt) {
            return tntOwner != null && tntOwner.isAlive();
        }

        return true;
    }

    @Override
    public void start() {
        // Immediately drop whatever we were carrying — TNT delivery is top priority
        erythrocyte.ejectPassengers();
        erythrocyte.getStateManager().setState(ErythrocyteStateManager.State.PICKING_UP);
        erythrocyte.setTarget(null);
    }

    @Override
    public void tick() {
        if (targetTnt == null || !targetTnt.isAlive()) return;

        boolean isCarryingTnt = erythrocyte.getFirstPassenger() == targetTnt;

        if (!isCarryingTnt) {
            tickApproachTnt();
        } else {
            tickDeliverTnt();
        }
    }

    @Override
    public void stop() {
        // If we still have the TNT when interrupted, drop it safely
        if (erythrocyte.getFirstPassenger() == targetTnt) {
            erythrocyte.ejectPassengers();
        }
        targetTnt    = null;
        tntOwner     = null;
        searchCooldown = 20; // Pause before searching again
    }

    // ===== Tick phases ===== //

    private void tickApproachTnt() {
        erythrocyte.getLookControl().setLookAt(targetTnt, 30f, 30f);
        erythrocyte.getNavigation().moveTo(targetTnt, 1.2);

        if (erythrocyte.distanceToSqr(targetTnt) < PICKUP_REACH_SQ) {
            erythrocyte.getNavigation().stop();
            targetTnt.startRiding(erythrocyte);
        }
    }

    private void tickDeliverTnt() {
        if (tntOwner == null || !tntOwner.isAlive()) {
            // No known owner — just fly away and drop it somewhere safe
            erythrocyte.ejectPassengers();
            return;
        }

        // Fly toward the player, slightly above their head so the TNT
        // lands on top of them rather than inside them
        erythrocyte.getLookControl().setLookAt(tntOwner, 30f, 30f);
        erythrocyte.getNavigation().moveTo(
                tntOwner.getX(),
                tntOwner.getY() + 3.0,
                tntOwner.getZ(),
                1.3
        );

//        if (erythrocyte.distanceToSqr(tntOwner) < DELIVER_REACH_SQ) {
//            erythrocyte.getNavigation().stop();
//            erythrocyte.ejectPassengers();
//            // Goal will end naturally via canContinueToUse on next tick
//        }
    }

    // ===== Helpers ===== //

    private PrimedTnt findNearbyTnt() {
        AABB searchBox = erythrocyte.getBoundingBox().inflate(SEARCH_RADIUS);
        List<PrimedTnt> candidates = erythrocyte.level().getEntitiesOfClass(
                PrimedTnt.class,
                searchBox,
                tnt -> tnt.isAlive() && erythrocyte.hasLineOfSight(tnt)
        );

        if (candidates.isEmpty()) return null;

        // Pick the closest one
        return candidates.stream()
                .min((a, b) -> Double.compare(
                        erythrocyte.distanceToSqr(a),
                        erythrocyte.distanceToSqr(b)
                ))
                .orElse(null);
    }

    private Player getTntOwner(PrimedTnt tnt) {
        // PrimedTnt stores the entity that placed it as the owner
        // In 1.20.1 this is getOwner(), which returns the Entity that primed it
        if (tnt.getOwner() instanceof Player player) return player;
        return null;
    }
}
