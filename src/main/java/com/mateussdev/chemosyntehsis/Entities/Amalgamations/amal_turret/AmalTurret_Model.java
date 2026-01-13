package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_turret;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AmalTurret_Model extends GeoModel<AmalTurret> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/amal_turret.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/amal_turret.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/amal_turret.animation.json");

    @Override
    public ResourceLocation getModelResource(AmalTurret object) { return model; }

    @Override
    public ResourceLocation getTextureResource(AmalTurret object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(AmalTurret object) { return animation; }

    @Override
    public void setCustomAnimations(AmalTurret animatable, long instanceId, AnimationState<AmalTurret> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var root = this.getBone("root");
        Vec3 rot = animatable.getNormalRot();
        if(rot != null) {
            root.get().setRotX((float) rot.x * Mth.PI / 180f);
            root.get().setRotY((float) rot.y * Mth.PI / 180f);
            root.get().setRotZ((float) rot.z * Mth.PI / 180f);
        }

        GeoBone neck = this.getBone("neck").get();
        GeoBone head = this.getBone("head").get();


            float yaw   = (float) Math.toRadians(animatable.yHeadRot);
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
