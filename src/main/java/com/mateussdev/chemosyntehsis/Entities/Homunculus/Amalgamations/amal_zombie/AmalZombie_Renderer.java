package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_zombie;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmalZombie_Renderer extends GeoEntityRenderer<AmalZombie> {
    public AmalZombie_Renderer(EntityRendererProvider.Context context) {
        super(context, new AmalZombie_Model());
    }
}