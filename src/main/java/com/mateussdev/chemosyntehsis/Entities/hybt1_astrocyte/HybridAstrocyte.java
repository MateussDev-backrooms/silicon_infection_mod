package com.mateussdev.chemosyntehsis.Entities.hybt1_astrocyte;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.LungeGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.gibs.flesh_gib.GibFlesh;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
        this.goalSelector.addGoal(0, new LungeGoal(this, 1.2f, 0.6f, 0, 9d, 3d, this::canLunge));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0f, true));

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

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));
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

    @Override
    public void tick() {
        super.tick();

        if (this.isPassenger()) {
            Entity vehicle = this.getVehicle();
            if(this.level() instanceof ServerLevel slvl) {
                if (vehicle instanceof LivingEntity victim) {
                    if (this.tickCount % 20 == 0) {
                        victim.hurt(damageSources().generic(), 1f);
                        if(victim.getHealth() / victim.getMaxHealth() < 0.2f) {
                            splitIntoChunks(5, victim);
                            victim.discard();
                            this.stopRiding();
                            this.setTarget(null);

                        }
                    }
                }
            }
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

    public void splitIntoChunks(int count, LivingEntity victim) {
        if(level() instanceof ServerLevel slvl) {
            slvl.playSound(
                    null,
                    victim.blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, victim.blockPosition());

            //Spawn chunks
            for (int i = 0; i < count; i++) {
                if(slvl.random.nextFloat() < 0.33f) {
                    ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                    chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.8f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(chunkOfFlesh);
                } else {
                    GibFlesh gib = ModEntities.GIB_FLESH.get().create(slvl);
                    gib.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    gib.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.5f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(gib);
                }
            }

        }
    }
}
