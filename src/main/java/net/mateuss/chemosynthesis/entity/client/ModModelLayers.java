package net.mateuss.chemosynthesis.entity.client;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation SILICON_ROLLER_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "silicon_roller_layer"), "main");
    public static final ModelLayerLocation SILICON_TRIPOD_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "silicon_tripod_layer"), "main");
    public static final ModelLayerLocation TETH_ZOMBIE_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "teth_zombie_layer"), "main");
}
