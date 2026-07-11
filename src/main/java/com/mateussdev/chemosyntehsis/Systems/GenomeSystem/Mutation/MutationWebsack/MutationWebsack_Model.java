package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationWebsack;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MutationWebsack_Model extends GeoModel<MutationWebsack> {
    @Override
    public ResourceLocation getModelResource(MutationWebsack animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/mutation/mutation_websack.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutationWebsack animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/mutation/mutation_websack.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutationWebsack animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "animations/mutation/mutation_websack.animation.json");
    }
}
