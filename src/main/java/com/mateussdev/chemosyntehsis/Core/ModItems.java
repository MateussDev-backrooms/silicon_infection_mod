package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Items.AtmosphereAnalyzer;
import com.mateussdev.chemosyntehsis.Items.BulbHarpoonGun;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Chemosynthesis.MODID);

    //===== DEFINE ITEMS HERE =====//

    public static final RegistryObject<Item> SILICON =
            ITEMS.register("silicon", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ATMOSPHERE_ANALYZER =
            ITEMS.register("atmosphere_analyzer", () -> new AtmosphereAnalyzer(new Item.Properties()));

    public static final RegistryObject<Item> BULB_HARPOON_GUN =
            ITEMS.register("bulb_harpoon_gun", () -> new BulbHarpoonGun(new Item.Properties()));

    //Spawn eggs
    //Pure
    public static final RegistryObject<Item> SPAWN_ROLLER =
            ITEMS.register("spawn_roller",
                    () -> new ForgeSpawnEggItem(ModEntities.SILICON_ROLLER, 0x967445, 0x7c4b45, new Item.Properties()));


    public static final RegistryObject<Item> SPAWN_CHUNK_OF_FLESH =
            ITEMS.register("spawn_chunk_of_flesh",
                    () -> new ForgeSpawnEggItem(ModEntities.CHUNK_OF_FLESH, 0x823431, 0x2e2b2a, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_CLUSTER_OF_FLESH =
            ITEMS.register("spawn_cluster_of_flesh",
                    () -> new ForgeSpawnEggItem(ModEntities.CLUSTER_OF_FLESH, 0x823431, 0x2e2b2a, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_GENOME_CARRIER =
            ITEMS.register("spawn_genome_carrier",
                    () -> new ForgeSpawnEggItem(ModEntities.GENOME_CARRIER, 0xD3AD4D, 0xC345B8, new Item.Properties()));

    //Tethered mobs
    public static final RegistryObject<Item> SPAWN_TETH_ZOMBIE =
            ITEMS.register("spawn_teth_zombie",
                    () -> new ForgeSpawnEggItem(ModEntities.TETH_ZOMBIE, 0x967445, 0x7c4b45, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_SKELETON =
            ITEMS.register("spawn_teth_skeleton",
                    () -> new ForgeSpawnEggItem(ModEntities.TETH_SKELETON, 0x967445, 0x7c4b45, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_COW =
            ITEMS.register("spawn_teth_cow",
                    () -> new ForgeSpawnEggItem(ModEntities.TETH_COW, 0x967445, 0x7c4b45, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_ENDERMAN =
            ITEMS.register("spawn_teth_enderman",
                    () -> new ForgeSpawnEggItem(ModEntities.TETH_ENDERMAN, 0x967445, 0x7c4b45, new Item.Properties()));

    //Metabolized mobs
    public static final RegistryObject<Item> SPAWN_MET_ZOMBIE =
            ITEMS.register("spawn_met_zombie",
                    () -> new ForgeSpawnEggItem(ModEntities.MET_ZOMBIE, 0x4e160d, 0x855421, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_MET_COW =
            ITEMS.register("spawn_met_cow",
                    () -> new ForgeSpawnEggItem(ModEntities.MET_COW, 0x4e160d, 0x855421, new Item.Properties()));

    //Vegetated mobs
    public static final RegistryObject<Item> SPAWN_VEG_BULB =
            ITEMS.register("spawn_veg_bulb",
                    () -> new ForgeSpawnEggItem(ModEntities.VEG_BULB, 0x22201f, 0x855d36, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_VEG_ROLLER =
            ITEMS.register("spawn_veg_roller",
                    () -> new ForgeSpawnEggItem(ModEntities.VEG_ROLLER, 0x22201f, 0x855d36, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_VASC_ROLLER =
            ITEMS.register("spawn_vasc_roller",
                    () -> new ForgeSpawnEggItem(ModEntities.VASC_ROLLER, 0x22201f, 0x855d36, new Item.Properties()));

    //Amalgamations
    public static final RegistryObject<Item> SPAWN_HOMUNCULUS_T1 =
            ITEMS.register("spawn_homunculus_t1",
                    () -> new ForgeSpawnEggItem(ModEntities.HOMUNCULUS_T1, 0x933b3b, 0x614558, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_AMAL_ZOMBIE =
            ITEMS.register("spawn_amal_zombie",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_ZOMBIE, 0x933b3b, 0x614558, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_AMAL_SPAWNER =
            ITEMS.register("spawn_amal_spawner",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_SPAWNER, 0x933b3b, 0x614558, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_AMAL_TURRET =
            ITEMS.register("spawn_amal_turret",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_TURRET, 0x933b3b, 0x614558, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_AMAL_RADAR =
            ITEMS.register("spawn_amal_radar",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_RADAR, 0x933b3b, 0x614558, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_AMAL_CONVERTER =
            ITEMS.register("spawn_amal_converter",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_CONVERTER, 0x933b3b, 0x614558, new Item.Properties()));

    //Hybrids

    //Tier 1
    public static final RegistryObject<Item> SPAWN_THROMBOCYTE =
            ITEMS.register("spawn_thrombocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.THROMBOCYTE, 0xc16c26, 0x7b4842, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_ERYTHROCYTE =
            ITEMS.register("spawn_erythrocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.ERYTHROCYTE, 0xc16c26, 0x7b4842, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_ASTROCYTE =
            ITEMS.register("spawn_astrocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.ASTROCYTE, 0xc16c26, 0x7b4842, new Item.Properties()));

    //Tier 2
    public static final RegistryObject<Item> SPAWN_PERFOCYTE =
            ITEMS.register("spawn_perfocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.PERFOCYTE, 0xc16c26, 0x745d70, new Item.Properties()));

    //===== DEFINE ITEMS HERE =====//

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
