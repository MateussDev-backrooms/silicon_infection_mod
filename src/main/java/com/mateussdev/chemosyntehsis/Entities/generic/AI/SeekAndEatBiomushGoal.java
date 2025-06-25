package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;

import java.util.EnumSet;

public class SeekAndEatBiomushGoal extends Goal {
    private final ChunkOfFlesh mob;
    private final Block targetBlock;
    private BlockPos targetPos;
    private int eatingTime;

    public SeekAndEatBiomushGoal(ChunkOfFlesh mob, Block targetBlock) {
        this.mob = mob;
        this.targetBlock = targetBlock;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Look for biomush nearby
        targetPos = findNearbyBiomush();
        return targetPos != null;
    }

    @Override
    public void start() {
        if (targetPos != null) {
            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
            eatingTime = 0;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return targetPos != null && mob.level().getBlockState(targetPos).is(targetBlock);
    }

    @Override
    public void tick() {
        if (targetPos == null) return;

        //check if the block below is biomush
        if (mob.level().getBlockState(mob.blockPosition().below()) == targetBlock.defaultBlockState()) {
            mob.setBurrowAnimation(true);
            eatingTime++;
            if(eatingTime%10==0) {
                mob.level().destroyBlockProgress(mob.getId(), mob.blockPosition().below(), Mth.floor(eatingTime/10f));
            }

            if (eatingTime > 60) {
                mob.level().destroyBlock(targetPos, false);
                mob.consumeBiomush();
                targetPos = null;
            }
        } else {
            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
            mob.setBurrowAnimation(false);
        }
    }

    private BlockPos findNearbyBiomush() {
        BlockPos mobPos = mob.blockPosition();
        for (BlockPos pos : BlockPos.betweenClosed(mobPos.offset(-5, -2, -5), mobPos.offset(5, 2, 5))) {
            if (mob.level().getBlockState(pos).is(targetBlock)) {
                return pos.immutable();
            }
        }
        return null;
    }
}
