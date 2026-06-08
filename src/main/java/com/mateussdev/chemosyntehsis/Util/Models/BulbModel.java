package com.mateussdev.chemosyntehsis.Util.Models;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class BulbModel extends GeoModel<GeoAnimatable> {
    @Override
    public ResourceLocation getModelResource(GeoAnimatable stemAnimatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/other/bulb.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(GeoAnimatable stemAnimatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "/util/bulb_singular.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeoAnimatable stemAnimatable) {
        return null;
    }
}
