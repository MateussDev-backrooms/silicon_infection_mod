package net.Mateuss.Chemosynthesis.datagen;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.core.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
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

//        simpleBlockWithItem(ModBlocks.SILICON_VEIN_BLOCK.get(), cubeAll(ModBlocks.SILICON_VEIN_BLOCK.get()));

        blockWithItem(ModBlocks.HOM_VEIN_BUILDER_BLOCK);
        blockWithItem(ModBlocks.HOM_TRIPOD_HATCHER);
        blockWithItem(ModBlocks.HOM_ROOT_VAUCOLE);
//        blockWithItem(ModBlocks.TENDRIL_BLOCK);
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
