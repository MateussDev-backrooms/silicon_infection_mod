package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Blocks.VeinBlock;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Chemosynthesis.MODID);

    //===== DEFINE BLOCKS HERE =====//

    public static final RegistryObject<Block> SILICATE_BLOCK = registerBlock("silicate_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> MUSHY_SILICON_BLOCK = registerBlock("mushy_silicate_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<VeinBlock> VEIN_BLOCK = registerBlock("vein_block",
            () -> new VeinBlock(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .requiresCorrectToolForDrops()));

    //===== DEFINE BLOCKS HERE =====//

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
