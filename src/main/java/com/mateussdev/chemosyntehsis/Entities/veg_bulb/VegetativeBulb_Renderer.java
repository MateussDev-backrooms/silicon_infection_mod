package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VegetativeBulb_Renderer extends GeoEntityRenderer<VegetativeBulb> {
    public VegetativeBulb_Renderer(EntityRendererProvider.Context context) {
        super(context, new VegetativeBulb_Model());
    }
}
