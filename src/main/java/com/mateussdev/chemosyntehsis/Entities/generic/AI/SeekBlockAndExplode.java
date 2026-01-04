package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Blocks.BiomushBlock;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class SeekBlockAndExplode extends Goal {
    private final ClusterOfFlesh mob;
    private final BiomushBlock targetBlock;
    private BlockPos targetPos;
    private int eatingTime;

    public SeekBlockAndExplode(ClusterOfFlesh mob, BiomushBlock targetBlock) {
        this.mob = mob;
        this.targetBlock = targetBlock;
        this.setFlags(EnumSet.of(Flag.MOVE));
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
        BlockPos pos = mob.blockPosition();
        BlockState state = mob.level().getBlockState(pos);
        if (state.getBlock() instanceof BiomushBlock) {

            //KABOOM

        } else {

            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
        }
    }

    private BlockPos findNearbyBiomush() {
        BlockPos mobPos = mob.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(
                mobPos.offset(-5, -2, -5),
                mobPos.offset(5, 2, 5))) {

            BlockState state = mob.level().getBlockState(pos);

            if (state.getBlock() instanceof BiomushBlock
                    && !state.getValue(BiomushBlock.IS_CONSUMED)) {
                return pos.immutable();
            }
        }

        return null;
    }
}
