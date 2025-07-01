package com.mateussdev.chemosyntehsis.Datagen;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Chemosynthesis.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SILICATE_BLOCK);
        blockWithItem(ModBlocks.MUSHY_SILICON_BLOCK);
        simpleBlockWithItem(ModBlocks.VEIN_BLOCK.get(), cubeAll(ModBlocks.VEIN_BLOCK.get()));
        simpleBlockWithItem(ModBlocks.TENDRILS.get(), cubeAll(ModBlocks.TENDRILS.get()));
        simpleBlockWithItem(ModBlocks.SULFURED_TENDRILS.get(), cubeAll(ModBlocks.SULFURED_TENDRILS.get()));



        simpleBlock(ModBlocks.BIOMUSH.get());
        horizontalBlock(ModBlocks.CORPSE_COW.get(), new ModelFile.UncheckedModelFile(modLoc("block/cow_corpse")));
        horizontalBlock(ModBlocks.CORPSE_PIG.get(), new ModelFile.UncheckedModelFile(modLoc("block/pig_corpse")));
        //blockWithItem(ModBlocks.TENDRIL_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
