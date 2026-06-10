package com.mateussdev.chemosyntehsis.Entities.Homunculus.genome;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.IHomunculus;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.SeekAndEatBiomushGoal;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HomingMissileGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseHybrid;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Gene;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.UUID;

import static com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods.spawnBloodBurst;

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
        this.goalSelector.addGoal(0, new HomingMissileGoal(this, 1f));
        this.goalSelector.addGoal(2, new FloatingSiliconiteRandomStrollGoal(this, 7f, 4f));

        //Seek out
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Mob.class, 0, true, false, this::shouldTargetForMutation));
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

    private boolean _brw = true;
    @Override
    public void tick() {
        super.tick();

        if(this.level() instanceof ServerLevel slvl) {
            if(this.getTarget() != null) {
                if (this.getBoundingBox().inflate(0.5f).intersects(this.getTarget().getBoundingBox())) {
                    applyGenome((Mob) this.getTarget());
                }
            }
        }
    }

    public void applyGenome(Mob target) {
        if(target instanceof IGenomeModifiable genmod) {

            //TEMP DEBUG - add a set MutationFlight
            carriedGene.addMutation(new MutationFlight(ModMutations.FLIGHT.getId(), random.nextInt(999999999)));
            carriedGene.addMutation(new MutationFlight(ModMutations.SWIMMING.getId(), random.nextInt(999999999)));
            carriedGene.addMutation(new MutationFlight(ModMutations.HARPOON.getId(), random.nextInt(999999999)));

            genmod.applyGene(carriedGene);

            //Send data to homunculus
            if(this.level() instanceof ServerLevel slvl) {
                BaseSiliconite host = (BaseSiliconite) slvl.getEntity(hostHomunculus);
                if(host instanceof IHomunculus homunculus) {
                    homunculus.trackGene(carriedGene);
                }

                //Poof out of existence
                slvl.playSound(null, blockPosition(), SoundEvents.TRIDENT_HIT_GROUND, SoundSource.HOSTILE, 1f, 1f);
                SiliconiteParticles.spawnTransformationParticle(slvl, target.blockPosition());
                this.discard();
            }


        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.setNoGravity(true);
    }
}
