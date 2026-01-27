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
    private int eatingTime = 0;

    // OPTIMIZATION: Cooldowns to prevent searching every tick
    private int searchCooldown = 0;
    private static final int SEARCH_DELAY = 40; // Only search every 2 seconds
    private static final int SEEK_RADIUS = 8;
    private static final int ATTEMPTS = 64; // How many random spots to "sniff"

    public SeekAndEatBiomushGoal(ChunkOfFlesh mob, BiomushBlock targetBlock) {
        this.mob = mob;
        this.targetBlock = targetBlock;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // 1. Don't start if we already have a combat target
        if (mob.getTarget() != null) return false;

        // 2. Don't start if we are already busy eating
        if (eatingTime > 0) return false;

        // 3. Don't spam the search (Optimization!)
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }

        // 4. Perform the Random Sniff
        targetPos = findRandomBiomush();

        // If we found nothing, reset cooldown so we don't check again immediately
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
        // Continue as long as we have a target OR we are actively eating
        if (eatingTime > 0) return true;

        // If we haven't started eating, check if the block still exists
        if (targetPos == null) return false;

        // Only check block state every few ticks to save performance
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

        // LOGIC: Check if we are standing on the target
        BlockPos pos = mob.blockPosition();

        // Check strictly the target position first
        if (pos.equals(targetPos)) {
            BlockState state = mob.level().getBlockState(pos);

            // Are we standing on a Biomush?
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
                    mob.level().destroyBlock(pos, false); // Break block
                    mob.consumeBiomush(); // Logic method
                    targetPos = null; // Stop goal
                }
            } else {
                // Block disappeared or was invalid
                stop();
            }
        } else {
            // We are NOT at the target yet.
            // Ensure we are actually moving towards it.
            // (Navigation handles this automatically usually, but this ensures we don't just stand there)
            mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1.0D);
            mob.setBurrowAnimation(false);
        }
    }

    // THE OPTIMIZED SEARCH: "The Sniff"
    private BlockPos findRandomBiomush() {
        BlockPos mobPos = mob.blockPosition();

        // Instead of checking 1500 blocks, we check 5 random spots.
        for (int i = 0; i < ATTEMPTS; i++) {
            int x = mobPos.getX() + (2 * mob.getRandom().nextInt(SEEK_RADIUS) - SEEK_RADIUS);
            int z = mobPos.getZ() + (2 * mob.getRandom().nextInt(SEEK_RADIUS) - SEEK_RADIUS);
            int y = mobPos.getY() + (mob.getRandom().nextInt(5) - 2); // Small vertical range

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
