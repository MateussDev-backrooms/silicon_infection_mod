package com.mateussdev.chemosyntehsis.Entities.met_cow;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MetCow_Renderer extends GeoEntityRenderer<MetCow> {
    public MetCow_Renderer(EntityRendererProvider.Context context) {
        super(context, new MetCow_Model());
    }
}
