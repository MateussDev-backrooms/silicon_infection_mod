package com.mateussdev.chemosyntehsis.Entities.teth_skeleton;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TethSkeleton extends BaseTethered implements RangedAttackMob {
    public TethSkeleton(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected void registerGoals() {
        //Avoid water (No float task cuz they are immune to water damage)
        this.goalSelector.addGoal(0, new RangedAttackGoal(this, 1.5f, 40, 10));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        // - TARGETS
        if(shouldAlertOthersOnHurt()) {
            //get aggressive and alert
            this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[]{BaseSiliconite.class}));
        } else {
            //only get aggressive
            this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        }

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, this::shouldTargetMob));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float dst_f) {
        BulbProjectileEntity bulbProjectile = new BulbProjectileEntity(this.level(), this);

        bulbProjectile.setPos(this.getX(), this.getEyeY(), this.getZ());

        // Calculate direction
        double dx = target.getX() - this.getX();
        double dy = target.getEyeY() - bulbProjectile.getY();
        double dz = target.getZ() - this.getZ();

        float velocity = 1.6f;
        float spread = 0.3f;

        bulbProjectile.shoot(dx, dy, dz, velocity, spread);
        this.level().addFreshEntity(bulbProjectile);
        this.level().playSound(null, this.blockPosition(), SoundEvents.MUD_BREAK, SoundSource.HOSTILE, 1.0F, 1.0F);
    }
}
