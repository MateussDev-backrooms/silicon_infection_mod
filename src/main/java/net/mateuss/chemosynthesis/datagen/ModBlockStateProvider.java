package net.mateuss.chemosynthesis.datagen;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.block.ModBlocks;
import net.mateuss.chemosynthesis.block.advanced.SiliconVeinBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Chemosynthesis.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.SILICATE_BLOCK);
        blockWithItem(ModBlocks.SILICATE_EXPLOSIVE_BLOCK);
        blockWithItem(ModBlocks.SILICATE_TENDRIL_BLOCK);

        simpleBlockWithItem(ModBlocks.SILICON_VEIN_BLOCK.get(), cubeAll(ModBlocks.SILICON_VEIN_BLOCK.get()));

        blockWithItem(ModBlocks.SILICON_VEIN_BUILDER_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
