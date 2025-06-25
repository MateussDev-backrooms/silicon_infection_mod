package com.mateussdev.chemosyntehsis.Entities.teth_skeleton;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class TethSkeleton_Model extends GeoModel<TethSkeleton> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_skeleton.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_skeleton.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_skeleton.animation.json");

    @Override
    public ResourceLocation getModelResource(TethSkeleton object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethSkeleton object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethSkeleton object) { return animation; }

    @Override
    public void setCustomAnimations(TethSkeleton animatable, long instanceId, AnimationState<TethSkeleton> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
    }
}
