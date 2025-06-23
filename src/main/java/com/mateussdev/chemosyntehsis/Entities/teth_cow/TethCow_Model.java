package com.mateussdev.chemosyntehsis.Entities.teth_cow;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class TethCow_Model extends GeoModel<TethCow> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_cow.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_cow.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_cow.animation.json");

    @Override
    public ResourceLocation getModelResource(TethCow object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethCow object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethCow object) { return animation; }

    @Override
    public void setCustomAnimations(TethCow animatable, long instanceId, AnimationState<TethCow> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
    }
}
