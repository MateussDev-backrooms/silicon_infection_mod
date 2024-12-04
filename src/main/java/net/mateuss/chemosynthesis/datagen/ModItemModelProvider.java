package net.mateuss.chemosynthesis.datagen;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.item.ModItems;
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
        simpleItem(ModItems.SILICON_DETECTOR);

        withExistingParent(ModItems.SPAWN_SILICON_ROLLER.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_SILICON_TRIPOD.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_ZOMBIE.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_COW.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_SHEEP.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_TETH_PIG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.SPAWN_HOMUNCULUS_HEART.getId().getPath(), mcLoc("item/template_spawn_egg"));

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(Chemosynthesis.MODID,"item/" + item.getId().getPath()));
    }
}
