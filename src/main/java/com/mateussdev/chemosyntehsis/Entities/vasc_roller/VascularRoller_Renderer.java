package com.mateussdev.chemosyntehsis.Entities.vasc_roller;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VascularRoller_Renderer extends GeoEntityRenderer<VascularRoller> {
    public VascularRoller_Renderer(EntityRendererProvider.Context context) {
        super(context, new VascularRoller_Model());
    }
}