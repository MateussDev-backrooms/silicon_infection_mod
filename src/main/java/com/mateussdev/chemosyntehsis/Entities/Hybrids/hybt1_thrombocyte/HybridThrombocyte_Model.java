package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class HybridThrombocyte_Model extends GeoModel<HybridThrombocyte> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/hybt1_thrombocyte.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/hybt1_thrombocyte.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/hybt1_thrombocyte.animation.json");

    @Override
    public ResourceLocation getModelResource(HybridThrombocyte object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HybridThrombocyte object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HybridThrombocyte object) { return animation; }
}
