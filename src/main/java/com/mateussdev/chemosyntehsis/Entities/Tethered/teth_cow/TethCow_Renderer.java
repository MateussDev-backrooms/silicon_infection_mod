package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethCow_Renderer extends GeoEntityRenderer<TethCow> {
    public TethCow_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethCow_Model());
    }
}
