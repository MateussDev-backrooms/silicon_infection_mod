package net.mateuss.chemosynthesis.item;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.ModEntities;
import net.mateuss.chemosynthesis.item.advanced.SiliconStageDetector;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Chemosynthesis.MODID);

    public static final RegistryObject<Item> SILICON = ITEMS.register("silicon",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SILICON_DETECTOR = ITEMS.register("silicon_detector",
            () -> new SiliconStageDetector(new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_SILICON_ROLLER = ITEMS.register("spawn_silicon_roller",
            () -> new ForgeSpawnEggItem(ModEntities.SILICON_ROLLER, 0x7b3c32, 0x553531,
                new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_SILICON_TRIPOD = ITEMS.register("spawn_silicon_tripod",
            () -> new ForgeSpawnEggItem(ModEntities.SILICON_TRIPOD, 0x7b3c32, 0x7b4657,
                    new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_SILIPEDE = ITEMS.register("spawn_silipede",
            () -> new ForgeSpawnEggItem(ModEntities.SILIPEDE, 0x7b3c32, 0x553531,
                    new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_ZOMBIE = ITEMS.register("spawn_teth_zombie",
            () -> new ForgeSpawnEggItem(ModEntities.TETH_ZOMBIE, 0x00cccc, 0x8b4d39,
                    new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_COW = ITEMS.register("spawn_teth_cow",
            () -> new ForgeSpawnEggItem(ModEntities.TETH_COW, 0x4b3616, 0x8b4d39,
                    new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_SHEEP = ITEMS.register("spawn_teth_sheep",
            () -> new ForgeSpawnEggItem(ModEntities.TETH_SHEEP, 0xf4ebdd, 0x8b4d39,
                    new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_TETH_PIG = ITEMS.register("spawn_teth_pig",
            () -> new ForgeSpawnEggItem(ModEntities.TETH_PIG, 0xf19e98, 0x8b4d39,
                    new Item.Properties()));

    public static final RegistryObject<Item> SPAWN_HOMUNCULUS_HEART = ITEMS.register("spawn_homunculus_heart",
            () -> new ForgeSpawnEggItem(ModEntities.HOMUNCULUS_HEART, 0x8b4d39, 0x8b2132,
                    new Item.Properties()));
    public static final RegistryObject<Item> SPAWN_BRACHATIC_STAGE = ITEMS.register("spawn_brachatic_stage",
            () -> new ForgeSpawnEggItem(ModEntities.BRACHATIC_STAGE, 0x8b4d39, 0x7b4657,
                    new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
