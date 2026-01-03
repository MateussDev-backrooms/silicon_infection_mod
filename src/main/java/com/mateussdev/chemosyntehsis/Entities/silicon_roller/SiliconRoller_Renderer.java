package com.mateussdev.chemosyntehsis.Entities.silicon_roller;

import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SiliconRoller_Renderer extends GeoEntityRenderer<SiliconRoller> {
    public SiliconRoller_Renderer(EntityRendererProvider.Context context) {
        super(context, new SiliconRoller_Model());
    }
}
