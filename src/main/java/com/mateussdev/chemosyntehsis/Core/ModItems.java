package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Items.AtmosphereAnalyzer;
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

    //Spawn eggs
    //Pure
    public static final RegistryObject<Item> SPAWN_ROLLER =
            ITEMS.register("spawn_roller",
                    () -> new ForgeSpawnEggItem(ModEntities.SILICON_ROLLER, 0x967445, 0x7c4b45, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_CHUNK_OF_FLESH =
            ITEMS.register("spawn_chunk_of_flesh",
                    () -> new ForgeSpawnEggItem(ModEntities.CHUNK_OF_FLESH, 0x967445, 0x7c4b45, new Item.Properties()));

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

    //Vegetated mobs
    public static final RegistryObject<Item> SPAWN_VEG_BULB =
            ITEMS.register("spawn_veg_bulb",
                    () -> new ForgeSpawnEggItem(ModEntities.VEG_BULB, 0x22201f, 0x855d36, new Item.Properties()));

    //Hybrids

    //Tier 1
    public static final RegistryObject<Item> SPAWN_THROMBOCYTE =
            ITEMS.register("spawn_thrombocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.THROMBOCYTE, 0xc66f5b, 0x7b4842, new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_ERYTHROCYTE =
            ITEMS.register("spawn_erythrocyte",
                    () -> new ForgeSpawnEggItem(ModEntities.ERYTHROCYTE, 0xc66f5b, 0x7b4842, new Item.Properties()));

    //===== DEFINE ITEMS HERE =====//

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
