package net.Mateuss.Chemosynthesis.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectileBulb extends AbstractArrow {
    public ProjectileBulb(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected ItemStack getPickupItem() {return null;}
}
