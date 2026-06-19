package com.mateussdev.chemosyntehsis.Util.Models;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.AbstractHarpoonProjectile;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StemModel extends GeoModel<AbstractHarpoonProjectile> {
    @Override
    public ResourceLocation getModelResource(AbstractHarpoonProjectile stemAnimatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "geo/other/stem.geo.json");
    }
    @Override
    public ResourceLocation getTextureResource(AbstractHarpoonProjectile stemAnimatable) {
        return new ResourceLocation(Chemosynthesis.MODID, "/entity/stem.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractHarpoonProjectile stemAnimatable) {
        return null;
    }
}
