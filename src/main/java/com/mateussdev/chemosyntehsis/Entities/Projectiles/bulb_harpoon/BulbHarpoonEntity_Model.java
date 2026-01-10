package com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BulbHarpoonEntity_Model extends GeoModel<BulbHarpoonEntity> {
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
    public ResourceLocation getModelResource(BulbHarpoonEntity object) { return model; }

    @Override
    public ResourceLocation getTextureResource(BulbHarpoonEntity object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(BulbHarpoonEntity object) { return animation; }

    @Override
    public void setCustomAnimations(BulbHarpoonEntity animatable, long instanceId, AnimationState<BulbHarpoonEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var head = this.getBone("appendage9"); // Replace with your model bone name

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
