package com.mateussdev.chemosyntehsis.Entities.silicon_roller;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;

public class SiliconRoller_Model extends GeoModel<SiliconRoller> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/silicon_roller.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/silicon_roller.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/silicon_roller.animation.json");

    @Override
    public ResourceLocation getModelResource(SiliconRoller object) { return model; }

    @Override
    public ResourceLocation getTextureResource(SiliconRoller object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(SiliconRoller object) { return animation; }
}
