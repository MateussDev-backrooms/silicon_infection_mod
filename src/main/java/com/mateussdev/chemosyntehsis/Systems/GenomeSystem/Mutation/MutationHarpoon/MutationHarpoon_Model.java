package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationHarpoon;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MutationHarpoon_Model extends GeoModel<MutationHarpoon> {
    @Override
    public ResourceLocation getModelResource(MutationHarpoon animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/mutation/mutation_harpoon.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutationHarpoon animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/mutation/mutation_harpoon.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutationHarpoon animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "animations/mutation/mutation_harpoon.animation.json");
    }
}
