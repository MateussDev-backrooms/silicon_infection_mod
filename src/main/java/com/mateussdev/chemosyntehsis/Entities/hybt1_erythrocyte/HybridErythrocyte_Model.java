package com.mateussdev.chemosyntehsis.Entities.hybt1_erythrocyte;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class HybridErythrocyte_Model extends GeoModel<HybridErythrocyte> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/hybt1_erythrocyte.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/hybt1_erythrocyte.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/hybt1_erythrocyte.animation.json");

    @Override
    public ResourceLocation getModelResource(HybridErythrocyte object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HybridErythrocyte object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HybridErythrocyte object) { return animation; }

    @Override
    public void setCustomAnimations(HybridErythrocyte animatable, long instanceId, AnimationState<HybridErythrocyte> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var root = this.getBone("root");

        double dy = animatable.getDeltaMovement().y;

        root.get().setRotY((float) dy);

    }
}
