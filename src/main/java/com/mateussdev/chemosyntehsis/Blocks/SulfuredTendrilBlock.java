package com.mateussdev.chemosyntehsis.Blocks;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseVegetated;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Map;

public class SulfuredTendrilBlock extends MultifaceBlock {
    private MultifaceSpreader spreader;
    public SulfuredTendrilBlock(Properties pProperties) {
        super(pProperties
                .speedFactor(0.7f)
                .strength(0.2f)
        );
        spreader = new MultifaceSpreader(this);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }
}
