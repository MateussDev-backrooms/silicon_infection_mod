package net.Mateuss.Chemosynthesis.entity.living_entities;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.core.ModSounds;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.Mateuss.Chemosynthesis.entity.projectile.ProjectileBulb;
import net.Mateuss.Chemosynthesis.stage_system.InfectionProgressManager;
import net.Mateuss.Chemosynthesis.stage_system.InfectionProgress;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SiliconiteBase extends Monster {
    protected SiliconiteBase(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    //This value determines the progress of evolution. When it reaches metabolism required points it evolves
    //By default evolution does nothing. Must be overriden to do smth
    public static final EntityDataAccessor<Integer> METABOLISM_VALUE = SynchedEntityData.defineId(SiliconiteBase.class, EntityDataSerializers.INT);

    //This value determines how much energy the siliconite has left. When it reaches zero it turns vegetative
    //Optional as the function can be overriden to do nothing
    //By default slows down the siliconite by 75%
    public static final EntityDataAccessor<Integer> ENERGY = SynchedEntityData.defineId(SiliconiteBase.class, EntityDataSerializers.INT);

    protected boolean isVegetated = false;

    public int bulb_jerk_timer = 0;

    //Access to the stage of the infection
    @Nullable
    public InfectionProgress getInfectionProgressClass() {
        if(!this.level().isClientSide) {
            return InfectionProgressManager.get((ServerLevel) this.level());
        }
        return null;
    }



    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("metabolism_value", entityData.get(METABOLISM_VALUE));
        tag.putInt("energy", entityData.get(ENERGY));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(METABOLISM_VALUE, tag.getInt("metabolism_value"));
        entityData.set(ENERGY, tag.getInt("energy"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(METABOLISM_VALUE, 0);
        this.entityData.define(ENERGY, 100);
    }

    //Aniation stuff
    public final AnimationState AS_isIdle = new AnimationState();
    private int idleAnimTimeout = 0;
    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            setupAnimationStates();
        }
    }

    protected int requiredMetabolismForEvolution() {
        return 120;
    }

    //used to calculate time
    private int t = 0;
    //used to select after how many seconds to play the bulb jerk sound
    private float rng = getBulbSoundMinCooldown();

    @Override
    public void baseTick() {
        super.baseTick();

        //prevent drowning
        this.setAirSupply(this.getMaxAirSupply());

        if(!this.level().isClientSide) {
            t++;

            //metabolism stuffs
            if(t%40==0) {
                //add to metabolism
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain((ServerLevel) this.level()));
            }
            //increase metabolism every tick by a lot if on fire
            if(isOnFire()) {
                entityData.set(METABOLISM_VALUE, entityData.get(METABOLISM_VALUE) + getMetabolismGain((ServerLevel) this.level()) * 5);
                entityData.set(ENERGY, entityData.get(ENERGY) + 1);

            }

            //evolution
            if(entityData.get(METABOLISM_VALUE) >= requiredMetabolismForEvolution()) {
                entityData.set(METABOLISM_VALUE, 0);
                this.evolve();
            }

            //energy stuffs
            if(entityData.get(ENERGY) <= 0 && entityData.get(ENERGY) > -999) {
                this.vegetate();
                entityData.set(ENERGY, -999);
            }
            if(t%20==0) {
                //remove energy
                entityData.set(ENERGY, entityData.get(ENERGY) - 1);
            }

            //sounds

            //scale to ticks
            if(t%(Mth.floor(rng*20))==0) {
                this.level().playSound(null, blockPosition(), ModSounds.BULB_JERK.get(), SoundSource.HOSTILE, 1f, this.level().random.nextFloat());
                generateRandomBufferSoundNum();
                bulb_jerk_timer = this.level().random.nextIntBetweenInclusive(33, 100);
            }

            //jerk animation for when bulbs
            if(bulb_jerk_timer > 0) {
                bulb_jerk_timer--;
            }

        }
    }

    private void generateRandomBufferSoundNum() {
        float s_rng = level().random.nextFloat(); //between 0 and 1
        rng = s_rng * (getBulbSoundMaxCooldown() - getBulbSoundMinCooldown() + 1) + getBulbSoundMinCooldown();
    }

    //determines conditions at which metabolism is gained
    public int getMetabolismGain(ServerLevel lvl) {
        return 1;
    }

    public void tetherMob(ServerLevel pLevel, LivingEntity pEntity, LivingEntity pSelf, boolean discardSelf) {
        LivingEntity tethered_result = null;

        if(pEntity.getType() == EntityType.ZOMBIE || pEntity.getType() == EntityType.HUSK || pEntity.getType() == EntityType.DROWNED) {
            tethered_result = ModEntities.TETH_ZOMBIE.get().create(pLevel);
        }
        if(pEntity.getType() == EntityType.COW) {
            tethered_result = ModEntities.TETH_COW.get().create(pLevel);
        }
        if(pEntity.getType() == EntityType.PIG) {
            tethered_result = ModEntities.TETH_PIG.get().create(pLevel);
        }
        if(pEntity.getType() == EntityType.SHEEP) {
            tethered_result = ModEntities.TETH_SHEEP.get().create(pLevel);
        }

        //spawn the chosen entity
        if(tethered_result!=null) {
            tethered_result.moveTo(pEntity.getX(), pEntity.getY(), pEntity.getZ());
            pLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    pEntity.getX() + 0.5,
                    pEntity.getY() + 0.5,
                    pEntity.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    0,    // X offset for randomness
                    0,    // Y offset
                    0,    // Z offset
                    0.1         // Speed of the particle
            );
            pLevel.playSound(
                    null,
                    pEntity.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);
            pLevel.addFreshEntity(tethered_result);

            if(discardSelf) {
                pSelf.discard();
            }
        }
    }

    public void evolve() {
        //override smth
    }

    public void vegetate() {
        //override smth
        if(!isVegetated) {
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(getAttribute(Attributes.MOVEMENT_SPEED).getBaseValue() * 0.33f);
            isVegetated = true;
        }
    }

    public void spawnTripods(ServerLevel level, LivingEntity entity, int amountMin, int amountMax) {
        if(!level.isClientSide() && entity.isDeadOrDying()) {
            //play effects
            for (int i = 0; i < 80; i++) {  // Spawn multiple particles for effect
                double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
                double offsetY = level.random.nextDouble() * 0.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;

                level.sendParticles(
                        ParticleTypes.SMOKE,   // Example green particle
                        entity.getX() + 0.5,
                        entity.getY() + 0.5,
                        entity.getZ() + 0.5,
                        1,          // Number of particles per spawn call
                        offsetX,    // X offset for randomness
                        offsetY,    // Y offset
                        offsetZ,    // Z offset
                        0.1         // Speed of the particle
                );
            }

            //sound
            level.playSound(null,
                    entity.blockPosition(),
                    SoundEvents.SQUID_DEATH,
                    SoundSource.HOSTILE
            );

            //spawn the tripods
            int k = level.random.nextIntBetweenInclusive(amountMin, amountMax);

            for(int i=0; i<k; i++) {
                EntitySiliconTripod tripod = ModEntities.SILICON_TRIPOD.get().create(level);
                if(tripod != null) {
                    tripod.moveTo(entity.getX()+level.random.nextDouble(), entity.getY(), entity.getZ()+level.random.nextDouble());
                    tripod.setDeltaMovement((level.random.nextDouble()*2.0d-1.0d)*0.33d, 0.5d, (level.random.nextDouble()*2.0d-1.0d)*0.33d);
                    level.addFreshEntity(tripod);
                }
            }
            for(int i=0; i<k*2; i++) {
                ProjectileBulb bulb = ModEntities.BULB_PROJECTILE.get().create(level);
                bulb.setPos(getX(), getY(), getZ());
                bulb.shoot((level.random.nextDouble()*2.0d-1.0d)*0.33d, (level.random.nextDouble())*0.33d, (level.random.nextDouble()*2.0d-1.0d)*0.33d, 0.5f, 0);
                level.addFreshEntity(bulb);
            }
        }
    }

    private void setupAnimationStates() {
        if(this.idleAnimTimeout <= 0) {
            this.idleAnimTimeout = this.random.nextInt(40)+80;
            this.AS_isIdle.start(this.tickCount);
        } else {
            --this.idleAnimTimeout;
        }
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f;
        if(this.getPose() == Pose.STANDING) {
            f = Math.min(pPartialTick * 6F, 1f);
        } else {
            f=0f;
        }

        this.walkAnimation.update(f, 0.2f);
    }

    //On hurt itself

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {

        //only get dealt 25% of fire damage
        if(pSource.is(DamageTypeTags.IS_FIRE) || pSource.is(DamageTypeTags.BURNS_ARMOR_STANDS) || pSource.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            pAmount *= 0.25;
        }
        return super.hurt(pSource, pAmount);
    }

    //On hurt others
    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean success = super.doHurtTarget(pEntity);
        if(success) {
            entityData.set(ENERGY, entityData.get(ENERGY) + 10);
        }
        return success;
    }

    protected boolean isSiliconite(Entity entity) {

        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        // Check if the entity is not part of your mod
        return entityTypeKey == null || entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }
    protected boolean isNotSiliconite(Entity entity) {

        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        // Check if the entity is not part of your mod
        return entityTypeKey == null || !entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

    //these functions are used to customise how often bulb jerk sounds will play
    protected float getBulbSoundMinCooldown() {
        return 0.33f;
    }
    protected float getBulbSoundMaxCooldown() {
        return 2f;
    }
}
