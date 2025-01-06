package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chemosynthesis.MODID);

    //===== DEFINE ENTITIES HERE =====//



    //===== DEFINE ENTITIES HERE =====//

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
