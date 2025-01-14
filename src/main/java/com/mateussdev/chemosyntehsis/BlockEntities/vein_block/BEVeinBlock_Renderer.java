package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BEVeinBlock_Renderer extends GeoBlockRenderer<BEVeinBlock> {
    public BEVeinBlock_Renderer(BlockEntityRendererProvider.Context context) {
        super(new BEVeinBlock_Model());
    }
}
