package com.mateussdev.chemosyntehsis.Entities.generic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

public class BaseVegetated extends Monster {
    protected BaseVegetated(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        super.push(0d, 0d, 0d);
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
        super.knockback(0, 0, 0);
    }


}
