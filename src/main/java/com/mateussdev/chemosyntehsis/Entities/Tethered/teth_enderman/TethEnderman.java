package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.met_zombie.MetZombie;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;

public class TethEnderman extends BaseTethered {

    //Parry mechanics
    private boolean parryAnim = false;

    //Enderman mechanics
    public enum TethEndermanState {
        CHASE,
        CARRYING_MOB,
        QUICKSTEP,
        SCREAM,
        STUNNED
    }

    private final TethEndermanStateMachine stateMachine;
    public TethEnderman(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.stateMachine = new TethEndermanStateMachine(this);
    }

    private int angerSoundT = 0;

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 32D)
                .add(Attributes.MOVEMENT_SPEED, 0.45D)
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
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
                model.getBone("appendage7").get(),
                model.getBone("appendage8").get(),
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
        return 5;
    }

    @Override
    public void evolve() {
        if (this.level() instanceof ServerLevel slvl) {
//            MetZombie metZombie = ModEntities.MET_ZOMBIE.get().create(slvl);
//            metZombie.moveTo(blockPosition().getCenter());
//            slvl.addFreshEntity(metZombie);
//            StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
//            this.discard();

        }
    }

    @Override
    public void tick() {
        super.tick();

        if(getTarget() instanceof Player) {
            if (!this.isSilent()) {
                if(angerSoundT % 400 == 0) {
                    this.level().playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ENDERMAN_STARE, this.getSoundSource(), 2.5F, 1.0F, false);
                    angerSoundT = 0;
                }
                angerSoundT++;
            }
        }

        if(level().isClientSide) {
            if(tickCount % 2 == 0) {
                this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5), this.getRandomY() - 0.25, this.getRandomZ(0.5), (this.random.nextDouble() - 0.5) * 2.0, -this.random.nextDouble(), (this.random.nextDouble() - 0.5) * 2.0);
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        stateMachine.tick();

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {


        if(pSource.getDirectEntity() instanceof Projectile projectile) {
            Vec3 deflectDir = new Vec3(
                    (level().random.nextFloat()*2-1)*2,
                    level().random.nextFloat()*0.5,
                    (level().random.nextFloat()*2-1)*2
            );
            projectile.shoot(deflectDir.x, -deflectDir.y, deflectDir.z, 15f, 1f);
            triggerAnim("parry_controller", parryAnim ? "parry1" : "parry2");
            parryAnim = !parryAnim;
            level().playSound(null, blockPosition(), SoundEvents.SHULKER_SHOOT, SoundSource.HOSTILE);
            return false;
        } else {
            boolean wasHurt = super.hurt(pSource, pAmount);
            if(wasHurt) {
                stateMachine.setStunned();
            }
            return wasHurt;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);
        controllers.add(new AnimationController<>(this, "parry_controller", state -> PlayState.STOP)
                .triggerableAnim("parry1", RawAnimation.begin().thenPlay("parry_projectile_1")).triggerableAnim("parry2", RawAnimation.begin().thenPlay("parry_projectile_2")).setAnimationSpeed(1.4f));

        controllers.add(new AnimationController<>(this, "quickstep_controller", 5, state -> {
            return PlayState.CONTINUE;
        }).triggerableAnim("quickstep_begin", RawAnimation.begin().thenPlay("quickstep_begin")).triggerableAnim("quickstep_end", RawAnimation.begin().thenPlay("quickstep_end")));

        controllers.add(new AnimationController<>(this, "scream_controller", 5, state -> {
            return PlayState.CONTINUE;
        }).triggerableAnim("scream", RawAnimation.begin().thenPlay("scream")));
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    public void teleport(Vec3 teleportPosition) {
        if(level() instanceof ServerLevel slvl) {
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.75f);
            this.playSound(SoundEvents.ENDERMAN_HURT, 0.75f, 1f);
            slvl.sendParticles(ParticleTypes.REVERSE_PORTAL, this.position().x,this.position().y, this.position().z, 15, this.getBoundingBox().getXsize(), this.getBoundingBox().getYsize(), this.getBoundingBox().getZsize(), 2f);
            teleportTo(teleportPosition.x, teleportPosition.y, teleportPosition.z);
        }
    }
}
