package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.world.item.Item;
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

    //===== DEFINE ITEMS HERE =====//

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
