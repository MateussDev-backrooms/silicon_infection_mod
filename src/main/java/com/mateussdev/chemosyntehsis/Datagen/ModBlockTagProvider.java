package com.mateussdev.chemosyntehsis.Datagen;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
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

                ;
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)

                ;
        this.tag(BlockTags.MINEABLE_WITH_HOE)

                ;
        this.tag(BlockTags.CAVE_VINES)

                ;
        this.tag(BlockTags.NEEDS_IRON_TOOL)

                ;
    }
}
