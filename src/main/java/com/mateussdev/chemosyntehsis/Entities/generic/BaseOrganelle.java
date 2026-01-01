package com.mateussdev.chemosyntehsis.Entities.generic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

public class BaseOrganelle extends BaseSiliconite {
    protected BaseOrganelle(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
    }

    // ===== Organelle overrides ===== //

    @Override
    protected void registerGoals() {
        // Properly override with empty implementation
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
        // Completely immobile
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
        // No knockback for stationary entities
    }

    @Override
    protected boolean isBrave() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }
}