package net.Mateuss.Chemosynthesis.datagen;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.core.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Chemosynthesis.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.SILICATE_BLOCK.get())
                .add(ModBlocks.SILICATE_EXPLOSIVE_BLOCK.get())
                .add(ModBlocks.SILICATE_TENDRIL_BLOCK.get())
                .add(ModBlocks.HOORGANELLE_CONNECTOR.get())
                .add(ModBlocks.TENDRIL_BLOCK.get())
        ;
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SILICATE_BLOCK.get())
                .add(ModBlocks.SILICATE_EXPLOSIVE_BLOCK.get())
                .add(ModBlocks.SILICATE_TENDRIL_BLOCK.get())
                .add(ModBlocks.HOORGANELLE_CONNECTOR.get())
        ;
        this.tag(BlockTags.MINEABLE_WITH_HOE)
                .add(ModBlocks.TENDRIL_BLOCK.get());
        this.tag(BlockTags.CAVE_VINES)
                .add(ModBlocks.TENDRIL_BLOCK.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL)
        ;
    }
}
