package net.Mateuss.Chemosynthesis.block.blockentity;

import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class BEOrganelleConnector extends BlockEntity {
    List<BEOrganelleConnector> connectors = new ArrayList<>();

    public BEOrganelleConnector(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ORGANELLE_CONNECTOR.get(), pPos, pBlockState);
    }

    public List<BEOrganelleConnector> getListNeighbours() {
        return connectors;
    }
    /**
     * Called when the block is placed in the world.
     */
    public void onPlace(Level level) {
        if (!level.isClientSide) {
            findAndConnect(level);
        }
    }

    /**
     * Called when the block is removed from the world.
     */
    public void onRemove(Level level) {
        if (!level.isClientSide) {
            disconnectAll(level);
        }
    }

    /**
     * Finds nearby block entities of the same type and connects to them.
     */
    private void findAndConnect(Level level) {
        int radius = 6; // Adjust radius as needed
        for (BlockPos pos : BlockPos.betweenClosed(
                worldPosition.offset(-radius, -radius, -radius),
                worldPosition.offset(radius, radius, radius)
        )) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BEOrganelleConnector connector && !pos.equals(worldPosition)) {
                connect(connector);
                connector.connect(this);
            }
        }
    }

    /**
     * Connects this block entity to another.
     */
    private void connect(BEOrganelleConnector other) {
        if (!connectors.contains(other)) {
            connectors.add(other);
        }
    }

    /**
     * Disconnects this block entity from another.
     */
    private void disconnect(BEOrganelleConnector other) {
        connectors.remove(other);
    }

    /**
     * Disconnects from all connected block entities.
     */
    private void disconnectAll(Level level) {
        for (BEOrganelleConnector pos : new ArrayList<>(connectors)) {
            this.disconnect(pos);
        }
        connectors.clear();
    }


}
