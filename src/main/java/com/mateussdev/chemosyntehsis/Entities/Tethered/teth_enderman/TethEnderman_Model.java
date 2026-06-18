package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class TethEnderman_Model extends GeoModel<TethEnderman> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_enderman.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_enderman.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_enderman.animation.json");

    @Override
    public ResourceLocation getModelResource(TethEnderman object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethEnderman object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethEnderman object) { return animation; }

    private int t = 0;
    @Override
    public void setCustomAnimations(TethEnderman animatable, long instanceId, AnimationState<TethEnderman> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
        //Breathing
        StaticSiliconiteMethods.updateBreathing(this, "body", 20, 0.02, animationState.getPartialTick(), t);
        t++;

        //Head movement
        StaticSiliconiteMethods.updateHeadRotationUpright(animatable, this, "head", (float) 0f, 0f);

        StaticSiliconiteMethods.updateBoneWobble(animatable, this, animationState.getPartialTick());

        GeoBone root = this.getBone("body").get();
        root.setPosX(animatable.getRandom().nextFloat());
    }
}
