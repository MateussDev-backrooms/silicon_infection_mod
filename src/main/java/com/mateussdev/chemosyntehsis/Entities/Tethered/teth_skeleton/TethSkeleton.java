package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TethSkeleton extends BaseTethered implements RangedAttackMob {
    public TethSkeleton(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.explodeOnDeath = false;
        this.bulbCount = 8;
    }

    // ===== Entity setup and stats ===== //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                //Basics
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0d)
                //Attack
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D)
                //Armor
                .add(Attributes.ARMOR, 0d)
                .add(Attributes.ARMOR_TOUGHNESS, 0D);
    }

    @Override
    public void registerDefaultGoals() {
        //Stop all goals
        this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
        this.targetSelector.getRunningGoals().forEach(WrappedGoal::stop);

        //Remove all goals
        this.goalSelector.removeAllGoals(g -> true);
        this.targetSelector.removeAllGoals(g -> true);

        //Avoid water (No float task cuz they are immune to water damage)
        this.goalSelector.addGoal(0, new RangedAttackGoal(this, 1.5f, 40, 10));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        // - TARGETS
        this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float dst_f) {
        if(!this.canBeSeenByAnyone()) return;
        BulbProjectileEntity bulbProjectile = new BulbProjectileEntity(this.level(), this);

        bulbProjectile.setPos(this.getX(), this.getEyeY(), this.getZ());

        // Calculate direction
        double dx = target.getX() - this.getX();
        double dy = target.getEyeY() - bulbProjectile.getY();
        double dz = target.getZ() - this.getZ();

        float velocity = 1.6f;
        float spread = 0.3f;

        // Shoot
        bulbProjectile.shoot(dx, dy, dz, velocity, spread);

        // Recoil

        double length = Math.sqrt(dx * dx + dz * dz);
        double dx_n = 0;
        double dz_n = 0;
        if (length != 0) {
            dx_n = dx/length;
            dz_n = dz/length;
        }

        float strength = 0.3F;
        this.push(-dx_n * strength, 0.1F, -dz_n * strength);

        // Post shoot
        this.hurt(this.damageSources().generic(), 1f);
        this.level().addFreshEntity(bulbProjectile);
        this.level().playSound(null, this.blockPosition(), SoundEvents.MUD_BREAK, SoundSource.HOSTILE, 1.0F, 1.0F);
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
                model.getBone("appendage5").get(),
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
}
