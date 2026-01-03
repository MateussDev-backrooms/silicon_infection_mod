package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class HybridAstrocyte_Model extends GeoModel<HybridAstrocyte> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/hybt1_astrocyte.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/hybt1_astrocyte.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/hybt1_astrocyte.animation.json");

    @Override
    public ResourceLocation getModelResource(HybridAstrocyte object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HybridAstrocyte object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HybridAstrocyte object) { return animation; }
}
