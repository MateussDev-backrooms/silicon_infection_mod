package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

public class VegetativeBulb_Model extends GeoModel<VegetativeBulb> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/veg_bulb.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/veg_bulb.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/veg_bulb.animation.json");

    @Override
    public ResourceLocation getModelResource(VegetativeBulb object) { return model; }

    @Override
    public ResourceLocation getTextureResource(VegetativeBulb object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(VegetativeBulb object) { return animation; }

    @Override
    public void setCustomAnimations(VegetativeBulb animatable, long instanceId, AnimationState<VegetativeBulb> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var root = this.getBone("bulb");
        Vec3 rot = animatable.getNormalRot();
        if(rot != null) {
            root.get().setRotX((float) rot.x * Mth.PI / 180f);
            root.get().setRotY((float) rot.y * Mth.PI / 180f);
            root.get().setRotZ((float) rot.z * Mth.PI / 180f);
        }

    }
}
