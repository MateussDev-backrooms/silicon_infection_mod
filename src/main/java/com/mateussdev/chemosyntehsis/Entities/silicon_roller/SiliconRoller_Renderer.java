package com.mateussdev.chemosyntehsis.Entities.silicon_roller;

import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SiliconRoller_Renderer extends GeoEntityRenderer<SiliconRoller> {
    public SiliconRoller_Renderer(EntityRendererProvider.Context context) {
        super(context, new SiliconRoller_Model());
    }
}
