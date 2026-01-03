package com.mateussdev.chemosyntehsis.Entities.Projectiles;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BulbProjectileEntity_Model extends GeoModel<BulbProjectileEntity> {
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
    public ResourceLocation getModelResource(BulbProjectileEntity object) { return model; }

    @Override
    public ResourceLocation getTextureResource(BulbProjectileEntity object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(BulbProjectileEntity object) { return animation; }

    @Override
    public void setCustomAnimations(BulbProjectileEntity animatable, long instanceId, AnimationState<BulbProjectileEntity> animationState) {
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
