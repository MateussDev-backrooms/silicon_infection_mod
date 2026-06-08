package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_converter;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmalConverter_Renderer extends GeoEntityRenderer<AmalConverter> {
    public AmalConverter_Renderer(EntityRendererProvider.Context context) {
        super(context, new AmalConverter_Model());
    }
}