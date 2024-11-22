package net.mateuss.chemosynthesis.block.advanced;

import net.mateuss.chemosynthesis.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.apache.logging.log4j.core.jmx.Server;

public class SiliconVeinBuilder extends Block implements IBloodFillable{
    public static final IntegerProperty DIRECTION = IntegerProperty.create("build_direction", 0, 5);


    public SiliconVeinBuilder(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(DIRECTION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(DIRECTION);
    }

    private int chooseDirection(ServerLevel level, BlockPos pos) {
        for(Direction dir : Direction.values()) {
            if(level.isEmptyBlock(pos.relative(dir))) {
                if(dir == Direction.NORTH) {
                    return 0;
                }
                else if(dir == Direction.EAST) {
                    return 1;
                }
                else if(dir == Direction.SOUTH) {
                    return 2;
                }
                else if(dir == Direction.WEST) {
                    return 3;
                }
                else if(dir == Direction.UP) {
                    return 4;
                }
                else if(dir == Direction.DOWN) {
                    return 5;
                }
            }
        }
        return 0;
    }

    @Override
    public void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel) {
        if(!level.isClientSide()) {
            boolean _success = false;
            BlockPos _pos = pos;
            switch (level.getBlockState(pos).getValue(DIRECTION)) {
                case 0:
                   _pos = pos.relative(Direction.NORTH);
                   _success = true;
                   break;
                case 1:
                    _pos = pos.relative(Direction.EAST);
                    _success = true;
                    break;
                case 2:
                    _pos = pos.relative(Direction.SOUTH);
                    _success = true;
                    break;
                case 3:
                    _pos = pos.relative(Direction.WEST);
                    _success = true;
                    break;
                case 4:
                    _pos = pos.relative(Direction.UP);
                    _success = true;
                    break;
                case 5:
                    _pos = pos.relative(Direction.DOWN);
                    _success = true;
                    break;
            }

            if(_success) {
                level.setBlock(_pos, state.setValue(DIRECTION, chooseDirection((ServerLevel) level, pos)), 3);
                level.setBlock(pos, ModBlocks.SILICON_VEIN_BLOCK.get().defaultBlockState(), 3);
            }
        }
    }

    @Override
    public boolean canReceiveBlood(ServerLevel level, BlockPos pos) {
        return true;
    }
}
