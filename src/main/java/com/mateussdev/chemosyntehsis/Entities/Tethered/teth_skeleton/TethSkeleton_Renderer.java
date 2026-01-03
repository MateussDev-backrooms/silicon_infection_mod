package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethSkeleton_Renderer extends GeoEntityRenderer<TethSkeleton> {
    public TethSkeleton_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethSkeleton_Model());
    }
}
