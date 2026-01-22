package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmalRadar_Renderer extends GeoEntityRenderer<AmalRadar> {
    public AmalRadar_Renderer(EntityRendererProvider.Context context) {
        super(context, new AmalRadar_Model());
    }
}