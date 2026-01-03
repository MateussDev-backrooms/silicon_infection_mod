package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HybridThrombocyte_Renderer extends GeoEntityRenderer<HybridThrombocyte> {
    public HybridThrombocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridThrombocyte_Model());
    }
}
