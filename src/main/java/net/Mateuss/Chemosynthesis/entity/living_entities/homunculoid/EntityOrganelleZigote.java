package net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid;

import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.ai_goals.ZigoteBehaviorTask;
import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EntityOrganelleZigote extends SiliconiteBase {
    public EntityOrganelleZigote(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public EntityHomunculus parentHomunculusEntity=null;

    private static final EntityDataAccessor<Optional<UUID>> syncedParentHomunculusUUID = SynchedEntityData.defineId(EntityOrganelleZigote.class, EntityDataSerializers.OPTIONAL_UUID);


    public float veinThickness = 0.1f + this.level().random.nextFloat()*0.25f;
    public float yOffset = this.level().random.nextFloat()*0.66f;
    public float randomBrightness = this.level().random.nextFloat()*0.4f;

    public boolean will_spawn_vaucole = false;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new ZigoteBehaviorTask(this, 1.0f, 10));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 3D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_DAMAGE, 1D);
    }

    public EntityHomunculus getParent() {
        return parentHomunculusEntity;
    }

    public void setParentUUID(UUID uuid) {
        entityData.set(syncedParentHomunculusUUID, Optional.of(uuid));
    }

    public void setParentEntity(EntityHomunculus homunculus) {
        parentHomunculusEntity = homunculus;
        entityData.set(syncedParentHomunculusUUID, Optional.of(homunculus.getUUID()));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if(this.level() instanceof ServerLevel pLevel) {
            if(parentHomunculusEntity == null) {
                if(entityData.get(syncedParentHomunculusUUID).isPresent()) {
                    Entity ent = pLevel.getEntity(entityData.get(syncedParentHomunculusUUID).get());
                    if(ent != null && ent instanceof EntityHomunculus ent_hom) {
                        parentHomunculusEntity = ent_hom;
                    }
                }
            }
        }
    }

    public Entity findNearestHomunculus() {
        List<Entity> nearbyList = level().getEntities(
                (Entity) null, new AABB(this.blockPosition()).inflate(12),
                entity -> entity instanceof EntityHomunculus
        );
        for (Entity entity : nearbyList) {
            if(distanceTo(entity) <= 12) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public void evolve() {
        if(this.level() instanceof ServerLevel lvl) {
            lvl.sendParticles(
                    ParticleTypes.EXPLOSION,
                    this.getX() + 0.5,
                    this.getY() + 0.5,
                    this.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    0,    // X offset for randomness
                    0,    // Y offset
                    0,    // Z offset
                    0.1         // Speed of the particle
            );
            lvl.playSound(
                    null,
                    this.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            LivingEntity result = null;
            if(will_spawn_vaucole) {
                result = ModEntities.HOMUNCULUS_VAUCOLE.get().create(lvl);
            } else {
                result = ModEntities.HOMUNCULUS_MITOCHONDRIA.get().create(lvl);
                //no other organelles yet
            }

            if(result != null) {
                result.moveTo(this.getX(), this.getY(), this.getZ());
                lvl.addFreshEntity(result);
                if(parentHomunculusEntity != null) {
                    parentHomunculusEntity.disconnect(getUUID());
                    parentHomunculusEntity.connectOrganelle(result.getUUID());
                }
                this.discard();
            }

        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if(pCompound.hasUUID("parent_homunculus_uuid")) {
            Optional<UUID> new_uuid = Optional.of(pCompound.getUUID("parent_homunculus_uuid"));
            entityData.set(syncedParentHomunculusUUID, new_uuid);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if(entityData.get(syncedParentHomunculusUUID).isPresent()) {
            pCompound.putUUID("parent_homunculus_uuid", entityData.get(syncedParentHomunculusUUID).get());
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(syncedParentHomunculusUUID, Optional.empty());
    }
}
