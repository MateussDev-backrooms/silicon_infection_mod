package net.Mateuss.Chemosynthesis.datagen.loot;

import net.Mateuss.Chemosynthesis.core.ModBlocks;
import net.Mateuss.Chemosynthesis.core.ModItems;
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
        this.add(ModBlocks.SILICATE_TENDRIL_BLOCK.get(),
                block -> createOreDrop(ModBlocks.SILICATE_BLOCK.get(), ModItems.SILICON.get()));

        this.dropSelf(ModBlocks.SILICATE_EXPLOSIVE_BLOCK.get());

        this.dropWhenSilkTouch(ModBlocks.TENDRIL_BLOCK.get());
        this.dropWhenSilkTouch(ModBlocks.HOORGANELLE_CONNECTOR.get());
    }

    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
