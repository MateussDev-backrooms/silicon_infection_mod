package com.mateussdev.chemosyntehsis.Entities.Vegetated.vasc_roller;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class VascularRoller_Model extends GeoModel<VascularRoller> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/veg_vascular_bulb.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/vasc_roller.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/veg_vascular_bulb.animation.json");

    @Override
    public ResourceLocation getModelResource(VascularRoller object) { return model; }

    @Override
    public ResourceLocation getTextureResource(VascularRoller object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(VascularRoller object) { return animation; }

    @Override
    public void setCustomAnimations(VascularRoller animatable, long instanceId, AnimationState<VascularRoller> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var root = this.getBone("root");
        Vec3 rot = animatable.getNormalRot();
        if(rot != null) {
            root.get().setRotX((float) rot.x * Mth.PI / 180f);
            root.get().setRotY((float) rot.y * Mth.PI / 180f);
            root.get().setRotZ((float) rot.z * Mth.PI / 180f);
        }

        GeoBone biopile = this.getBone("biopile").get();
        biopile.setScaleX(animatable.getBiomass()/20f);
        biopile.setScaleY(animatable.getBiomass()/20f);
        biopile.setScaleZ(animatable.getBiomass()/20f);

    }
}
