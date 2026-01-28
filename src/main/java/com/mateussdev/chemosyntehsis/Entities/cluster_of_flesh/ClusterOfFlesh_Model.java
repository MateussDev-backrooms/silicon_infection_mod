package com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class ClusterOfFlesh_Model extends GeoModel<ClusterOfFlesh> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/cluster_of_flesh.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/cluster_of_flesh.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/cluster_of_flesh.animation.json");

    @Override
    public ResourceLocation getModelResource(ClusterOfFlesh object) { return model; }

    @Override
    public ResourceLocation getTextureResource(ClusterOfFlesh object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(ClusterOfFlesh object) { return animation; }
}
