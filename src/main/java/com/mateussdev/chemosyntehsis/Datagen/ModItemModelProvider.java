package com.mateussdev.chemosyntehsis.Datagen;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, Chemosynthesis.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.SILICON);
        simpleItem(ModItems.ATMOSPHERE_ANALYZER);

        //Spawn eggs
        withExistingParent(ModItems.SPAWN_ROLLER.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_ZOMBIE.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_SKELETON.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_COW.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_CHUNK_OF_FLESH.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_THROMBOCYTE.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_ERYTHROCYTE.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_ASTROCYTE.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_VEG_BULB.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Chemosynthesis.MODID,"item/" + item.getId().getPath()));
    }
}
