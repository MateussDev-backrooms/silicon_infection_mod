package net.mateuss.chemosynthesis.datagen.loot;

import net.mateuss.chemosynthesis.block.ModBlocks;
import net.mateuss.chemosynthesis.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
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
        this.dropSelf(ModBlocks.SILICON_VEIN_BLOCK.get());
        this.dropSelf(ModBlocks.SILICON_VEIN_BUILDER_BLOCK.get());
    }

    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
