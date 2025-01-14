package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class BEVeinBlock_Model extends GeoModel<BEVeinBlock> {
    // Models must be stored in assets/<modid>/geo with subfolders supported inside the geo folder
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID, "geo/blockentity/vein_block.geo.json");
    // Textures must be stored in assets/<modid>/textures with subfolders supported inside the textures folder
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID, "textures/block/vein_block_be.png");
    // Animations must be stored in assets/<modid>/animations with subfolders supported inside the animations folder
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID, "animations/blockentity/vein_block.animation.json");

    @Override
    public ResourceLocation getModelResource(BEVeinBlock object) {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureResource(BEVeinBlock object) {
        return this.texture;
    }

    @Override
    public ResourceLocation getAnimationResource(BEVeinBlock object) {
        return this.animation;
    }
}
