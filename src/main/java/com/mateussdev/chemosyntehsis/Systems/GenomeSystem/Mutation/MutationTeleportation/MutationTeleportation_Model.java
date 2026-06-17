package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationTeleportation;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MutationTeleportation_Model extends GeoModel<MutationTeleportation> {
    @Override
    public ResourceLocation getModelResource(MutationTeleportation animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/mutation/mutation_teleportation.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MutationTeleportation animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/mutation/mutation_teleportation.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MutationTeleportation animatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "animations/mutation/mutation_teleportation.animation.json");
    }
}
