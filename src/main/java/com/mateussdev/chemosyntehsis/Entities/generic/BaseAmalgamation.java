package com.mateussdev.chemosyntehsis.Entities.generic;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class BaseAmalgamation extends BaseOrganelle{
    protected BaseAmalgamation(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
