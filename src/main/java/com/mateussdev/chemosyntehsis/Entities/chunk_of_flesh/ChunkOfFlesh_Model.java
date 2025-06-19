package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class ChunkOfFlesh_Model extends GeoModel<ChunkOfFlesh> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/chunk_of_flesh.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/chunk_of_flesh.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/chunk_of_flesh.animation.json");

    @Override
    public ResourceLocation getModelResource(ChunkOfFlesh object) { return model; }

    @Override
    public ResourceLocation getTextureResource(ChunkOfFlesh object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(ChunkOfFlesh object) { return animation; }
}
