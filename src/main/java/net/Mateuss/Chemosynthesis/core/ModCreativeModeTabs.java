package net.Mateuss.Chemosynthesis.core;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Chemosynthesis.MODID);

    public static final RegistryObject<CreativeModeTab> SILICON_TAB = CREATIVE_MODE_TABS.register("silicon_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.SILICON.get()))
                    .title(Component.translatable("creativetab.silicon_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.SILICON.get());
                        pOutput.accept(ModBlocks.SILICATE_BLOCK.get());
                        pOutput.accept(ModBlocks.SILICATE_TENDRIL_BLOCK.get());
                        pOutput.accept(ModBlocks.SILICATE_EXPLOSIVE_BLOCK.get());

                        pOutput.accept(ModBlocks.HOORGANELLE_CONNECTOR.get());

                        pOutput.accept(ModBlocks.TENDRIL_BLOCK.get());


                        pOutput.accept(ModItems.SILICON_DETECTOR.get());

                        pOutput.accept(ModItems.SPAWN_SILICON_TRIPOD.get());
                        pOutput.accept(ModItems.SPAWN_SILICON_ROLLER.get());
                        pOutput.accept(ModItems.SPAWN_SILIPEDE.get());

                        pOutput.accept(ModItems.SPAWN_VEG_ROLLER.get());


                        pOutput.accept(ModItems.SPAWN_TETH_ZOMBIE.get());
                        pOutput.accept(ModItems.SPAWN_TETH_SHEEP.get());
                        pOutput.accept(ModItems.SPAWN_TETH_COW.get());
                        pOutput.accept(ModItems.SPAWN_TETH_PIG.get());

                        pOutput.accept(ModItems.SPAWN_BRACHATIC_STAGE.get());

                        pOutput.accept(ModItems.SPAWN_HOMUNCULUS_HEART.get());
                        pOutput.accept(ModItems.SPAWN_HOMUNCULUS_VAUCOLE.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
