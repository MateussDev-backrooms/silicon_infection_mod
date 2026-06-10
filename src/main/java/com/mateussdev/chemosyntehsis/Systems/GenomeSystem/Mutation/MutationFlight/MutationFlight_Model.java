package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;

public class MutationFlight_Model extends GeoModel<MutationFlight> {
    @Override
    public ResourceLocation getModelResource(MutationFlight animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/mutation/mutation_flight.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutationFlight animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/mutation/mutation_flight.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutationFlight animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "animations/mutation/mutation_flight.animation.json");
    }
}
