package net.Mateuss.Chemosynthesis.core;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.block.*;
import net.Mateuss.Chemosynthesis.block.homulculus.SiliconTripodHatcher;
import net.Mateuss.Chemosynthesis.block.homulculus.SiliconRootVaucole;
import net.Mateuss.Chemosynthesis.block.homulculus.SiliconVeinBlock;
import net.Mateuss.Chemosynthesis.block.homulculus.SiliconVeinBuilder;
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
    public static final RegistryObject<Block> HOM_VEIN_BLOCK = registerBlock("hom_vein",
            () -> new SiliconVeinBlock(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()));
    public static final RegistryObject<Block> HOM_VEIN_BUILDER_BLOCK = registerBlock("hom_vein_builder",
            () -> new SiliconVeinBuilder(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()));
    public static final RegistryObject<Block> HOM_TRIPOD_HATCHER = registerBlock("hom_tripod_hatcher",
            () -> new SiliconTripodHatcher(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()));
    public static final RegistryObject<Block> HOM_ROOT_VAUCOLE = registerBlock("hom_root_vaucole",
            () -> new SiliconRootVaucole(BlockBehaviour.Properties
                    .copy(Blocks.DRIPSTONE_BLOCK)
                    .noOcclusion()));

    //MISC BLOCKS
    public static final RegistryObject<Block> TENDRIL_BLOCK = registerBlock("tendril_block",
            () -> new TendrilBlock(BlockBehaviour.Properties
                    .copy(Blocks.GLOW_LICHEN)
                    .speedFactor(0.33f)
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