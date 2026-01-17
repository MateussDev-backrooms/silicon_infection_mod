package com.mateussdev.chemosyntehsis.Datagen.loot;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
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
                block -> createOreDrop(ModBlocks.MUSHY_SILICON_BLOCK.get(), ModItems.SILICON.get()));
        this.add(ModBlocks.AMALGAMATED_FLESH_BLOCK.get(),
                block -> createOreDrop(ModBlocks.AMALGAMATED_FLESH_BLOCK.get(), Items.ROTTEN_FLESH));

        this.dropSelf(ModBlocks.VEIN_BLOCK.get());
        this.dropWhenSilkTouch(ModBlocks.BIOMUSH.get());
        this.dropWhenSilkTouch(ModBlocks.FLESH_PILE.get());
        this.dropWhenSilkTouch(ModBlocks.TENDRILS.get());
        this.dropWhenSilkTouch(ModBlocks.SULFURED_TENDRILS.get());

        //Corpses
        this.add(ModBlocks.CORPSE_COW.get(),
                block -> createOreDrop(ModBlocks.CORPSE_COW.get(), Items.ROTTEN_FLESH));
        this.add(ModBlocks.CORPSE_PIG.get(),
                block -> createOreDrop(ModBlocks.CORPSE_PIG.get(), Items.ROTTEN_FLESH));
    }

    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .toList();
    }
}
