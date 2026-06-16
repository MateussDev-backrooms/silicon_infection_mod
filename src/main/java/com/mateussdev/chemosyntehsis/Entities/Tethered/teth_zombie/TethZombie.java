package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.met_zombie.MetZombie;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.server.level.ServerLevel;
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

    // ===== Entity setup and stats ===== //
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                //Basics
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
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
                model.getBone("appendage2").get(),
                model.getBone("appendage3").get(),
                model.getBone("appendage4").get(),
                model.getBone("appendage6").get(),
                model.getBone("appendage7").get(),
                model.getBone("appendage8").get(),
                model.getBone("appendage9").get()
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
    public int getBulbCount() {
        return 7;
    }

    @Override
    public void evolve() {
        if (this.level() instanceof ServerLevel slvl) {
            MetZombie metZombie = ModEntities.MET_ZOMBIE.get().create(slvl);
            metZombie.moveTo(blockPosition().getCenter());
            slvl.addFreshEntity(metZombie);
            StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
            this.discard();

        }
    }


}
