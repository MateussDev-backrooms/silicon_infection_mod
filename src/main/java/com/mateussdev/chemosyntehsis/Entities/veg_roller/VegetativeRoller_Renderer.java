package com.mateussdev.chemosyntehsis.Entities.veg_roller;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VegetativeRoller_Renderer extends GeoEntityRenderer<VegetativeRoller> {
    public VegetativeRoller_Renderer(EntityRendererProvider.Context context) {
        super(context, new VegetativeRoller_Model());
    }
}