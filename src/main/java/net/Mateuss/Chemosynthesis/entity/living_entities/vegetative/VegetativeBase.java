package net.Mateuss.Chemosynthesis.entity.living_entities.vegetative;

import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class VegetativeBase extends SiliconiteBase {
    protected VegetativeBase(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //make knockback zero
    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        this.setDeltaMovement(0, 0, 0);
    }
}
