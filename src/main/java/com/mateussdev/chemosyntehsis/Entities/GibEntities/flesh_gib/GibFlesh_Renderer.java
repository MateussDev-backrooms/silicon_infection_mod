package com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class GibFlesh_Renderer extends GeoEntityRenderer<GibFlesh> {
    public GibFlesh_Renderer(EntityRendererProvider.Context context) {
        super(context, new GibFlesh_Model());
    }
}
