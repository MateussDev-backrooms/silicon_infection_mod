package com.mateussdev.chemosyntehsis.Blocks;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.util.VeinConnectorEntity;
import com.mateussdev.chemosyntehsis.Entities.util.VeinConnectorEntity_Renderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VeinBlock extends Block {
    public VeinBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
        if(pLevel instanceof ServerLevel slvl) {
            VeinConnectorEntity connector = ModEntities.VEIN_CONNECTOR.get().create(slvl);
            connector.moveTo(pPos, 0f, 0f);
            pLevel.addFreshEntity(connector);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        List<VeinConnectorEntity> entities = pLevel.getEntitiesOfClass(
                VeinConnectorEntity.class,
                new AABB(pPos),
                entity -> entity.blockPosition().equals(pPos)
        );

        for (VeinConnectorEntity entity : entities) {
            entity.discard();
        }
    }
}
