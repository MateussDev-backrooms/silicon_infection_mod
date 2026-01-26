package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte.HybridErythrocyte;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseMetabolized;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;

import java.util.EnumSet;
import java.util.List;

public class ErythrocytePickUpMobGoal extends Goal {
    private final HybridErythrocyte erythrocyte;
    private LivingEntity transportTarget;

    public ErythrocytePickUpMobGoal(HybridErythrocyte erythrocyte) {
        this.erythrocyte = erythrocyte;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = erythrocyte.getTarget();
        if (target == null || !target.isAlive()) return false;

        return erythrocyte.getFirstPassenger() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return erythrocyte.getFirstPassenger() == null;
    }

    @Override
    public void start() {
        super.start();

    }

    @Override
    public void tick() {
        super.tick();
        findNearbyAlly();
    }

    private void findNearbyAlly() {
        if(erythrocyte.getTarget() == null) return;

        AABB searchBox = erythrocyte.getBoundingBox().inflate(16);
        List<LivingEntity> allies = erythrocyte.level().getEntitiesOfClass(
                LivingEntity.class,
                searchBox,
                entity -> (entity instanceof BaseTethered || entity instanceof BaseMetabolized || entity instanceof SiliconRoller || entity instanceof ClusterOfFlesh) &&
                        entity.isAlive() &&
                        !entity.isPassenger() &&
                        !entity.equals(erythrocyte) &&
                        erythrocyte.getTarget().distanceTo(entity) > 6f &&
                        erythrocyte.hasLineOfSight(entity)
        );

//        if (allies.isEmpty()) {
//            // No allies found, go straight to attack target
//            return;
//        }

        // Pick the closest ally
        transportTarget = allies.stream()
                .min((a, b) -> Double.compare(
                        erythrocyte.distanceToSqr(a),
                        erythrocyte.distanceToSqr(b)
                ))
                .orElse(null);

        // Move to the ally
        if(transportTarget != null) {
            erythrocyte.getLookControl().setLookAt(transportTarget, 30.0F, 30.0F);
            erythrocyte.getNavigation().moveTo(transportTarget, 1.0);
            erythrocyte.level().addParticle(ParticleTypes.HEART, transportTarget.getX(), transportTarget.getY(), transportTarget.getZ(), 1f, 1f, 1f);

            // Check if close enough to pick up
            if (erythrocyte.distanceToSqr(transportTarget) < 2.25) { // 1.5 blocks
                erythrocyte.getNavigation().stop();
                transportTarget.startRiding(erythrocyte);
                StaticSiliconiteMethods.debugLog("Picked up "+transportTarget.getType().toString());
            }
        }
    }
}
