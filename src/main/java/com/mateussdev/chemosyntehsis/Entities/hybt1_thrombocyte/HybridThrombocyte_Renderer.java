package com.mateussdev.chemosyntehsis.Entities.hybt1_thrombocyte;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HybridThrombocyte_Renderer extends GeoEntityRenderer<HybridThrombocyte> {
    public HybridThrombocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridThrombocyte_Model());
    }
}
