package com.mateussdev.chemosyntehsis.Blocks;

import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;

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
