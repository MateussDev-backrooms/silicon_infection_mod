package com.mateussdev.chemosyntehsis.Entities.hybt1_erythrocyte;

import com.mateussdev.chemosyntehsis.Entities.generic.AI.*;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class HybridErythrocyte extends BaseHybrid {
    public HybridErythrocyte(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.moveControl = new FloatingMoveControl(this);
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatingLungeGoal(this));
        this.goalSelector.addGoal(2, new FloatingSiliconiteRandomStrollGoal(this));

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, this::shouldTargetMob));
    }

    private int cooldown;
    private int max_cooldown = 80;
    private boolean canLunge(Integer integer) {
        return cooldown <= 0;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.setNoGravity(true);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level() instanceof ServerLevel slvl) {
            if(this.getDeltaMovement().length() > 1.0f && this.getTarget() != null) {
                if(this.getTarget().distanceTo(this) < 0.5f) {
                    this.doHurtTarget(this.getTarget());
                }
            }

            if(cooldown > 0) {
                cooldown--;
            } else {
                boolean isFalling = this.getDeltaMovement().length() > 1.0f;
                if(isFalling && canLunge(0)) {
                    cooldown = max_cooldown;
                }
            }
        }
    }

    public void onLunge() {
        //TODO add particle effect
    }

    private int deathTime = 0;
    private boolean hasSelectedRandomDir = false;
    double rngx;
    double rngy;
    double rngz;
    @Override
    protected void tickDeath() {
        if (this.level() instanceof ServerLevel slvl) {
            ++this.deathTime;

            if (last_attacker != null && last_attacker.isAlive()) {
                Vec3 dir = last_attacker.position().subtract(this.position()).normalize().scale(this.getSpeed() * 1.5f);
                this.setDeltaMovement(dir);
            } else {
                if (!hasSelectedRandomDir) {
                    hasSelectedRandomDir = true;
                    rngx = slvl.random.nextDouble() * 2 - 1;
                    rngy = slvl.random.nextDouble() * 2 - 1;
                    rngz = slvl.random.nextDouble() * 2 - 1;
                }
                this.setDeltaMovement(new Vec3(rngx, rngy, rngz).normalize().scale(this.getSpeed() * 1.2f));
            }

            if (this.deathTime >= 60) {
                slvl.explode(this, this.getX(), this.getY(), this.getZ(), 3, Level.ExplosionInteraction.TNT);
                StaticSiliconiteMethods.spawnBloodBurst(slvl, this.blockPosition());
                this.discard();
            }
        }
    }

    private LivingEntity last_attacker;
    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof LivingEntity living) {
            this.last_attacker = living;
        }
        return super.hurt(pSource, pAmount);

    }
}
