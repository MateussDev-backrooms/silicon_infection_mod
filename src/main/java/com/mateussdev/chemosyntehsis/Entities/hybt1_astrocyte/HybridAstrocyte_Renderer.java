package com.mateussdev.chemosyntehsis.Entities.hybt1_astrocyte;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HybridAstrocyte_Renderer extends GeoEntityRenderer<HybridAstrocyte> {
    public HybridAstrocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridAstrocyte_Model());
    }
}
