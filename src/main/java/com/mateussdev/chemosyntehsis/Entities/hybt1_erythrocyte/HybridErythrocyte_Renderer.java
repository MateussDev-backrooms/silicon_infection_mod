package com.mateussdev.chemosyntehsis.Entities.hybt1_erythrocyte;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HybridErythrocyte_Renderer extends GeoEntityRenderer<HybridErythrocyte> {
    public HybridErythrocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridErythrocyte_Model());
    }
}
