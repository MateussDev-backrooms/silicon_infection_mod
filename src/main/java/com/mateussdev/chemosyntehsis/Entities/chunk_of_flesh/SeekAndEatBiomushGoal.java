package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Blocks.BiomushBlock;
import com.mateussdev.chemosyntehsis.Mixin.RemoveBlockGoalAccessor;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class SeekAndEatBiomushGoal extends RemoveBlockGoal {
    private final ChunkOfFlesh mob;
    private float eatProgress = 0f;
    private boolean isEating = false;

    public SeekAndEatBiomushGoal(Block pBlockToRemove, PathfinderMob pRemoverMob, double pSpeedModifier, int pSearchRange) {
        super(pBlockToRemove, pRemoverMob, pSpeedModifier, pSearchRange);
        this.mob = (ChunkOfFlesh) pRemoverMob;
    }

    @Override
    public void start() {
        super.start();
        mob.setIsGoingToBiomush(true);
    }

    @Override
    public void stop() {
        super.stop();
        mob.getNavigation().stop();
        mob.setBurrowAnimation(false);
        isEating = false;
    }

    @Override
    public void tick() {
        super.tick();
//        if(isEating) {
//            if(++eatProgress > 60f) {
//                BlockPos blockpos1 = ((RemoveBlockGoalAccessor)this).callGetPosWithBlock(mob.blockPosition(), mob.level());
//                playBreakSound(mob.level(), blockpos1);
//                isEating = false;
//            }
//        }
    }

    @Override
    public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
        super.playDestroyProgressSound(pLevel, pPos);
        mob.getNavigation().stop(); // Stop moving

        BlockState state = mob.level().getBlockState(pPos);

        // Start eating if fresh
        if (!state.getValue(BiomushBlock.IS_CONSUMED)) {
            mob.level().setBlock(pPos, state.setValue(BiomushBlock.IS_CONSUMED, true), 3);
            mob.setBurrowAnimation(true);
//            StaticSiliconiteMethods.debugLog("eat eat");
            isEating = true;
        }
    }

    @Override
    public void playBreakSound(Level pLevel, BlockPos pPos) {
        super.playBreakSound(pLevel, pPos);
        mob.consumeBiomush();
//        StaticSiliconiteMethods.debugLog("breab break");
    }

    @Override
    public double acceptedDistance() {
        return 1.3f;
    }
}
