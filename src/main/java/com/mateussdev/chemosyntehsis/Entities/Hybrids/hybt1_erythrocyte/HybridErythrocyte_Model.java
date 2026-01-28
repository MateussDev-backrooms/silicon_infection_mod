package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class HybridErythrocyte_Model extends GeoModel<HybridErythrocyte> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/hybt1_erythrocyte.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/hybt1_erythrocyte.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/hybt1_erythrocyte.animation.json");

    @Override
    public ResourceLocation getModelResource(HybridErythrocyte object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HybridErythrocyte object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HybridErythrocyte object) { return animation; }

    private int t=0;
    @Override
    public void setCustomAnimations(HybridErythrocyte animatable, long instanceId, AnimationState<HybridErythrocyte> animationState) {
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
