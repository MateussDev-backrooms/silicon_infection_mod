package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationTeleportation;

import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class TeleportNearTargetMutGoal extends Goal {
    private Mob mob;
    private MutationTeleportation mutationRef;
    private float teleportationDistance;

    private static final int TELEPORT_CHECK_ITERATIONS = 5;

    public TeleportNearTargetMutGoal(Mob mob, MutationTeleportation mutation, float distance) {
        this.mob = mob;
        this.mutationRef = mutation;
        this.teleportationDistance = distance;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = mob.getTarget();
        return target != null && mutationRef.teleportationT <= 0 && mob.distanceTo(target) >= teleportationDistance && mob.canBeSeenByAnyone();
    }

    @Override
    public void start() {
        if(mob.level() instanceof ServerLevel slvl) {
            RandomSource rng = mob.getRandom();
            LivingEntity target = mob.getTarget();
            //Find random unit XZ vector. Check pos before teleporting
            Vec3 teleportPos = null;
            for(int iter=0; iter<TELEPORT_CHECK_ITERATIONS; iter++) {
                Vec3 dir = new Vec3(rng.nextDouble()*2-1, 0, rng.nextDouble()*2-1).normalize();
                if(target == null) continue;
                //Get position
                teleportPos = dir.scale(teleportationDistance).add(target.position());
                if(mutationRef.checkTeleportPosition(teleportPos, slvl, mob)) break;
                //Position is failed
                teleportPos = null;
            }

            if(teleportPos != null) {
                mutationRef.teleport(mutationRef.cleanTeleportPosition(teleportPos, slvl, mob), mob);
            }
            mutationRef.teleportationT = mutationRef.TELEPORTATION_COOLDOWN;
        }

    }
}
