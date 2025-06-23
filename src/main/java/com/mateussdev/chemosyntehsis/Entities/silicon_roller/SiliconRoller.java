package com.mateussdev.chemosyntehsis.Entities.silicon_roller;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

public class SiliconRoller extends BaseSiliconite {
    public SiliconRoller(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    protected boolean destructiveTether() {
        return true;
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 12D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 4D);
    }

    public int deathTime = 0;
    @Override
    protected void tickDeath() {
        ++deathTime;

        if (deathTime == 20 && !level().isClientSide) {
            explodeIntoBulbs();
            spawnBloodBurst();
            level().playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
        }
    }

    public void explodeIntoBulbs() {
        for (int i = 0; i < getBulbCount()-getBrokenOffBulbs(); i++) {
            BulbProjectileEntity shard = new BulbProjectileEntity(level(),this);
            shard.shoot(
                    level().random.triangle(0, 1),
                    level().random.triangle(0.2, 1),
                    level().random.triangle(0, 1),
                    1.2f, // speed
                    10.0f // inaccuracy
            );
            level().addFreshEntity(shard);
        }
    }

    private void spawnBloodBurst() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        DustParticleOptions blood = new DustParticleOptions(
                new Vector3f(0.8f, 0.2f, 0.0f),
                3.0f
        );

        serverLevel.sendParticles(
                blood,
                this.getX(),
                this.getY() + 1.0,
                this.getZ(),
                30,
                0.3,
                0.5,
                0.3,
                0.1
        );
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
                model.getBone("appendage8").get(),
                model.getBone("appendage9").get(),
                model.getBone("appendage10").get(),
                model.getBone("appendage11").get(),
                model.getBone("appendage12").get(),
                model.getBone("appendage13").get(),
                model.getBone("appendage14").get(),
                model.getBone("appendage15").get(),
                model.getBone("appendage16").get(),
                model.getBone("appendage17").get(),
                model.getBone("appendage18").get()
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
        return 18;
    }
}
