package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.homunculus_t1;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HomunculusNucleusT1_Renderer extends GeoEntityRenderer<HomunculusNucleusT1> {
    public HomunculusNucleusT1_Renderer(EntityRendererProvider.Context context) {
        super(context, new HomunculusNucleusT1_Model());
    }
}