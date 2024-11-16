package net.mateuss.chemosynthesis.entity;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.EntitySiliconRoller;
import net.mateuss.chemosynthesis.entity.custom.EntitySiliconTripod;
import net.mateuss.chemosynthesis.entity.custom.EntityTethZombie;
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

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
