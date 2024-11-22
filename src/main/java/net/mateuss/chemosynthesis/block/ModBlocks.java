package net.mateuss.chemosynthesis.block;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.block.advanced.ExplosiveBlobBlock;
import net.mateuss.chemosynthesis.block.advanced.SiliconVeinBlock;
import net.mateuss.chemosynthesis.block.advanced.SiliconVeinBuilder;
import net.mateuss.chemosynthesis.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Chemosynthesis.MODID);


    public static final RegistryObject<Block> SILICATE_BLOCK = registerBlock("silicate_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> SILICATE_TENDRIL_BLOCK = registerBlock("silicate_tendril_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)));
    public static final RegistryObject<Block> SILICATE_EXPLOSIVE_BLOCK = registerBlock("silicate_explosive_block",
            () -> new ExplosiveBlobBlock(BlockBehaviour.Properties.copy(Blocks.DRIPSTONE_BLOCK)));

    public static final RegistryObject<Block> SILICON_VEIN_BLOCK = registerBlock("silicon_vein",
            () -> new SiliconVeinBlock(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()));

    public static final RegistryObject<Block> SILICON_VEIN_BUILDER_BLOCK = registerBlock("silicon_vein_builder",
            () -> new SiliconVeinBuilder(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()));


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
