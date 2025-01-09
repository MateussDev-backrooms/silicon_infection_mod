package com.mateussdev.chemosyntehsis.Entities.teth_zombie;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class TethZombie_Model extends GeoModel<TethZombie> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/teth_zombie.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/teth_zombie.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/teth_zombie.animation.json");

    @Override
    public ResourceLocation getModelResource(TethZombie object) { return model; }

    @Override
    public ResourceLocation getTextureResource(TethZombie object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(TethZombie object) { return animation; }
}
