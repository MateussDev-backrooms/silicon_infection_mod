package com.mateussdev.chemosyntehsis.Entities.teth_zombie;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethZombie_Renderer extends GeoEntityRenderer<TethZombie> {
    public TethZombie_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethZombie_Model());
    }
}
