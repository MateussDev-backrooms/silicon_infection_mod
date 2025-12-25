package com.mateussdev.chemosyntehsis.Entities.gibs.flesh_gib;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseGib;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class GibFlesh extends BaseGib {
    public GibFlesh(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }
}
