package com.mateussdev.chemosyntehsis.Entities.Homunculus.genome;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GenomeCarrier_Renderer extends GeoEntityRenderer<GenomeCarrier> {
    public GenomeCarrier_Renderer(EntityRendererProvider.Context context) {
        super(context, new GenomeCarrier_Model());
    }
}
