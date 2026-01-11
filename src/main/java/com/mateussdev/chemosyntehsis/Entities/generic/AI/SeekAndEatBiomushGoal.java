package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Blocks.BiomushBlock;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class SeekAndEatBiomushGoal extends Goal {
    private final ChunkOfFlesh mob;
    private final BiomushBlock targetBlock;
    private BlockPos targetPos;
    private int eatingTime;

    public SeekAndEatBiomushGoal(ChunkOfFlesh mob, BiomushBlock targetBlock) {
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
        BlockPos pos = mob.blockPosition();
        BlockState state = mob.level().getBlockState(pos);
        if (state.getBlock() instanceof BiomushBlock) {

            if (!state.getValue(BiomushBlock.IS_CONSUMED)) {
                mob.level().setBlock(pos, state.setValue(BiomushBlock.IS_CONSUMED, true), 3);
            }
            mob.setBurrowAnimation(true);
            eatingTime++;

            if(eatingTime%10==0) {
                mob.level().destroyBlockProgress(mob.getId(), mob.blockPosition(), Mth.floor(eatingTime/10f));
            }

            if (eatingTime > 60) {
                mob.level().destroyBlock(targetPos, false);
                mob.consumeBiomush();
                targetPos = null;
            }
        } else {
            if (state.getBlock() instanceof BiomushBlock && state.getValue(BiomushBlock.IS_CONSUMED)) {
                mob.level().setBlock(pos, state.setValue(BiomushBlock.IS_CONSUMED, false), 3);
            }

            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0);
            mob.setBurrowAnimation(false);
        }
    }

    private static final int SEEK_RADIUS = 8;

    private BlockPos findNearbyBiomush() {
        BlockPos mobPos = mob.blockPosition();

        for (BlockPos pos : BlockPos.betweenClosed(
                mobPos.offset(-SEEK_RADIUS, -2, -SEEK_RADIUS),
                mobPos.offset(SEEK_RADIUS, 2, SEEK_RADIUS))) {

            BlockState state = mob.level().getBlockState(pos);

            if (state.getBlock() instanceof BiomushBlock
                    && !state.getValue(BiomushBlock.IS_CONSUMED)) {
                return pos.immutable();
            }
        }

        return null;
    }
}
