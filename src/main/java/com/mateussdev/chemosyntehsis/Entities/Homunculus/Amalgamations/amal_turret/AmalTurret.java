package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_turret;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class AmalTurret extends BaseAmalgamation implements RangedAttackMob {
    public AmalTurret(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.lookControl = new TurretLookControl(this);
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 38D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 24D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        })).add(new AnimationController<>(this, "shoot_controller", state -> PlayState.STOP)
                .triggerableAnim("shoot", RawAnimation.begin().thenPlay("shoot")).setAnimationSpeed(1.6f));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new RangedAttackGoal(this, 1.5f, 7, 22));

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        //Seek out
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float v) {
        BulbProjectileEntity bulbProjectile = new BulbProjectileEntity(this.level(), this);

        bulbProjectile.setPos(this.getX(), this.getAttachedEyeY(), this.getZ());

        // Calculate direction
        double dx = (target.getX() + target.getDeltaMovement().x()) - this.getX();
        double dy = target.getEyeY() - bulbProjectile.getY();
        double dz = (target.getZ() + target.getDeltaMovement().z()) - this.getZ();

        float spread = 0.3f;

        // Shoot
        bulbProjectile.shoot(dx, dy, dz, 2.8f, spread);

        if(this.level() instanceof ServerLevel slvl) {
            slvl.sendParticles(ParticleTypes.POOF, this.getX(), this.getAttachedEyeY(), this.getZ(), 5, 0.5, 0.5, 0.5, 1.1f);
        }

        this.getAnimatableInstanceCache()
                .getManagerForId(this.getId()).getAnimationControllers().get("shoot_controller").forceAnimationReset();
        triggerAnim("shoot_controller", "shoot");

        // Post shoot
        this.level().addFreshEntity(bulbProjectile);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.HOSTILE, 1.0F, 0.66F);
    }

    public class TurretLookControl extends LookControl {

        public TurretLookControl(Mob pMob) {
            super(pMob);
        }

        @Override
        protected void clampHeadRotationToBody() {
            //do not
        }
    }
}
