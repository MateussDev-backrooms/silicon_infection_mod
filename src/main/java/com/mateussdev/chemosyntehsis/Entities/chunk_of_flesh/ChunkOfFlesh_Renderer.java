package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChunkOfFlesh_Renderer extends GeoEntityRenderer<ChunkOfFlesh> {
    public ChunkOfFlesh_Renderer(EntityRendererProvider.Context context) {
        super(context, new ChunkOfFlesh_Model());
    }
}
