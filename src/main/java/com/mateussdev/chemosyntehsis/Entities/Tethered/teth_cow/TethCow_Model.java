package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
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

    private int t = 0;
    @Override
    public void setCustomAnimations(TethCow animatable, long instanceId, AnimationState<TethCow> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        //Missing bulb visuals
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);

        //Breathing
        StaticSiliconiteMethods.updateBreathing(this, "body", 20, 0.02, animationState.getPartialTick(), t);
        t++;

        //Head movement
        StaticSiliconiteMethods.updateHeadRotationAnimal(animatable, this, "head", (float) Math.PI/2, 0f);

        StaticSiliconiteMethods.updateBoneWobble(animatable, this, animationState.getPartialTick());


    }
}
