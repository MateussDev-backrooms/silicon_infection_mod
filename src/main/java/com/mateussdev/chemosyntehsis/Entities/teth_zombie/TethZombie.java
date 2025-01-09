package com.mateussdev.chemosyntehsis.Entities.teth_zombie;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class TethZombie extends BaseTethered {
    public TethZombie(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected boolean destructiveTether() {
        return false;
    }

    @Override
    protected float getTetherChance() {
        return 0.6f;
    }
}
