package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.teth_zombie.TethZombie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chemosynthesis.MODID);

    //===== DEFINE ENTITIES HERE =====//

    public static final RegistryObject<EntityType<SiliconRoller>> SILICON_ROLLER =
            ENTITIES.register("silicon_roller", () -> EntityType.Builder.of(SiliconRoller::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("silicon_roller"));

    public static final RegistryObject<EntityType<TethZombie>> TETH_ZOMBIE =
            ENTITIES.register("teth_zombie", () -> EntityType.Builder.of(TethZombie::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .build("teth_zombie"));

    //===== DEFINE ENTITIES HERE =====//

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
