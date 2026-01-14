package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class TethZombie_Model extends GeoModel<TethZombie> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_zombie.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_zombie.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_zombie.animation.json");

    @Override
    public ResourceLocation getModelResource(TethZombie object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethZombie object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethZombie object) { return animation; }

    private int t = 0;
    @Override
    public void setCustomAnimations(TethZombie animatable, long instanceId, AnimationState<TethZombie> animationState) {
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
