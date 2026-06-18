package com.mateussdev.chemosyntehsis.Entities.Metabolized.met_zombie;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MetZombie_Renderer extends GeoEntityRenderer<MetZombie> {
    public MetZombie_Renderer(EntityRendererProvider.Context context) {
        super(context, new MetZombie_Model());
    }
}
