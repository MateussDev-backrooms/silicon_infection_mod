package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Entities.teth_cow.TethCow;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChunkOfFlesh_Renderer extends GeoEntityRenderer<ChunkOfFlesh> {
    public ChunkOfFlesh_Renderer(EntityRendererProvider.Context context) {
        super(context, new ChunkOfFlesh_Model());
    }
}
