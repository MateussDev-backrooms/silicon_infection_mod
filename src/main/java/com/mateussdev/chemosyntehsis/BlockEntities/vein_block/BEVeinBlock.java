package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mateussdev.chemosyntehsis.Core.ModBlockEntities;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BEVeinBlock extends BlockEntity{
    private static final int RADIUS = 8;
    private final List<BlockPos> connections = new ArrayList<>();

    private static final Logger LOGGER = LogUtils.getLogger();

    public BEVeinBlock(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.VEIN_BLOCK.get(), pPos, pBlockState);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BEVeinBlock be) {
        if (level.getGameTime() % 40 != 0) return;
        if(level instanceof ServerLevel slvl) {
            slvl.sendParticles(
                    ParticleTypes.EXPLOSION,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    1,
                    0,
                    0,
                    0,
                    0.1
            );
        }

        be.scanForConnections();

    }

    private void scanForConnections() {
        connections.clear();

        BlockPos.betweenClosed(
                worldPosition.offset(-RADIUS, -RADIUS, -RADIUS),
                worldPosition.offset(RADIUS, RADIUS, RADIUS)
        ).forEach(p -> {
            if (!p.equals(worldPosition) &&
                    level.getBlockState(p).is(ModBlocks.VEIN_BLOCK.get())) {
                connections.add(p.immutable());
            }
        });

        setChanged();
    }

    public List<BlockPos> getConnections() {
        return connections;
    }
}
