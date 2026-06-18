package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_pig;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TethPig_Model extends GeoModel<TethPig> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_pig.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_pig.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_pig.animation.json");

    @Override
    public ResourceLocation getModelResource(TethPig object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethPig object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethPig object) { return animation; }

    private int t = 0;
    @Override
    public void setCustomAnimations(TethPig animatable, long instanceId, AnimationState<TethPig> animationState) {
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
