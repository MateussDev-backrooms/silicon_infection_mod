package com.mateussdev.chemosyntehsis.Entities.generics;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class EntityBaseTethered extends EntityBaseSiliconite{
    protected EntityBaseTethered(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }
}
