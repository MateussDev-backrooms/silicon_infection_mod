package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Chemosynthesis.MODID);

    public static final RegistryObject<CreativeModeTab> SILICON_TAB = CREATIVE_MODE_TABS.register("silicon_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SILICON.get()))
                    .title(Component.translatable("creativetab.silicon_tab"))
                    .displayItems((pParameters, pOutput) -> {

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
