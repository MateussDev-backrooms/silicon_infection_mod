package net.Mateuss.Chemosynthesis.block.advanced;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveBlobBlock extends Block {
    public ExplosiveBlobBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if(!pLevel.isClientSide()) {
            spawnGreenParticles((ServerLevel) pLevel, pPos);
            pLevel.explode(
                    null,
                    pPos.getX()+0.5F,
                    pPos.getY()+0.5F,
                    pPos.getZ()+0.5F,
                    4.0F,
                    Level.ExplosionInteraction.TNT);
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    private void spawnGreenParticles(ServerLevel world, BlockPos pos) {
        for (int i = 0; i < 80; i++) {  // Spawn multiple particles for effect
            double offsetX = (world.random.nextDouble() - 0.5) * 0.5;
            double offsetY = world.random.nextDouble() * 0.5;
            double offsetZ = (world.random.nextDouble() - 0.5) * 0.5;

            world.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,   // Example green particle
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    offsetX,    // X offset for randomness
                    offsetY,    // Y offset
                    offsetZ,    // Z offset
                    0.1         // Speed of the particle
            );
        }
    }
}
