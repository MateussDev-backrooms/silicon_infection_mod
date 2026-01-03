package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte;


import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HybridAstrocyte_Renderer extends GeoEntityRenderer<HybridAstrocyte> {
    public HybridAstrocyte_Renderer(EntityRendererProvider.Context context) {
        super(context, new HybridAstrocyte_Model());
    }
}
