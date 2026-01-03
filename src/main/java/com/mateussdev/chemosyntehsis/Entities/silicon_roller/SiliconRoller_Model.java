package com.mateussdev.chemosyntehsis.Entities.silicon_roller;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SiliconRoller_Model extends GeoModel<SiliconRoller> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/silicon_roller.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/silicon_roller.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/silicon_roller.animation.json");

    @Override
    public ResourceLocation getModelResource(SiliconRoller object) { return model; }

    @Override
    public ResourceLocation getTextureResource(SiliconRoller object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(SiliconRoller object) { return animation; }

    private int t = 0;
    @Override
    public void setCustomAnimations(SiliconRoller animatable, long instanceId, AnimationState<SiliconRoller> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        var root = this.getBone("root");

        // Death anim
        if(root != null && animatable.isDeadOrDying()) {
            ++t;
            float scale = Mth.sin(t*8f)*0.15f;
            root.get().setScaleX(1f + scale);
            root.get().setScaleY(1f + scale);
            root.get().setScaleZ(1f + scale);
        }
        //Broken off bulbs
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);


    }
}
