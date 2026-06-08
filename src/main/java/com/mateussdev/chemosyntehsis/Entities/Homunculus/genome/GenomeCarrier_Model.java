package com.mateussdev.chemosyntehsis.Entities.Homunculus.genome;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GenomeCarrier_Model extends GeoModel<GenomeCarrier> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/genome.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/genome.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/genome.animation.json");

    @Override
    public ResourceLocation getModelResource(GenomeCarrier object) { return model; }

    @Override
    public ResourceLocation getTextureResource(GenomeCarrier object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(GenomeCarrier object) { return animation; }

    private int t;
    @Override
    public void setCustomAnimations(GenomeCarrier animatable, long instanceId, AnimationState<GenomeCarrier> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        var root = this.getBone("body");
        StaticSiliconiteMethods.updateHeadRotationAnimal(animatable, this, "body", (float) Math.PI/2, 0f);
    }
}
