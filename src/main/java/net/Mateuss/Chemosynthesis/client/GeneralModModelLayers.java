package net.Mateuss.Chemosynthesis.client;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class GeneralModModelLayers {
    //PURE
    public static final ModelLayerLocation SILICON_ROLLER_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "silicon_roller_layer"), "main");
    public static final ModelLayerLocation SILIPEDE_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "silipede_layer"), "main");
    public static final ModelLayerLocation SILICON_TRIPOD_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "silicon_tripod_layer"), "main");

    //TETHERED
    public static final ModelLayerLocation TETH_ZOMBIE_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "teth_zombie_layer"), "main");
    public static final ModelLayerLocation TETH_COW_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "teth_cow_layer"), "main");
    public static final ModelLayerLocation TETH_SHEEP_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "teth_sheep_layer"), "main");
    public static final ModelLayerLocation TETH_PIG_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "teth_pig_layer"), "main");

    //CONJUGONAL
    public static final ModelLayerLocation BRACHATIC_STAGE_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "brachatic_stage_layer"), "main");


    //VEGETATIVE
    public static final ModelLayerLocation VEG_ROLLER_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "veg_roller_layer"), "main");

    //HOMUNCULOID
    public static final ModelLayerLocation HOMUNCULUS_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "homunculus_layer"), "main");

    //PROJECTILES
    public static final ModelLayerLocation BRACHATIC_HARPOON_LAYER = new ModelLayerLocation(
            new ResourceLocation(Chemosynthesis.MODID, "brachatic_harpoon_layer"), "main");

    //MISC
}
