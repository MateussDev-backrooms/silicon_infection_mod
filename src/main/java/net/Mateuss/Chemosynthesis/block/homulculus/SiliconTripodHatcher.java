package net.Mateuss.Chemosynthesis.block.homulculus;

import net.Mateuss.Chemosynthesis.block.IBloodFillable;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SiliconTripodHatcher extends Block implements IBloodFillable {

    public SiliconTripodHatcher(Properties pProperties) {
        super(pProperties);

    }





    @Override
    public void onBloodFlow(Level level, BlockPos pos, BlockState state, int bloodLevel) {
        if(!level.isClientSide()) {
            if(level.random.nextBoolean()) {
                EntitySiliconTripod tripod = ModEntities.SILICON_TRIPOD.get().create(level);
                if(tripod != null) {
                    tripod.moveTo(pos.getX()+level.random.nextDouble(), pos.getY()+1, pos.getZ()+level.random.nextDouble());
                    level.addFreshEntity(tripod);
                }
            }
        }
    }

    @Override
    public boolean canReceiveBlood(ServerLevel level, BlockPos pos) {
        return true;
    }
}
