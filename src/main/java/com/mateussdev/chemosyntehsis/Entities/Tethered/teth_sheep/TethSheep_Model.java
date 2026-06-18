package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_sheep;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TethSheep_Model extends GeoModel<TethSheep> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_sheep.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_sheep.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_sheep.animation.json");

    @Override
    public ResourceLocation getModelResource(TethSheep object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethSheep object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethSheep object) { return animation; }

    private int t = 0;
    @Override
    public void setCustomAnimations(TethSheep animatable, long instanceId, AnimationState<TethSheep> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        //Missing bulb visuals
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);

        //Breathing
        StaticSiliconiteMethods.updateBreathing(this, "body", 20, 0.02, animationState.getPartialTick(), t);
        t++;

        //Head movement
        StaticSiliconiteMethods.updateHeadRotationUpright(animatable, this, "head", 0, 0f);

        StaticSiliconiteMethods.updateBoneWobble(animatable, this, animationState.getPartialTick());


    }
}
