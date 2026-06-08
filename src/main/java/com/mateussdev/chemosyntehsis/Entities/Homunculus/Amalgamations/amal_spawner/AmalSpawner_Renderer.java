package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_spawner;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmalSpawner_Renderer extends GeoEntityRenderer<AmalSpawner> {
    public AmalSpawner_Renderer(EntityRendererProvider.Context context) {
        super(context, new AmalSpawner_Model());
    }
}