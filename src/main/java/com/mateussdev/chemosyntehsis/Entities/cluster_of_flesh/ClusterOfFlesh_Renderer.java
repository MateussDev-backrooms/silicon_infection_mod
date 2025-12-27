package com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ClusterOfFlesh_Renderer extends GeoEntityRenderer<ClusterOfFlesh> {
    public ClusterOfFlesh_Renderer(EntityRendererProvider.Context context) {
        super(context, new ClusterOfFlesh_Model());
    }
}
