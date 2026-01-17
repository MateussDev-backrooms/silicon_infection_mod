package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TethEnderman_Renderer extends GeoEntityRenderer<TethEnderman> {
    public TethEnderman_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethEnderman_Model());
    }
}
