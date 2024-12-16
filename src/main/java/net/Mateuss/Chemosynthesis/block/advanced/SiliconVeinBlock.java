package net.Mateuss.Chemosynthesis.block.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class SiliconVeinBlock extends Block implements IBloodFillable {

    //base
    public static final IntegerProperty FILLED = IntegerProperty.create("siliconmod_filled", 0, 2);
    private int stagger_time = 1;

    //connection multipart thing
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public SiliconVeinBlock(Properties pProperties) {

        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(FILLED, 0)
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder
                .add(FILLED, NORTH, SOUTH, WEST, EAST, UP, DOWN);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(pState, pLevel, pPos, pNeighborBlock, pNeighborPos, pMovedByPiston);
        if(!pLevel.isClientSide()) {
            BlockState neighbourState = pLevel.getBlockState(pNeighborPos);
            pLevel.setBlock(pPos, pState
                    .setValue(NORTH, canConnectTo(pLevel.getBlockState(pPos.north())))
                    .setValue(EAST, canConnectTo(pLevel.getBlockState(pPos.east())))
                    .setValue(SOUTH, canConnectTo(pLevel.getBlockState(pPos.south())))
                    .setValue(WEST, canConnectTo(pLevel.getBlockState(pPos.west())))
                    .setValue(UP, canConnectTo(pLevel.getBlockState(pPos.above())))
                    .setValue(DOWN, canConnectTo(pLevel.getBlockState(pPos.below())))
//                    .setValue(FILLED, neighbourState.getValue(FILLED))
                    , 3)

            ;
            pLevel.scheduleTick(pPos, this, stagger_time*2);

        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if(pState.getValue(FILLED) == 2) {
            spreadBlood(pLevel, pPos);
            pLevel.setBlock(pPos, pState.setValue(FILLED, 1), 3);

        }
        else if(pState.getValue(FILLED) == 1) {
            pLevel.setBlock(pPos, pState.setValue(FILLED, 0), 3);
        }
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        if(pState.getValue(FILLED) == 1) {
            pLevel.setBlock(pPos, pState.setValue(FILLED, 0), 3);
        }
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);

        if (!pLevel.isClientSide() && pOldState.isAir()) {

            pLevel.setBlock(pPos, pState.setValue(FILLED, 0), 2);
        }
    }

    private void spreadBlood(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof IBloodFillable interacted) {
                if (interacted.canReceiveBlood(level, neighborPos)) {
                    // Delegate the blood flow handling to the IBloodFillable implementation
                    interacted.onBloodFlow(level, neighborPos, neighborState, 2);
                }
            }
        }
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        return this.defaultBlockState()
                .setValue(NORTH, canConnectTo(level.getBlockState(pos.north())))
                .setValue(EAST, canConnectTo(level.getBlockState(pos.east())))
                .setValue(SOUTH, canConnectTo(level.getBlockState(pos.south())))
                .setValue(WEST, canConnectTo(level.getBlockState(pos.west())))
                .setValue(UP, canConnectTo(level.getBlockState(pos.above())))
                .setValue(DOWN, canConnectTo(level.getBlockState(pos.below())));
    }

    private boolean canConnectTo(BlockState state) {
        return state.getBlock() instanceof IBloodFillable;
    }

    @Override
    public void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel) {
        if(!level.isClientSide()) {
            level.setBlock(pos, state.setValue(FILLED, bloodLevel), 3);
            level.scheduleTick(pos, this, stagger_time);
        }
    }

    @Override
    public boolean canReceiveBlood(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.getValue(FILLED) == 0;
    }

    //TEMP

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        this.onBloodFlow(pLevel, pPos, pState, 2);
        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }
}
