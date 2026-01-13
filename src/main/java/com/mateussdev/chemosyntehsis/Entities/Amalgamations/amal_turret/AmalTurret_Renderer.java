package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_turret;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmalTurret_Renderer extends GeoEntityRenderer<AmalTurret> {
    public AmalTurret_Renderer(EntityRendererProvider.Context context) {
        super(context, new AmalTurret_Model());
    }
}