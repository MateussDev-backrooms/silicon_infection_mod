package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mateussdev.chemosyntehsis.Core.ModBlockEntities;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar.AmalRadar;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class BEVeinBlock extends BlockEntity{
    private static final int TICK_INTERVAL = 10; // Ticks every 10 game ticks (0.5 seconds)
    private int tickCounter = 0;
    private List<BlockPos> connectedBlocks = new ArrayList<>();

    public BEVeinBlock(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VEIN_BLOCK.get(), pos, state);
    }

    public void tick() {
        if (level == null || level.isClientSide()) return;

        tickCounter++;
        if (tickCounter >= TICK_INTERVAL) {
            tickCounter = 0;
            findAndConnectBlocks();
        }
    }

    private void findAndConnectBlocks() {
        if (level == null) return;

        connectedBlocks.clear();
        int range = 10; // Check within 10 blocks radius

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos checkPos = worldPosition.offset(x, y, z);
                    BlockState state = level.getBlockState(checkPos);

                    if (state.is(this.getBlockState().getBlock()) &&
                            !checkPos.equals(worldPosition)) {
                        connectedBlocks.add(checkPos);
                    }
                }
            }
        }

        // Update visuals on client side
        syncConnections();
    }

    public List<BlockPos> getConnectedBlocks() {
        return connectedBlocks;
    }

    private void syncConnections() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ConnectionCount", connectedBlocks.size());
        int i = 0;
        for (BlockPos pos : connectedBlocks) {
            tag.putLong("Connection" + i, pos.asLong());
            i++;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        connectedBlocks.clear();
        int count = tag.getInt("ConnectionCount");
        for (int i = 0; i < count; i++) {
            long posLong = tag.getLong("Connection" + i);
            connectedBlocks.add(BlockPos.of(posLong));
        }
    }
}
