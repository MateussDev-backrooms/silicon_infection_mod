package com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class GibFlesh_Model extends GeoModel<GibFlesh>  {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/gib.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/gib_flesh.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/gib.animation.json");

    @Override
    public ResourceLocation getModelResource(GibFlesh object) { return model; }

    @Override
    public ResourceLocation getTextureResource(GibFlesh object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(GibFlesh object) { return animation; }

    @Override
    public void setCustomAnimations(GibFlesh animatable, long instanceId, AnimationState<GibFlesh> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        GeoBone gibRoot = this.getBone("root").get();

        gibRoot.setScaleX(animatable.getEntityData().get(animatable.SCALE));
        gibRoot.setScaleY(animatable.getEntityData().get(animatable.SCALE));
        gibRoot.setScaleZ(animatable.getEntityData().get(animatable.SCALE));

        gibRoot.setRotY(animatable.getEntityData().get(animatable.ROTATION));
    }
}
