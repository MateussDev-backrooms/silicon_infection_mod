package com.mateussdev.chemosyntehsis.Entities.generic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class BaseHybrid extends BaseSiliconite{
    protected BaseHybrid(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.discardOnTether = false;
        this.tetherChance = 0.5f;
    }

    @Override
    public GeoBone[] getBulbsArray(GeoModel<?> model) {
        return new GeoBone[0];
    }

}
