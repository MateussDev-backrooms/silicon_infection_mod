package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HybridPerfocyte_Renderer extends GeoEntityRenderer<HybridPerfocyte> {
    public HybridPerfocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridPerfocyte_Model());
    }
}
