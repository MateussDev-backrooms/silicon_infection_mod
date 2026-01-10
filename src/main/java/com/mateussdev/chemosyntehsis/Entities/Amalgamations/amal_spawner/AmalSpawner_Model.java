package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_spawner;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AmalSpawner_Model extends GeoModel<AmalSpawner> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/amal_spawner.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/amal_spawner.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/amal_spawner.animation.json");

    @Override
    public ResourceLocation getModelResource(AmalSpawner object) { return model; }

    @Override
    public ResourceLocation getTextureResource(AmalSpawner object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(AmalSpawner object) { return animation; }

    @Override
    public void setCustomAnimations(AmalSpawner animatable, long instanceId, AnimationState<AmalSpawner> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var root = this.getBone("root");
        Vec3 rot = animatable.getNormalRot();
        if(rot != null) {
            root.get().setRotX((float) rot.x * Mth.PI / 180f);
            root.get().setRotY((float) rot.y * Mth.PI / 180f);
            root.get().setRotZ((float) rot.z * Mth.PI / 180f);
        }

    }
}
