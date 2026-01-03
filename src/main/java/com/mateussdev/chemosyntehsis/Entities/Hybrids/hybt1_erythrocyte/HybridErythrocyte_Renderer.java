package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HybridErythrocyte_Renderer extends GeoEntityRenderer<HybridErythrocyte> {
    public HybridErythrocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridErythrocyte_Model());
    }
}
