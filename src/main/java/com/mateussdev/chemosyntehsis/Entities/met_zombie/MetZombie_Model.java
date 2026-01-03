package com.mateussdev.chemosyntehsis.Entities.met_zombie;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class MetZombie_Model extends GeoModel<MetZombie> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/met_zombie.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/met_zombie.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/met_zombie.animation.json");

    @Override
    public ResourceLocation getModelResource(MetZombie object) { return model; }

    @Override
    public ResourceLocation getTextureResource(MetZombie object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(MetZombie object) { return animation; }

    @Override
    public void setCustomAnimations(MetZombie animatable, long instanceId, AnimationState<MetZombie> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);
    }
}
