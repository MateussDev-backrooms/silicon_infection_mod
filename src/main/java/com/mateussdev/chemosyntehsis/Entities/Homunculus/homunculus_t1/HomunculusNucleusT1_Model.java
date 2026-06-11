package com.mateussdev.chemosyntehsis.Entities.Homunculus.homunculus_t1;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class HomunculusNucleusT1_Model extends GeoModel<HomunculusNucleusT1> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/homunculus_t1.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/homunculus_t1.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/homunculus_t1.animation.json");

    @Override
    public ResourceLocation getModelResource(HomunculusNucleusT1 object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HomunculusNucleusT1 object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HomunculusNucleusT1 object) { return animation; }

    @Override
    public void setCustomAnimations(HomunculusNucleusT1 animatable, long instanceId, AnimationState<HomunculusNucleusT1> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

//        var root = this.getBone("root");
//        Vec3 rot = animatable.getNormalRot();
//        if(rot != null) {
//            root.get().setRotX((float) rot.x * Mth.PI / 180f);
//            root.get().setRotY((float) rot.y * Mth.PI / 180f);
//            root.get().setRotZ((float) rot.z * Mth.PI / 180f);
//        }

    }
}
