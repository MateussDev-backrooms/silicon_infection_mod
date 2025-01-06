package net.Mateuss.Chemosynthesis.block.homulculus;

import net.Mateuss.Chemosynthesis.block.blockentity.BEOrganelleConnector;
import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomunculus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HOrganelleConnector extends BaseEntityBlock {
    public HOrganelleConnector(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof BEOrganelleConnector connector) {
            connector.onPlace(pLevel);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BEOrganelleConnector connector) {
            connector.onRemove(level);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BEOrganelleConnector(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
