package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_villager;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TethVillager_Model extends GeoModel<TethVillager> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_villager.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_villager.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_villager.animation.json");

    @Override
    public ResourceLocation getModelResource(TethVillager object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethVillager object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethVillager object) { return animation; }

    private int t = 0;
    @Override
    public void setCustomAnimations(TethVillager animatable, long instanceId, AnimationState<TethVillager> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
        //Breathing
        StaticSiliconiteMethods.updateBreathing(this, "body", 20, 0.02, animationState.getPartialTick(), t);
        t++;

        //Head movement
        StaticSiliconiteMethods.updateHeadRotationUpright(animatable, this, "head", (float) 0f, 0f);

        StaticSiliconiteMethods.updateBoneWobble(animatable, this, animationState.getPartialTick());
    }
}
