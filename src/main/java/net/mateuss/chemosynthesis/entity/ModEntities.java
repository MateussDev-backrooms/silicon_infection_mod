package net.mateuss.chemosynthesis.entity;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chemosynthesis.MODID);

    public static final RegistryObject<EntityType<EntitySiliconRoller>> SILICON_ROLLER =
            ENTITY_TYPES.register("silicon_roller", () -> EntityType.Builder.of(EntitySiliconRoller::new, MobCategory.MONSTER)
                    .sized(1f, 1f).build("silicon_roller"));

    public static final RegistryObject<EntityType<EntitySiliconTripod>> SILICON_TRIPOD =
            ENTITY_TYPES.register("silicon_tripod", () -> EntityType.Builder.of(EntitySiliconTripod::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f).build("silicon_tripod"));

    public static final RegistryObject<EntityType<EntityTethZombie>> TETH_ZOMBIE =
            ENTITY_TYPES.register("teth_zombie", () -> EntityType.Builder.of(EntityTethZombie::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_zombie"));

    public static final RegistryObject<EntityType<EntityTethCow>> TETH_COW =
            ENTITY_TYPES.register("teth_cow", () -> EntityType.Builder.of(EntityTethCow::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_cow"));

    public static final RegistryObject<EntityType<EntityTethSheep>> TETH_SHEEP =
            ENTITY_TYPES.register("teth_sheep", () -> EntityType.Builder.of(EntityTethSheep::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_sheep"));


    public static final RegistryObject<EntityType<EntityHomunculus>> HOMUNCULUS_HEART =
            ENTITY_TYPES.register("homunculus_heart", () -> EntityType.Builder.of(EntityHomunculus::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("homunculus_heart"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
