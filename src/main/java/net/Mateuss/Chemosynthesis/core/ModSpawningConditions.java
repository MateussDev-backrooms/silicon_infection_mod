package net.Mateuss.Chemosynthesis.core;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

public class ModSpawningConditions {
    public static boolean spawnLowTierSiliconite(EntityType<?> entity, LevelAccessor level, MobSpawnType type, BlockPos pos, RandomSource random) {
        BlockState blockBelow = level.getBlockState(pos.below());
        int lightLevel = level.getBrightness(LightLayer.BLOCK, pos);

        // Check if the block below is solid and the light level is low enough for spawning
        return blockBelow.isSolid() && lightLevel <= 7;
    }
}
