package com.mateussdev.chemosyntehsis.Blocks;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FleshPileBlock extends FallingBlock {
    public static final BooleanProperty IS_CONSUMED = BooleanProperty.create("is_consumed");
    public static final IntegerProperty BIOMUSHIFICATION = IntegerProperty.create("biomushification", 0, 10);

    public FleshPileBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(IS_CONSUMED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(IS_CONSUMED);
        pBuilder.add(BIOMUSHIFICATION);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return box(0, 0, 0, 16, 7, 16);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        if(pState.getDestroyProgress(null, pLevel, pPos) == 0f) {
            pState.setValue(IS_CONSUMED, false);
        }

        //Slowly turn into biomush
        pLevel.setBlockAndUpdate(pPos, pState.setValue(BIOMUSHIFICATION, pState.getValue(BIOMUSHIFICATION) + 1));

    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if(pState.getValue(BIOMUSHIFICATION) >= 10) {
            pLevel.setBlock(pPos, ModBlocks.BIOMUSH.get().defaultBlockState(), 3);
        }
    }
}
