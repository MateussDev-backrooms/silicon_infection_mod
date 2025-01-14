package com.mateussdev.chemosyntehsis.Datagen.loot;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.add(ModBlocks.SILICATE_BLOCK.get(),
                block -> createOreDrop(ModBlocks.SILICATE_BLOCK.get(), ModItems.SILICON.get()));
        this.add(ModBlocks.MUSHY_SILICON_BLOCK.get(),
                block -> createOreDrop(ModBlocks.SILICATE_BLOCK.get(), ModItems.SILICON.get()));

        this.dropSelf(ModBlocks.VEIN_BLOCK.get());
    }

    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
