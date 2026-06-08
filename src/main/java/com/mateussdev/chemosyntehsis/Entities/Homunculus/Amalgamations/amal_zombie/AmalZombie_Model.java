package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_zombie;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class AmalZombie_Model extends GeoModel<AmalZombie> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/amal_zombie.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/amal_zombie.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/amal_zombie.animation.json");

    @Override
    public ResourceLocation getModelResource(AmalZombie object) { return model; }

    @Override
    public ResourceLocation getTextureResource(AmalZombie object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(AmalZombie object) { return animation; }

    @Override
    public void setCustomAnimations(AmalZombie animatable, long instanceId, AnimationState<AmalZombie> animationState) {
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
