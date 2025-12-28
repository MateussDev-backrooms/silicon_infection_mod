package com.mateussdev.chemosyntehsis.Entities.veg_roller;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class VegetativeRoller_Model extends GeoModel<VegetativeRoller> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/veg_roller.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/veg_roller.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/veg_roller.animation.json");

    @Override
    public ResourceLocation getModelResource(VegetativeRoller object) { return model; }

    @Override
    public ResourceLocation getTextureResource(VegetativeRoller object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(VegetativeRoller object) { return animation; }

    @Override
    public void setCustomAnimations(VegetativeRoller animatable, long instanceId, AnimationState<VegetativeRoller> animationState) {
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
