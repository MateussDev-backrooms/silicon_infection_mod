package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MutationSwimming_Model extends GeoModel<MutationSwimming> {
    @Override
    public ResourceLocation getModelResource(MutationSwimming animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/mutation/mutation_swimming.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutationSwimming animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/mutation/mutation_swimming.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutationSwimming animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "animations/mutation/mutation_swimming.animation.json");
    }
}
