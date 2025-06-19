package com.mateussdev.chemosyntehsis.Entities.teth_cow;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethCow_Renderer extends GeoEntityRenderer<TethCow> {
    public TethCow_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethCow_Model());
    }
}
