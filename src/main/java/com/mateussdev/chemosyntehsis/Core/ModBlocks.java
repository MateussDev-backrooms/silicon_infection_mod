package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Blocks.*;
import com.mateussdev.chemosyntehsis.Blocks.corpses.CowCorpse;
import com.mateussdev.chemosyntehsis.Blocks.corpses.PigCorpse;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.world.entity.animal.Cow;
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
    public static final RegistryObject<Block> AMALGAMATED_FLESH_BLOCK = registerBlock("amalgamated_flesh_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<VeinBlock> VEIN_BLOCK = registerBlock("vein_block",
            () -> new VeinBlock(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<TendrilBlock> TENDRILS = registerBlock("tendrils",
            () -> new TendrilBlock(BlockBehaviour.Properties.copy(Blocks.GLOW_LICHEN).lightLevel((i) -> 0)
                    .requiresCorrectToolForDrops().noOcclusion()));
    public static final RegistryObject<SulfuredTendrilBlock> SULFURED_TENDRILS = registerBlock("sulfured_tendrils",
            () -> new SulfuredTendrilBlock(BlockBehaviour.Properties.copy(Blocks.GLOW_LICHEN).lightLevel((i) -> 0)
                    .requiresCorrectToolForDrops().noOcclusion()));


    public static final RegistryObject<BiomushBlock> BIOMUSH = registerBlock("biomush",
            () -> new BiomushBlock(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .noOcclusion()));
    public static final RegistryObject<FleshPileBlock> FLESH_PILE = registerBlock("flesh_pile",
            () -> new FleshPileBlock(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .noOcclusion()));

    public static final RegistryObject<CowCorpse> CORPSE_COW = registerBlock("cow_corpse",
            () -> new CowCorpse(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .noOcclusion()));
    public static final RegistryObject<PigCorpse> CORPSE_PIG = registerBlock("pig_corpse",
            () -> new PigCorpse(BlockBehaviour.Properties.copy(Blocks.MUD)
                    .noOcclusion()));

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
