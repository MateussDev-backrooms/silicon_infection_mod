package com.mateussdev.chemosyntehsis.Entities.Projectiles.mutated_harpoon;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MutatedHarpoonEntity_Model extends GeoModel<MutatedHarpoonEntity> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/bulb_projectile.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/bulb_projectile.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/bulb_projectile.animation.json");

    @Override
    public ResourceLocation getModelResource(MutatedHarpoonEntity object) { return model; }

    @Override
    public ResourceLocation getTextureResource(MutatedHarpoonEntity object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(MutatedHarpoonEntity object) { return animation; }

    @Override
    public void setCustomAnimations(MutatedHarpoonEntity animatable, long instanceId, AnimationState<MutatedHarpoonEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var head = this.getBone("appendage9");

        if (head != null) {
            float pitch = animatable.xRotO+90;
            float yaw = animatable.yRotO+180;

            head.get().setRotX(degreesToRadians(-pitch));
            head.get().setRotY(degreesToRadians(yaw));
        }
    }

    private float degreesToRadians(float deg) {
        return deg * Mth.PI / 180f;
    }
}
