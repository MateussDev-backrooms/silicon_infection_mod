package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class HybridPerfocyte_Model extends GeoModel<HybridPerfocyte> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/hybt2_perfocyte.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/hybt2_perfocyte.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/hybt2_perfocyte.animation.json");

    @Override
    public ResourceLocation getModelResource(HybridPerfocyte object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HybridPerfocyte object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HybridPerfocyte object) { return animation; }

    private int t=0;
    @Override
    public void setCustomAnimations(HybridPerfocyte animatable, long instanceId, AnimationState<HybridPerfocyte> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone body = this.getBone("body").get();
        // Death anim
        if(animatable.isDeadOrDying()) {
            ++t;
            float scale = Mth.sin(t*8f)*0.15f;
            body.setScaleX(1f + scale);
            body.setScaleY(1f + scale);
            body.setScaleZ(1f + scale);
        }
        //Broken off bulbs
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);

    }
}
