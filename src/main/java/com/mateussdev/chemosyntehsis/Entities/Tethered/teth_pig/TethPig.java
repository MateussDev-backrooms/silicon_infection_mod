package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_pig;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class TethPig extends BaseTethered {
    public TethPig(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.bulbCount = 7;
    }

    // ===== Entity setup and stats ===== //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                //Basics
                .add(Attributes.MAX_HEALTH, 18D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0d)
                //Attack
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D)
                //Armor
                .add(Attributes.ARMOR, 0d)
                .add(Attributes.ARMOR_TOUGHNESS, 0D)

                ;
    }

    // ===== Bulb setup ===== //
    private boolean hasScrambled = false;
    private GeoBone[] scrambled_bulbs = {};

    @Override
    public GeoBone[] getBulbsArray(GeoModel<?> model) {

        GeoBone[] bulbs = {
                model.getBone("appendage1").get(),
                model.getBone("appendage2").get(),
                model.getBone("appendage3").get(),
                model.getBone("appendage4").get(),
                model.getBone("appendage5").get(),
                model.getBone("appendage6").get(),
                model.getBone("appendage7").get(),
        };

        if(hasScrambled) {
            return scrambled_bulbs;
        } else {
            scrambled_bulbs = StaticSiliconiteMethods.scrambleBones(bulbs);
            hasScrambled = true;
            return scrambled_bulbs;
        }
    }

    @Override
    public void evolve() {

    }

}
