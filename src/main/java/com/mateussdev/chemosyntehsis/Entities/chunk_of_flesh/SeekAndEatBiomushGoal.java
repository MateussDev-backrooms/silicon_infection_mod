package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Blocks.BiomushBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;

public class SeekAndEatBiomushGoal extends Goal {
    private final ChunkOfFlesh mob;
    private final BiomushBlock targetBlock;
    private BlockPos targetPos;
    private int eatingTime = 0;


    private int searchCooldown = 0;
    private static final int SEARCH_DELAY = 40;
    private static final int SEEK_RADIUS = 8;
    private static final int ATTEMPTS = 64;

    public SeekAndEatBiomushGoal(ChunkOfFlesh mob, BiomushBlock targetBlock) {
        this.mob = mob;
        this.targetBlock = targetBlock;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (eatingTime > 0) return false;

        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }

        //Sniff out the position
        targetPos = findRandomBiomush();
        if (targetPos == null) {
            searchCooldown = SEARCH_DELAY / 2;
        }

        return targetPos != null;
    }

    @Override
    public void start() {
        if (targetPos != null) {
            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0D);
            eatingTime = 0;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (eatingTime > 0) return true;
        if (targetPos == null) return false;


        return mob.level().getBlockState(targetPos).is(targetBlock);
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
        targetPos = null;
        eatingTime = 0;
        mob.setBurrowAnimation(false);
    }

    @Override
    public void tick() {
        if (targetPos == null) return;

        BlockPos pos = mob.blockPosition();

        if (pos.equals(targetPos) || pos.equals(targetPos.above())) {
            BlockState state = mob.level().getBlockState(pos);
            if (state.getBlock() instanceof BiomushBlock) {
                mob.getNavigation().stop(); // Stop moving

                // Start eating if fresh
                if (!state.getValue(BiomushBlock.IS_CONSUMED)) {
                    mob.level().setBlock(pos, state.setValue(BiomushBlock.IS_CONSUMED, true), 3);
                    mob.setBurrowAnimation(true);
                }

                eatingTime++;

                // Client sync crack progress
                if(eatingTime % 10 == 0) {
                    mob.level().destroyBlockProgress(mob.getId(), pos, Mth.floor(eatingTime / 10f));
                }

                // Finished eating
                if (eatingTime > 60) {
                    mob.level().destroyBlock(pos, false);
                    mob.consumeBiomush();
                    targetPos = null;
                }
            } else {
                stop();
            }
        } else {
            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0D);
            mob.setBurrowAnimation(false);
        }
    }

    private BlockPos findRandomBiomush() {
        BlockPos mobPos = mob.blockPosition();

        for (int i = 0; i < ATTEMPTS; i++) {
            int x = mobPos.getX() + (2 * mob.getRandom().nextInt(SEEK_RADIUS) - SEEK_RADIUS);
            int z = mobPos.getZ() + (2 * mob.getRandom().nextInt(SEEK_RADIUS) - SEEK_RADIUS);
            int y = mobPos.getY() + (mob.getRandom().nextInt(5) - 2);

            BlockPos checkPos = new BlockPos(x, y, z);
            BlockState state = mob.level().getBlockState(checkPos);

            if (state.getBlock() instanceof BiomushBlock
                    && !state.getValue(BiomushBlock.IS_CONSUMED)) {
                return checkPos.immutable();
            }
        }

        return null;
    }
}
