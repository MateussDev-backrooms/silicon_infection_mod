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
        blockWithItem(ModBlocks.AMALGAMATED_FLESH_BLOCK);

        //Silicated blocks
        blockWithItem(ModBlocks.SILICATE_BLOCK_L1);
        blockWithItem(ModBlocks.SILICATE_BLOCK_L2);
        blockWithItem(ModBlocks.SILICATE_BLOCK_L3);

        //Silicated block variants
        blockWithItem(ModBlocks.SILICATED_STONE_L4);
        logBlock(ModBlocks.SILICATED_LOG_L4.get());

        simpleBlockWithItem(ModBlocks.VEIN_BLOCK.get(), cubeAll(ModBlocks.VEIN_BLOCK.get()));

        simpleBlock(ModBlocks.BIOMUSH.get(), new ModelFile.UncheckedModelFile(modLoc("block/biomush")));
        simpleBlock(ModBlocks.FLESH_PILE.get(), new ModelFile.UncheckedModelFile(modLoc("block/flesh_pile")));

        horizontalBlock(ModBlocks.CORPSE_COW.get(), new ModelFile.UncheckedModelFile(modLoc("block/cow_corpse")));
        horizontalBlock(ModBlocks.CORPSE_PIG.get(), new ModelFile.UncheckedModelFile(modLoc("block/pig_corpse")));
//        blockWithItem(ModBlocks.TENDRIL_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
