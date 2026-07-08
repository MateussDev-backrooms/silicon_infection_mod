package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_villager;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Metabolized.met_zombie.MetZombie;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalAttackGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ConditionalFleeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.LungeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;

public class TethVillager extends BaseTethered {
    public TethVillager(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.bulbCount = 8;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            boolean isFalling = !this.onGround() && this.getDeltaMovement().y < 0;
            if(isFalling) return event.setAndContinue(RawAnimation.begin().thenLoop("jump_loop"));
            //death anim
            if (this.isDeadOrDying()) {
                return event.setAndContinue(RawAnimation.begin().then("death", Animation.LoopType.HOLD_ON_LAST_FRAME));
            }

            return event.setAndContinue(
                    event.isMoving() ? RawAnimation.begin().thenLoop("walk") :
                            RawAnimation.begin().thenLoop("idle"));
        }));


        //Jump events
        controllers.add(new AnimationController<>(this, "jump_controller", state -> PlayState.STOP)
                .triggerableAnim("jump_start", RawAnimation.begin().then("jump_begin", Animation.LoopType.PLAY_ONCE))
                .triggerableAnim("jump_end", RawAnimation.begin().then("jump_end", Animation.LoopType.PLAY_ONCE))
                .triggerableAnim("jump_fail", RawAnimation.begin().then("jump_fail", Animation.LoopType.HOLD_ON_LAST_FRAME))
                .setAnimationSpeed(1.4f));
        //Bone wobbling
        controllers.add(new AnimationController<>(this, "reaction_controller", 1, event -> PlayState.CONTINUE));
    }

    @Override
    public void registerDefaultGoals() {
        this.goalSelector.removeAllGoals(g -> true);
        this.targetSelector.removeAllGoals(g -> true);

        //Default siliconite AI

        this.goalSelector.addGoal(0, new LungeGoal(this, 1.5f, 0.4f, 0, 9d, 3d, this::canLunge));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0f, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));


        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ===== Entity setup and stats ===== //
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                //Basics
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
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
                model.getBone("appendage8").get()
        };

        if (hasScrambled) {
            return scrambled_bulbs;
        } else {
            scrambled_bulbs = StaticSiliconiteMethods.scrambleBones(bulbs);
            hasScrambled = true;
            return scrambled_bulbs;
        }
    }

    public void onLunge() {
//        triggerAnim("jump_controller", "jump_start");
    }

    private int cooldown = 0;
    private int jump_cld = 40;
    private int atk_cooldown = 0;

    private int stun = 0;

    private boolean oldFalling = false;
    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level() instanceof ServerLevel slvl && this.getTarget() != null) {

            LivingEntity target = this.getTarget();
            if(cooldown > 0) cooldown--;
            if(atk_cooldown > 0) atk_cooldown--;

            if(--stun > 0) {
                this.setTarget(null);
                this.navigation.stop();
            }

            boolean isFalling = !this.onGround() && this.getDeltaMovement().y < 0;
            if(isFalling && canLunge(0)) {
                cooldown = jump_cld;
            }

            double distance = this.distanceTo(target);
            if (distance < 1.0D) {
                if(target instanceof Player player) {
                    if (player.isBlocking() && atk_cooldown <= 0) {
                        if (player.isUsingItem()) {
                            player.disableShield(false);

                            if (!player.getUseItem().isEmpty()) {
                                player.getUseItem().hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                            }

                            faiLJump();


                            return;
                        }
                    }
                }
                this.doHurtTarget(target);
            }
        }
    }

    private boolean canLunge(int i) {
        return cooldown <= 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        boolean isFalling = !this.onGround() && this.getDeltaMovement().y < 0;
        if(isFalling) {
            faiLJump();
        }

        return super.hurt(pSource, pAmount);
    }

    protected void faiLJump() {
        atk_cooldown = 20;
        cooldown = 80;
        stun = 60;

        triggerAnim("jump_controller", "jump_fail");
        this.setDeltaMovement(this.getDeltaMovement().multiply(-1, 1, -1).scale(0.7));
        this.hasImpulse = true;
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        if(stun <= 0 && pFallDistance >= 0.8f) {
            triggerAnim("jump_controller", "jump_end");
        }
        return super.calculateFallDamage(pFallDistance, pDamageMultiplier);
    }

    @Override
    public void evolve() {
//        if (!(this.level() instanceof ServerLevel slvl)) return;
//        MetZombie metZombie = ModEntities.MET_ZOMBIE.get().create(slvl);
//        metZombie.moveTo(blockPosition().getCenter());
//        slvl.addFreshEntity(metZombie);
//        StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
//        this.discard();
    }


}
