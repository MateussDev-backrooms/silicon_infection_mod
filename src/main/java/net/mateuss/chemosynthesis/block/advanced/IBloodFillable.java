package net.mateuss.chemosynthesis.block.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.core.jmx.Server;

public interface IBloodFillable {
    /**
     * Called when blood flows into this block.
     *
     * @param level The world level.
     * @param pos The position of the block.
     * @param state The current block state.
     * @param bloodLevel The blood level flowing into the block (0-2 or higher if extended).
     */
    void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel);

    boolean canReceiveBlood(ServerLevel level, BlockPos pos);
}
