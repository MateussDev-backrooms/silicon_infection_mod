package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.met_zombie.MetZombie;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.model.GeoModel;

import java.util.List;

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

    public int screamCooldown = 0;
    public int quickstepCooldown = 0;
    public int stunT = 0;

    public TethEnderman(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setMaxUpStep(1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
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
                .add(Attributes.ATTACK_DAMAGE, 7D);
    }

    // ===== AI ===== //


    @Override
    protected void registerGoals() {
        // Priority order: higher number = lower priority
        this.goalSelector.addGoal(0, new TethEndermanQuickstepGoal(this));
//        this.goalSelector.addGoal(3, new TethEndermanChaseGoal(this));
        this.goalSelector.addGoal(1, new TethEndermanCarryMobGoal(this));
//        this.goalSelector.addGoal(2, new TethEndermanScreamGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0f, true));

        //Avoid water
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.1D));

        //Looking goals
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 3f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        // - TARGETS
        this.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(this));

        //Seek out
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, false, false, StaticSiliconiteMethods::shouldAttackMob));
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

        // Handle cooldowns
        if (screamCooldown > 0) { screamCooldown--; }
        if (quickstepCooldown > 0) { quickstepCooldown--; }

        if(stunT > 0) {
            stunT--;
            getNavigation().stop();
        }

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
            if(random.nextFloat() < 0.5f) {
                double angle = random.nextDouble() * Math.PI * 2;
                double distance = 8 + random.nextDouble() * 12;
                double x = blockPosition().getX() + Math.cos(angle) * distance;
                double z = blockPosition().getZ() + Math.sin(angle) * distance;
                double y = blockPosition().getY();

                Vec3 teleportPos = new Vec3(x, y, z);
                teleport(teleportPos);
            }
            return super.hurt(pSource, pAmount);
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
            return PlayState.STOP;
        }).triggerableAnim("scream", RawAnimation.begin().thenPlay("scream")));

        controllers.add(new AnimationController<>(this, "teleport_controller", 0, state -> {
            return PlayState.STOP;
        }).triggerableAnim("teleport1", RawAnimation.begin().thenPlay("teleport_1"))
                .triggerableAnim("teleport2", RawAnimation.begin().thenPlay("teleport_2"))
                .triggerableAnim("teleport3", RawAnimation.begin().thenPlay("teleport_3")));
    }

    public boolean isSensitiveToWater() {
        return true;
    }

    public void teleport(Vec3 teleportPosition) {
        if(level() instanceof ServerLevel slvl) {
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.75f);
            this.playSound(SoundEvents.ENDERMAN_HURT, 0.75f, 1f);
            this.playSound(SoundEvents.SHULKER_TELEPORT, 0.75f, 1f);
            this.playSound(SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, 0.75f, 1f);

            slvl.sendParticles(ParticleTypes.REVERSE_PORTAL, this.position().x,this.position().y, this.position().z, 15, this.getBoundingBox().getXsize(), this.getBoundingBox().getYsize(), this.getBoundingBox().getZsize(), 2f);
            slvl.sendParticles(ParticleTypes.FLASH, this.position().x,this.getEyePosition().y, this.position().z, 1, 0, 0, 0, 2f);
            double x = teleportPosition.x;
            double y = teleportPosition.y;
            double z = teleportPosition.z;

            slvl.sendParticles(ParticleTypes.REVERSE_PORTAL, x, y, z, 15, this.getBoundingBox().getXsize(), this.getBoundingBox().getYsize(), this.getBoundingBox().getZsize(), 2f);

            boolean teleportSuccessful = checkTeleportPosition(teleportPosition, slvl);
            if(teleportSuccessful) {
                this.randomTeleport( x, y, z, true);

                this.xo = x;
                this.yo = y;
                this.zo = z;
                this.level().gameEvent(GameEvent.TELEPORT, this.position(), GameEvent.Context.of(this));

                slvl.broadcastEntityEvent(this, (byte)46);
                List<String> tpanims = List.of("teleport1", "teleport2", "teleport3");
                this.getAnimatableInstanceCache()
                        .getManagerForId(this.getId()).getAnimationControllers().get("teleport_controller").forceAnimationReset();
                triggerAnim("teleport_controller", tpanims.get(random.nextInt(tpanims.size())));

                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1f, 0.75f);
                this.playSound(SoundEvents.ENDERMAN_HURT, 0.75f, 1f);
                this.playSound(SoundEvents.SHULKER_TELEPORT, 0.75f, 1f);
                this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 0.75f, 1f);
            } else {
                this.hurt(damageSources().generic(), 3);
                this.playSound(SoundEvents.ENDERMAN_DEATH);
                stopAllAnimations();
                this.triggerAnim("scream_controller", "scream");
                stunT = 60;
                this.getNavigation().stop();
            }
        }
    }

    protected boolean checkTeleportPosition(Vec3 position, ServerLevel slvl) {
        BlockState blockstate = this.level().getBlockState(BlockPos.containing(position));
        boolean flag = blockstate.blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        boolean flag2 = !slvl.noCollision(getBoundingBox().move(position.subtract(this.position())));

        return !flag && !flag1 && !flag2;
    }


    public void ramIntoTarget(LivingEntity target) {
        if(target instanceof Player player) {
            if(player.isBlocking()) {
                player.disableShield(true);
                this.hurt(damageSources().generic(), 5);
                this.playSound(SoundEvents.ENDERMAN_DEATH);
                stopAllAnimations();
                this.triggerAnim("scream_controller", "scream");
                stunT = 60;
            } else {
                target.setDeltaMovement(this.getLookAngle().normalize().add(0, 0.2f, 0).scale(5f));
                playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR);
                stunT = 20;
            }
        } else {
            if(doHurtTarget(target)) {
                //Successful hit
                target.setDeltaMovement(this.getLookAngle().normalize().add(0, 0.2f, 0).scale(5f));
                playSound(SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR);
                stunT = 20;
            } else {
                stunT = 100;
                //TODO make stunned
            }
        }
    }

    private void stopAllAnimations() {
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("teleport_controller").forceAnimationReset();
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("scream_controller").forceAnimationReset();
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("movement").forceAnimationReset();
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("parry_controller").forceAnimationReset();
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("quickstep_controller").forceAnimationReset();
    }
}
