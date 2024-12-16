package net.Mateuss.Chemosynthesis.entity.living_entities.tethered;

import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class TetheredBase extends SiliconiteBase {
    protected TetheredBase(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
}
