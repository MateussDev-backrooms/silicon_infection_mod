package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
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

    private int t=0;
    @Override
    public void setCustomAnimations(TethSkeleton animatable, long instanceId, AnimationState<TethSkeleton> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
        //Breathing
        StaticSiliconiteMethods.updateBreathing(this, "body", 20, 0.02, animationState.getPartialTick(), t);
        t++;

        //Head movement
        StaticSiliconiteMethods.updateHeadRotationAnimal(animatable, this, "head", (float) Math.PI/2, 0f);

        StaticSiliconiteMethods.updateBoneWobble(animatable, this, animationState.getPartialTick());
    }
}
