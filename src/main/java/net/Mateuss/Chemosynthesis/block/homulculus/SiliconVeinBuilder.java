package net.Mateuss.Chemosynthesis.block.homulculus;

import net.Mateuss.Chemosynthesis.block.IBloodFillable;
import net.Mateuss.Chemosynthesis.core.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SiliconVeinBuilder extends Block implements IBloodFillable {
    public static final IntegerProperty DIRECTION = IntegerProperty.create("build_direction", 0, 5);
    public static final IntegerProperty RESOURCES = IntegerProperty.create("build_resources", 0, 10);


    public SiliconVeinBuilder(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(DIRECTION, 0)
                .setValue(RESOURCES, 10)
        )
        ;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DIRECTION, RESOURCES);
    }

    private int randomDirection(boolean[] arr, Level level) {
        for(int it=0; it<arr.length*2; it++) {
            int _chosen = level.random.nextIntBetweenInclusive(0, arr.length-1);
            if(!arr[_chosen]) {
                return _chosen;
            }
        }
        return -1;
    }

    private boolean canExpandTo(Level level, BlockPos pos) {
        BlockState _state = level.getBlockState(pos);

        return _state.isAir() || !_state.isSolid() || _state.getDestroySpeed(level, pos) <= 0.2f;
    }

    private boolean surroundedByBlocks(Level level, BlockPos pos) {
        return !canExpandTo(level, pos.relative(Direction.NORTH))
                && !canExpandTo(level, pos.relative(Direction.SOUTH))
                && !canExpandTo(level, pos.relative(Direction.WEST))
                && !canExpandTo(level, pos.relative(Direction.EAST))
                && !canExpandTo(level, pos.relative(Direction.UP))
                && !canExpandTo(level, pos.relative(Direction.DOWN));

    }
    private boolean surroundedByVeins(Level level, BlockPos pos) {
        return !(level.getBlockState(pos.relative(Direction.NORTH, 2)).getBlock() instanceof SiliconVeinBlock)
                && !(level.getBlockState(pos.relative(Direction.SOUTH, 2)).getBlock() instanceof SiliconVeinBlock)
                && !(level.getBlockState(pos.relative(Direction.WEST, 2)).getBlock() instanceof SiliconVeinBlock)
                && !(level.getBlockState(pos.relative(Direction.EAST, 2)).getBlock() instanceof SiliconVeinBlock);
    }

    private boolean connectedToVeinsDirectly(Level level, BlockPos pos) {
        return level.getBlockState(pos.relative(Direction.NORTH, 1)).getBlock() instanceof SiliconVeinBlock
                || level.getBlockState(pos.relative(Direction.SOUTH, 1)).getBlock() instanceof SiliconVeinBlock
                || level.getBlockState(pos.relative(Direction.WEST, 1)).getBlock() instanceof SiliconVeinBlock
                || level.getBlockState(pos.relative(Direction.EAST, 1)).getBlock() instanceof SiliconVeinBlock
                || level.getBlockState(pos.relative(Direction.DOWN, 1)).getBlock() instanceof SiliconVeinBlock
                || level.getBlockState(pos.relative(Direction.UP, 1)).getBlock() instanceof SiliconVeinBlock
                ;
    }
    private Direction oppositeDirectionOf(Direction dir) {
        if(dir == Direction.NORTH) return Direction.SOUTH;
        if(dir == Direction.SOUTH) return Direction.NORTH;
        if(dir == Direction.EAST) return Direction.WEST;
        if(dir == Direction.WEST) return Direction.EAST;
        if(dir == Direction.UP) return Direction.DOWN;
        if(dir == Direction.DOWN) return Direction.UP;
        return Direction.NORTH;
    }
    private boolean connectedVeinsDirectlySmart(Level level, BlockPos pos, Direction last_moved_dir) {
        for(Direction dir : Direction.values()) {
            if(dir != oppositeDirectionOf(last_moved_dir)) {
                if(level.getBlockState(pos.relative(dir)).getBlock() instanceof SiliconVeinBlock) {
                    return true;
                }
            }
        }
        return false;
    }

    private void collapseVeinBuilder(Level level, BlockPos pos) {
        Block[] collapsibleBlocks = {
                ModBlocks.HOM_ROOT_VAUCOLE.get(),
                ModBlocks.HOM_TRIPOD_HATCHER.get()
        };

        Block chosenBlock = collapsibleBlocks[level.random.nextIntBetweenInclusive(0, collapsibleBlocks.length-1)];
        level.setBlock(pos, chosenBlock.defaultBlockState(), 3);
    }
    private void collapseToVein(Level level, BlockPos pos) {
        level.setBlock(pos, ModBlocks.HOM_VEIN_BLOCK.get().defaultBlockState(), 3);
    }

    @Override
    public void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel) {
        if(!level.isClientSide()) {
            if(level.random.nextBoolean()) {
                level.playSound(null, pos, SoundEvents.MUD_BREAK, SoundSource.AMBIENT);

                if(state.getValue(RESOURCES) > 0) {
                    Direction[] priority_dirs = {
                            Direction.NORTH,
                            Direction.EAST,
                            Direction.SOUTH,
                            Direction.WEST,
                            Direction.UP,
                            Direction.DOWN
                    };
                    Direction moveInDirection = priority_dirs[state.getValue(DIRECTION)];
                    boolean _collapsed = false;

                    //first check if it can go down 2 blocks due to gravity
                    //make sure there are no vein blocks to the sides of this one
                    if(canExpandTo(level, pos.below())
                            && canExpandTo(level, pos.below(2))
                            && !connectedToVeinsDirectly(level, pos.below())
                            && !connectedToVeinsDirectly(level, pos.below(2)))
                    {

                        level.setBlock(pos.below(2), state, 3);
                        level.setBlock(pos.below(), ModBlocks.HOM_VEIN_BLOCK.get().defaultBlockState(), 3);
                        level.setBlock(pos, ModBlocks.HOM_VEIN_BLOCK.get().defaultBlockState(), 3);
                        return;
                    }

                    //then check if it is surrounded by blocks or by veins on a 2 distance
                    if(surroundedByBlocks(level, pos) || surroundedByBlocks(level, pos)) {
                        collapseVeinBuilder(level, pos);
                        return;
                    }

                    //check if the block has any blood vessel connected to it other than the one that provided it with blood
                    if(connectedVeinsDirectlySmart(level, pos, priority_dirs[state.getValue(DIRECTION)])) {
                        //instead of collapsing it would disappear as turning into a vein might cause a loop
                        collapseVeinBuilder(level, pos);
                    }

                    //check if the block is surrounded, but it can go up
                    if(
                        !canExpandTo(level, pos.relative(Direction.NORTH))
                            && !canExpandTo(level, pos.relative(Direction.SOUTH))
                            && !canExpandTo(level, pos.relative(Direction.WEST))
                            && !canExpandTo(level, pos.relative(Direction.EAST))
                            && !canExpandTo(level, pos.relative(Direction.DOWN))
                            && canExpandTo(level, pos.relative(Direction.UP))
                    ) {
                        level.setBlock(pos.above(), state, 3);
                        level.setBlock(pos, ModBlocks.HOM_VEIN_BLOCK.get().defaultBlockState(), 3);
                        return;
                    }

                    //select random direction and check if it can move there until it moves
                    boolean[] checkedDirs = {
                            false,
                            false,
                            false,
                            false
                    };

                    //check for every other direction on the horizontal plane
                    for(int i=0; i<4; i++) {
                        int chosenDir = randomDirection(checkedDirs, level);

                        //instead of randomly choosing a direction it will ALWAYS move anywhere it can
                        if(chosenDir != -1) {

                            //check if we can move 2 blocks in the chosen direction
                            //make sure the destination doesn't connect to any other veins at any point
                            if(level.isEmptyBlock(pos.relative(priority_dirs[chosenDir]))
                                    && level.isEmptyBlock(pos.relative(priority_dirs[chosenDir], 2))
                                    && !connectedVeinsDirectlySmart(level, pos.relative(priority_dirs[chosenDir]), priority_dirs[chosenDir])
                                    && !connectedToVeinsDirectly(level, pos.relative(priority_dirs[chosenDir], 2))
                            ) {

                                //we move in this direction
                                moveInDirection = priority_dirs[chosenDir];
                                level.setBlock(pos.relative(moveInDirection, 2), state.setValue(RESOURCES, state.getValue(RESOURCES)-1), 3);
                                level.setBlock(pos.relative(moveInDirection, 1), ModBlocks.HOM_VEIN_BLOCK.get().defaultBlockState(), 3);
                                level.setBlock(pos, ModBlocks.HOM_VEIN_BLOCK.get().defaultBlockState(), 3);
                                //don't return so we can check out other directions we can move in
                            } else {
                                //we cannot move in this direction so we check it out
                                checkedDirs[chosenDir] = true;
                            }
                        } else {
                            //we break off the loop if we get -1
                            break;
                        }
                    }
                    //if we are here it means we cannot move anywhere
                    //collapse if it cannot move
                    collapseVeinBuilder(level, pos);
                } else {
                    //collapse due to running out of resources
                    collapseVeinBuilder(level, pos);
                }
            }
        }
    }

    @Override
    public boolean canReceiveBlood(ServerLevel level, BlockPos pos) {
        return true;
    }
}
