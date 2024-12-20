package net.Mateuss.Chemosynthesis.core;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.block.*;
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

    //CONVERSION/INFECTION BLOCKS
    public static final RegistryObject<Block> SILICATE_BLOCK = registerBlock("silicate_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)
                    .requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> SILICATE_TENDRIL_BLOCK = registerBlock("silicate_tendril_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)));
    public static final RegistryObject<Block> SILICATE_EXPLOSIVE_BLOCK = registerBlock("silicate_explosive_block",
            () -> new ExplosiveBlobBlock(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)));

    //HOMUNCULUS PARTS
    public static final RegistryObject<Block> HOM_VEIN_BLOCK = registerBlock("hom_vein_block",
            () -> new HomunculusVein(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()
                    .randomTicks()
            ));

    //MISC BLOCKS
    public static final RegistryObject<Block> TENDRIL_BLOCK = registerBlock("tendril_block",
            () -> new TendrilBlock(BlockBehaviour.Properties
                    .copy(Blocks.VINE)
                    .speedFactor(0.33f)
                    .noOcclusion()
                    .randomTicks()
            ));

    //MACHINES




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
