package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class HybridPerfocyte_Model extends GeoModel<HybridPerfocyte> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/hybt2_perfocyte.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/hybt2_perfocyte.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/hybt2_perfocyte.animation.json");

    @Override
    public ResourceLocation getModelResource(HybridPerfocyte object) { return model; }

    @Override
    public ResourceLocation getTextureResource(HybridPerfocyte object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(HybridPerfocyte object) { return animation; }

    private int t=0;
    @Override
    public void setCustomAnimations(HybridPerfocyte animatable, long instanceId, AnimationState<HybridPerfocyte> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

//        var head = this.getBone("root"); // Replace with your model bone name
//
//        if (head.isPresent()) {
//
//            Vec3 delta;
//            if(animatable.getTarget() != null) {
//                delta = animatable.getTarget().position().subtract(animatable.position());
//            } else {
//                delta = animatable.getDeltaMovement();
//            }
//
//            float yaw = (float) Math.atan2(delta.z, delta.x);
//
//            float horizontalDist = (float) Math.sqrt(delta.x * delta.x + delta.z * delta.z);
//            float pitch = (float) Math.atan2(delta.y, horizontalDist);
//
//            head.get().setRotX(Mth.lerp(0.1f, head.get().getRotX(), -pitch));
//            head.get().setRotY(Mth.lerp(0.1f, head.get().getRotY(), -yaw));
//        }
        //Broken off bulbs
        StaticSiliconiteMethods.updateBulbVisuals(animatable, this);

    }
}
