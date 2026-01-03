package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte.HybridThrombocyte;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.List;

public class DeliverGibToBulbGoal extends Goal {

    private final HybridThrombocyte mob;
    private VegetativeBulb targetBulb;

    public DeliverGibToBulbGoal(HybridThrombocyte mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!mob.isCarryingGib()) return false;

        List<VegetativeBulb> bulbs = mob.level().getEntitiesOfClass(
                VegetativeBulb.class,
                mob.getBoundingBox().inflate(24)
        );

        if (bulbs.isEmpty()) return false;

        //Linear distance check
        double min_dst = Double.POSITIVE_INFINITY;
        VegetativeBulb best = null;
        for (VegetativeBulb bulb : bulbs) {
            if(bulb.distanceToSqr(mob.position()) < min_dst) {
                min_dst = bulb.distanceToSqr(mob.position());
                best = bulb;
            }
        }

        targetBulb = best;
        return true;
    }

    @Override
    public void start() {
        mob.getNavigation().moveTo(targetBulb, 1.0D);
    }

    @Override
    public void tick() {
        if (targetBulb == null || !targetBulb.isAlive()) return;

        mob.getLookControl().setLookAt(targetBulb, 30.0F, 30.0F);

        if (mob.getNavigation().isDone()) {
            mob.getNavigation().moveTo(targetBulb, 1.1D);
        }

        if (mob.distanceTo(targetBulb) < 1.5D) {
            deliver();
        }
    }

    private void deliver() {
        if (!(mob.level() instanceof ServerLevel slvl)) return;

        mob.useUpGib();

        ChunkOfFlesh chunk = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
        chunk.moveTo(
                targetBulb.getX(),
                targetBulb.getY(),
                targetBulb.getZ()
        );

        targetBulb.discard();
        targetBulb = null;

        slvl.addFreshEntity(chunk);


    }

    @Override
    public boolean canContinueToUse() {
        return mob.isCarryingGib() && targetBulb != null && targetBulb.isAlive();
    }
}
