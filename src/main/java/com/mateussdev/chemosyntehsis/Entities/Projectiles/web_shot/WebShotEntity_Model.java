package com.mateussdev.chemosyntehsis.Entities.Projectiles.web_shot;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class WebShotEntity_Model extends GeoModel<WebShotEntity> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/web_shot.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/web_shot.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/web_shot.animation.json");

    @Override
    public ResourceLocation getModelResource(WebShotEntity object) { return model; }

    @Override
    public ResourceLocation getTextureResource(WebShotEntity object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(WebShotEntity object) { return animation; }

    @Override
    public void setCustomAnimations(WebShotEntity animatable, long instanceId, AnimationState<WebShotEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var head = this.getBone("root");

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
