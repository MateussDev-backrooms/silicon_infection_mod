package net.Mateuss.Chemosynthesis.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProjectileBrachaticHarpoon extends AbstractHarpoonProjectile{
    public ProjectileBrachaticHarpoon(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, true, true, false);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

}
