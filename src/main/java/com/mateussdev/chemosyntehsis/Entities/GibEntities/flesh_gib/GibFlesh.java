package com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseGib;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class GibFlesh extends BaseGib {


    public GibFlesh(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
}
