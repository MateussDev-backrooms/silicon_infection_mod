package net.mateuss.chemosynthesis.block.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SiliconVeinFullBlock extends Block {

    //base
    public static final IntegerProperty FILLED = IntegerProperty.create("siliconmod_filled", 0, 3);
    private int stagger_time = 2;

    //connection multipart thing
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public SiliconVeinFullBlock(Properties pProperties) {

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
            if(pLevel.getBlockState(pNeighborPos).hasProperty(FILLED)) {
                if (pLevel.getBlockState(pNeighborPos).getValue(FILLED) == 2) {
                    scheduleBloodFlow(pLevel, pPos, pState);
                } else {
                    pLevel.scheduleTick(pPos, this, 0);
                }
            }
            pLevel.setBlock(pPos, pState
                    .setValue(NORTH, canConnectTo(pLevel.getBlockState(pPos.north())))
                    .setValue(EAST, canConnectTo(pLevel.getBlockState(pPos.east())))
                    .setValue(SOUTH, canConnectTo(pLevel.getBlockState(pPos.south())))
                    .setValue(WEST, canConnectTo(pLevel.getBlockState(pPos.west())))
                    .setValue(UP, canConnectTo(pLevel.getBlockState(pPos.above())))
                    .setValue(DOWN, canConnectTo(pLevel.getBlockState(pPos.below()))), 3);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
        if(pState.getValue(FILLED) == 2) {
            spreadBlood(pLevel, pPos);
            pLevel.setBlock(pPos, pState.setValue(FILLED, 1), 3);
            bloodflowParticles(pLevel, pPos);

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

    private void scheduleBloodFlow(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, stagger_time); // Schedule blood flow after 2 ticks
        }
    }

    private void spreadBlood(ServerLevel level, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof SiliconVeinFullBlock) {
                if(neighborState.getValue(FILLED) == 0) {
                    level.setBlock(neighborPos, neighborState.setValue(FILLED, 2), 3);
                    level.scheduleTick(neighborPos, neighborState.getBlock(), stagger_time);

                }
            }
        }
    }

    private void bloodflowParticles(ServerLevel world, BlockPos pos) {
        for (int i = 0; i < 20; i++) {
            double offsetX = (world.random.nextDouble() - 0.5) * 0.5;
            double offsetY = world.random.nextDouble() * 0.5;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.5;
            world.sendParticles(
                    ParticleTypes.CRIT,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    offsetX,    // X offset for randomness
                    offsetY,    // Y offset
                    offsetZ,    // Z offset
                    0.03         // Speed of the particle
            );
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
        return state.getBlock() instanceof SiliconVeinFullBlock;
    }

    //TEMP

//    @Override
//    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
//        pLevel.setBlock(pPos, pState.setValue(FILLED, 2), 3);
//        pLevel.scheduleTick(pPos, pState.getBlock(), 0);
//        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
//    }
}
