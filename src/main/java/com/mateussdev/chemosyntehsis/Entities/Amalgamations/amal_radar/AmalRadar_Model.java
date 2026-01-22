package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_radar;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AmalRadar_Model extends GeoModel<AmalRadar> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/amal_radar.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/amal_radar.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/amal_radar.animation.json");

    @Override
    public ResourceLocation getModelResource(AmalRadar object) { return model; }

    @Override
    public ResourceLocation getTextureResource(AmalRadar object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(AmalRadar object) { return animation; }

    @Override
    public void setCustomAnimations(AmalRadar animatable, long instanceId, AnimationState<AmalRadar> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var root = this.getBone("root");
        Vec3 rot = animatable.getNormalRot();
        if(rot != null) {
            root.get().setRotX((float) rot.x * Mth.PI / 180f);
            root.get().setRotY((float) rot.y * Mth.PI / 180f);
            root.get().setRotZ((float) rot.z * Mth.PI / 180f);
        }

        GeoBone neck = this.getBone("head").get();
        GeoBone head = this.getBone("head").get();


            float yaw   = (float) Math.toRadians(animatable.yHeadRotO);
            float pitch = (float) Math.toRadians(animatable.xRotO);
        if(animatable.getTarget() != null) {
            neck.setRotY(yaw - root.get().getRotY());
            head.setRotX(pitch - root.get().getRotX());
        } else {
            switch (animatable.getAttachDir()) {
                case UP -> {
                    neck.setRotY(-yaw);
                    neck.setRotX(-pitch);
                }

                case DOWN -> {
                    neck.setRotY(yaw);
                    neck.setRotX(pitch);
                }
                default -> {
                    neck.setRotY(-yaw);
                    neck.setRotX(pitch);
                }
            }
        }

    }
}
