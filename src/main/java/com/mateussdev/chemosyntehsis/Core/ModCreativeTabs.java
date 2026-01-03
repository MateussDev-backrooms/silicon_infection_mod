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
                        //Items
                        pOutput.accept(ModItems.SILICON.get());
                        pOutput.accept(ModItems.ATMOSPHERE_ANALYZER.get());

                        //Mobs
                        pOutput.accept(ModItems.SPAWN_ROLLER.get());
                        pOutput.accept(ModItems.SPAWN_CHUNK_OF_FLESH.get());
                        pOutput.accept(ModItems.SPAWN_CLUSTER_OF_FLESH.get());

                        pOutput.accept(ModItems.SPAWN_TETH_ZOMBIE.get());
                        pOutput.accept(ModItems.SPAWN_TETH_SKELETON.get());
                        pOutput.accept(ModItems.SPAWN_TETH_COW.get());

                        pOutput.accept(ModItems.SPAWN_MET_ZOMBIE.get());

                        pOutput.accept(ModItems.SPAWN_VEG_BULB.get());
                        pOutput.accept(ModItems.SPAWN_VEG_ROLLER.get());
                        pOutput.accept(ModItems.SPAWN_VASC_ROLLER.get());

                        pOutput.accept(ModItems.SPAWN_THROMBOCYTE.get());
                        pOutput.accept(ModItems.SPAWN_ERYTHROCYTE.get());
                        pOutput.accept(ModItems.SPAWN_ASTROCYTE.get());

                        //Blocks
                        pOutput.accept(ModBlocks.SILICATE_BLOCK.get());
                        pOutput.accept(ModBlocks.MUSHY_SILICON_BLOCK.get());
                        pOutput.accept(ModBlocks.VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.TENDRILS.get());
                        pOutput.accept(ModBlocks.SULFURED_TENDRILS.get());

                        //Corpses
                        pOutput.accept(ModBlocks.BIOMUSH.get());
                        pOutput.accept(ModBlocks.CORPSE_COW.get());
                        pOutput.accept(ModBlocks.CORPSE_PIG.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
