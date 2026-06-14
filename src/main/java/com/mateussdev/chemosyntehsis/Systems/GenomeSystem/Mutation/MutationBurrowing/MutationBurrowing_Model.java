package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MutationBurrowing_Model extends GeoModel<MutationBurrowing> {
    @Override
    public ResourceLocation getModelResource(MutationBurrowing animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/mutation/mutation_burrowing.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutationBurrowing animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/mutation/mutation_burrowing.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutationBurrowing animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "animations/mutation/mutation_burrowing.animation.json");
    }
}
