package com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalAttackGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.LungeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class HybridAstrocyte extends BaseHybrid {
    public HybridAstrocyte(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.44D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 2D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 3D);
    }

    protected int mode = 0; // 0 - Support, 1 - Attack

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            boolean isFalling = !this.onGround() && this.getDeltaMovement().y < 0;
            if(isFalling) return event.setAndContinue(RawAnimation.begin().thenLoop("fall"));
            if(this.isPassenger()) return event.setAndContinue(RawAnimation.begin().thenLoop("attached"));

            return event.setAndContinue(
                    event.isMoving()
                            ? RawAnimation.begin().thenLoop("walk") // If moving, play the walking animation
                            : RawAnimation.begin().thenLoop("idle")); // If not moving, play the idle animation
        }));
    }

    @Override
    protected void registerGoals() {
        //Default settings override when new behavior is required

        // - GOALS
        this.goalSelector.addGoal(0, new LungeGoal(this, 1.2f, 0.4f, 0, 9d, 4d, this::canLunge));
        this.goalSelector.addGoal(1, new ConditionalAttackGoal(this, 1.2f, false, (i) -> mode == 1));

        //Avoid water (No float task cuz they are immune to water damage)
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        // - TARGETS
        if(shouldAlertOthersOnHurt()) {
            //get aggressive and alert
            this.targetSelector.addGoal(1, (new HurtByNonSiliconiteGoal(this, new Class[0])).setAlertOthers(new Class[]{BaseSiliconite.class}));
        } else {
            //only get aggressive
            this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));
        }

        //Seek out mobs to support
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                target -> (StaticSiliconiteMethods.ASTROCYTE_SUPPORT_TARGETS.containsKey(target.getType()) && target.getPassengers().isEmpty())));
        //Seek out attack targets
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        super.setTarget(target);
        if (target == null ||
                StaticSiliconiteMethods.ASTROCYTE_SUPPORT_TARGETS.containsKey(target.getType())) {
            mode = 0;
        } else if((this.getVehicle() instanceof LivingEntity host) && StaticSiliconiteMethods.ASTROCYTE_SUPPORT_TARGETS.containsKey(host.getType())) {
            mode = 0;
        }
        else {
            mode = 1;
        }
    }

    @Override
    protected float getTetherChance() {
        return 0.0f;
    }

    @Override
    protected float getBulbBreakoffChance() {
        return 0.0f;
    }

    private int cooldown = 0;
    private int jump_cld = 20;
    private int atk_cooldown = 0;
    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.getTarget() != null) {
            LivingEntity target = this.getTarget();
            if(cooldown > 0) cooldown--;
            if(atk_cooldown > 0) atk_cooldown--;

            boolean isFalling = !this.onGround() && this.getDeltaMovement().y < 0;
            if(isFalling && canLunge(0)) {
                cooldown = jump_cld;
            }


            if (!(target instanceof Player) && !this.isPassenger()) {
                double distance = this.distanceTo(target);
                if (distance < 1.5D && target.getFirstPassenger() == null) {
                    this.startRiding(target, true);
                }
            } else if(target instanceof Player player && this.getDeltaMovement().length() > 0.3f && !this.onGround()) {
                double distance = this.distanceTo(target);
                if (distance < 1.0D) {
                    if (player.isBlocking() && atk_cooldown <= 0) {
                        if (player.isUsingItem()) {
                            player.disableShield(false);

                            if (!player.getUseItem().isEmpty()) {
                                player.getUseItem().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                            }

                            this.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                                    SoundEvents.SHIELD_BREAK,
                                    SoundSource.PLAYERS,
                                    1.0F, 1.0F);
                            //Hurt when blocked
                            this.hurt(damageSources().playerAttack(player), 1f);

                            atk_cooldown = 20;

                            this.setDeltaMovement(player.getLookAngle().scale(this.getSpeed()));
                            this.hasImpulse = true;
                        }
                    } else {
                        this.doHurtTarget(player);
                    }
                }
            }
        }

        if (this.isPassenger()) {
            this.setDeltaMovement(0, 0, 0);
        }
    }

    //Tickering
    @Override
    public void tick() {
        super.tick();

        if(!this.isPassenger()) return;
        if (!(this.level() instanceof ServerLevel slvl)) return;


        Entity vehicle = this.getVehicle();
        if (!(vehicle instanceof LivingEntity host)) return;
        if(StaticSiliconiteMethods.ASTROCYTE_SUPPORT_TARGETS.containsKey(host.getType())) {
            mode = 0;
        }

        if (mode == 0) {
            handleSupportTick(host);
        } else {
            handleMeltingTick(host, slvl);
        }

    }

    //Supporting tick
    protected void handleSupportTick(LivingEntity host) {
        if (this.tickCount % 100 != 0) return;

        host.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 0));

        if (this.random.nextBoolean()) {
            host.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 0));
        } else {
            host.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 120, 0));
        }
    }


    //Attacking tick
    protected void handleMeltingTick(LivingEntity victim, ServerLevel slvl) {
        if (this.tickCount % 20 != 0) return;

        victim.hurt(damageSources().generic(), 1f);

        if (victim.getHealth() / victim.getMaxHealth() < 0.2f) {
            slvl.playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

            slvl.setBlock(victim.blockPosition(), ModBlocks.BIOMUSH.get().defaultBlockState(), 2);
            victim.discard();

            this.stopRiding();
            this.setTarget(null);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(this.isPassenger()) {
            this.stopRiding();
            this.setTarget(null);
        }
        return super.hurt(pSource, pAmount);
    }

    public boolean canLunge(int _i) {
        return cooldown <= 0;
    }
}
