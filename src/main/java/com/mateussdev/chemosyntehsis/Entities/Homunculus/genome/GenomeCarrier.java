package com.mateussdev.chemosyntehsis.Entities.Homunculus.genome;

import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IHomunculus;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HomingMissileGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Gene;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;

import java.util.UUID;

public class GenomeCarrier extends BaseSiliconite {
    public GenomeCarrier(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public Gene carriedGene = new Gene();
    public UUID hostHomunculus = null;

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 5D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.FLYING_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 2D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 3D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(
                    RawAnimation.begin().thenLoop("fly"));
        }));
    }

    @Override
    protected void registerGoals() {
        //Default settings override when new behavior is required

        // - GOALS
        //No target selector cuz target is set via forced target
        this.goalSelector.addGoal(0, new HomingMissileGoal(this, 1f));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos pos) {
                return true; // Can pathfind anywhere
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean isNoGravity() {
        return !isDeadOrDying();
    }

    protected boolean shouldTargetForMutation(LivingEntity mob) {
        if(mob instanceof IGenomeModifiable) return true;
        return false;
    }

    @Override
    protected float getTetherChance() {
        return 0.0f;
    }

    @Override
    protected float getBulbBreakoffChance() {
        return 0.0f;
    }

    @Override
    public GeoBone[] getBulbsArray(GeoModel<?> model) {
        return new GeoBone[0];
    }

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            if(this.getTarget() != null && this.getTarget().distanceTo(this) < 1.3f && this.getTarget() instanceof Mob mob) {
                applyGenome(mob);
            }
        }
    }

    public void applyGenome(Mob target) {
        if(target instanceof IGenomeModifiable genmod) {
            genmod.setGeneOrigin(hostHomunculus);
            genmod.applyGene(carriedGene);

            //Send data to homunculus
            if(this.level() instanceof ServerLevel slvl) {

                //Poof out of existence
                slvl.playSound(null, blockPosition(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.HOSTILE, 1f, 1f);
                SiliconiteParticles.spawnTransformationParticle(slvl, target.blockPosition());
                this.discard();
            }


        }
    }

    private UUID forcedTargetUUID = null;   // target to deliver the gene to

    public void setForcedTarget(Mob target) {
        this.forcedTargetUUID = target.getUUID();
        this.setTarget(target);
    }

    public Mob getForcedTarget(ServerLevel level) {
        if (forcedTargetUUID == null) return null;
        Entity e = level.getEntity(forcedTargetUUID);
        return (e instanceof Mob) ? (Mob) e : null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (forcedTargetUUID != null) tag.putUUID("ForcedTarget", forcedTargetUUID);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("ForcedTarget")) {
            forcedTargetUUID = tag.getUUID("ForcedTarget");
            if(level() instanceof ServerLevel slvl) {
                LivingEntity targetedEntity = (LivingEntity) slvl.getEntity(forcedTargetUUID);
                if(targetedEntity != null) this.setTarget(targetedEntity);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.setNoGravity(true);
    }
}
