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
                        pOutput.accept(ModItems.BULB_HARPOON_GUN.get());

                        //Blocks
                        pOutput.accept(ModBlocks.SILICATE_BLOCK_L1.get());
                        pOutput.accept(ModBlocks.SILICATE_BLOCK_L2.get());
                        pOutput.accept(ModBlocks.SILICATE_BLOCK_L3.get());

                        pOutput.accept(ModBlocks.SILICATED_GRASS_L4.get());
                        pOutput.accept(ModBlocks.SILICATED_DIRT_L4.get());
                        pOutput.accept(ModBlocks.SILICATED_STONE_L4.get());
                        pOutput.accept(ModBlocks.SILICATED_DEEPSLATE_L4.get());
                        pOutput.accept(ModBlocks.SILICATED_LOG_L4.get());

                        //Mobs
                        pOutput.accept(ModItems.SPAWN_ROLLER.get());
                        pOutput.accept(ModItems.SPAWN_CHUNK_OF_FLESH.get());
                        pOutput.accept(ModItems.SPAWN_CLUSTER_OF_FLESH.get());


                        pOutput.accept(ModItems.SPAWN_TETH_ZOMBIE.get());
                        pOutput.accept(ModItems.SPAWN_TETH_SKELETON.get());
                        pOutput.accept(ModItems.SPAWN_TETH_COW.get());
                        pOutput.accept(ModItems.SPAWN_TETH_SHEEP.get());
                        pOutput.accept(ModItems.SPAWN_TETH_PIG.get());
                        pOutput.accept(ModItems.SPAWN_TETH_ENDERMAN.get());

                        pOutput.accept(ModItems.SPAWN_MET_ZOMBIE.get());
                        pOutput.accept(ModItems.SPAWN_MET_COW.get());

                        pOutput.accept(ModItems.SPAWN_VEG_BULB.get());
                        pOutput.accept(ModItems.SPAWN_VEG_ROLLER.get());
                        pOutput.accept(ModItems.SPAWN_VASC_ROLLER.get());

                        pOutput.accept(ModItems.SPAWN_HOMUNCULUS_T1.get());
                        pOutput.accept(ModItems.SPAWN_AMAL_ZOMBIE.get());
                        pOutput.accept(ModItems.SPAWN_AMAL_SPAWNER.get());
                        pOutput.accept(ModItems.SPAWN_AMAL_TURRET.get());
                        pOutput.accept(ModItems.SPAWN_AMAL_RADAR.get());
                        pOutput.accept(ModItems.SPAWN_AMAL_CONVERTER.get());

                        pOutput.accept(ModItems.SPAWN_THROMBOCYTE.get());
                        pOutput.accept(ModItems.SPAWN_ERYTHROCYTE.get());
                        pOutput.accept(ModItems.SPAWN_ASTROCYTE.get());

                        pOutput.accept(ModItems.SPAWN_PERFOCYTE.get());

                        //Corpses
                        pOutput.accept(ModBlocks.BIOMUSH.get());
                        pOutput.accept(ModBlocks.FLESH_PILE.get());
                        pOutput.accept(ModBlocks.CORPSE_COW.get());
                        pOutput.accept(ModBlocks.CORPSE_PIG.get());

                        pOutput.accept(ModBlocks.AMALGAMATED_FLESH_BLOCK.get());
                        pOutput.accept(ModBlocks.VEIN_BLOCK.get());
                        pOutput.accept(ModBlocks.TENDRILS.get());
                        pOutput.accept(ModBlocks.SULFURED_TENDRILS.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
