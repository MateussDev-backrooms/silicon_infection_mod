package com.mateussdev.chemosyntehsis.Entities.met_cow;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MetCow_Model extends GeoModel<MetCow> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/met_cow.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/met_cow.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/met_cow.animation.json");

    @Override
    public ResourceLocation getModelResource(MetCow object) { return model; }

    @Override
    public ResourceLocation getTextureResource(MetCow object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(MetCow object) { return animation; }

    @Override
    public void setCustomAnimations(MetCow animatable, long instanceId, AnimationState<MetCow> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
    }
}
