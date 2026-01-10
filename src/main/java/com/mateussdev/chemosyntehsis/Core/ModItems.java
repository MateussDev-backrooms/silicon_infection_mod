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

    //Metabolized mobs
    public static final RegistryObject<Item> SPAWN_MET_ZOMBIE =
            ITEMS.register("spawn_met_zombie",
                    () -> new ForgeSpawnEggItem(ModEntities.MET_ZOMBIE, 0x3d1411, 0x7c4b45, new Item.Properties()));

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
    public static final RegistryObject<Item> SPAWN_AMAL_ZOMBIE =
            ITEMS.register("spawn_amal_zombie",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_ZOMBIE, 0x6b221c, 0x583451, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_AMAL_SPAWNER =
            ITEMS.register("spawn_amal_spawner",
                    () -> new ForgeSpawnEggItem(ModEntities.AMAL_SPAWNER, 0x6b221c, 0x583451, new Item.Properties()));

    //Hybrids

    //Tier 1
    public static final RegistryObject<Item> SPAWN_THROMBOCYTE =
            ITEMS.register("spawn_thrombocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.THROMBOCYTE, 0xc66f5b, 0x7b4842, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_ERYTHROCYTE =
            ITEMS.register("spawn_erythrocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.ERYTHROCYTE, 0xc66f5b, 0x7b4842, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_ASTROCYTE =
            ITEMS.register("spawn_astrocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.ASTROCYTE, 0xc66f5b, 0x7b4842, new Item.Properties()));

    //===== DEFINE ITEMS HERE =====//

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
