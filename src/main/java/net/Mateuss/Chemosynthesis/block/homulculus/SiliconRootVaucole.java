package net.Mateuss.Chemosynthesis.block.homulculus;

import net.Mateuss.Chemosynthesis.block.IBloodFillable;
import net.Mateuss.Chemosynthesis.block.blockentity.BEVaucoleRootEntity;
import net.Mateuss.Chemosynthesis.block.blockentity.BaseOrganelleRootEntity;
import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SiliconRootVaucole extends BaseEntityBlock implements IBloodFillable {

    public SiliconRootVaucole(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel) {
        if(!level.isClientSide() && level instanceof ServerLevel slvl) {

            BlockEntity be = slvl.getBlockEntity(pos);
            if(be instanceof BEVaucoleRootEntity bore) {
                bore.onBlockFilled(level, pos, state, bloodLevel);
            }
        }
    }

    @Override
    public boolean canReceiveBlood(ServerLevel level, BlockPos pos) {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BEVaucoleRootEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }
}
