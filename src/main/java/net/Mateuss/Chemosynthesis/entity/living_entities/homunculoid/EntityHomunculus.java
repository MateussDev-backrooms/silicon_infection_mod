package net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid;

import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityHomunculus extends Monster {
    public EntityHomunculus(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public final AnimationState AS_isIdle = new AnimationState();
    private int idleAnimTimeout = 0;
    public int t = 0;

    public int heartbeat_speed = 40;

    public List<UUID> connectedEntitiesUUIDs = new ArrayList<UUID>();
    public int maxConnectedEntities = 5;

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide) {
            setupAnimationStates();
        }
    }



    @Override
    public void baseTick() {
        super.baseTick();

        if(!this.level().isClientSide && this.level() instanceof ServerLevel lvl) {
            pullFarAwayOrganelles();
            this.t++;
            if(t%this.heartbeat_speed==0) {
                lvl.playSound(null, this.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.HOSTILE);
                checkConnectedOrganelleStatus();
            }

            if(t%(this.heartbeat_speed*2)==0) {

                if(connectedEntitiesUUIDs.toArray().length < maxConnectedEntities) {
                    for(int i = 0; i<maxConnectedEntities- connectedEntitiesUUIDs.toArray().length; i++) {
                        EntityOrganelleZigote zigote = ModEntities.ZIGOTE.get().create(lvl);
                        if(zigote != null) {
                            zigote.moveTo(this.getEyePosition());
                            zigote.setDeltaMovement((lvl.random.nextDouble()*2.0d-1.0d)*0.33d, 0.5d, (lvl.random.nextDouble()*2.0d-1.0d)*0.33d);
                            zigote.setParentEntity(this);
                            connectedEntitiesUUIDs.add(zigote.getUUID());
                            lvl.addFreshEntity(zigote);
                        }
                    }
                }
            }

        }

        this.setAirSupply(this.getMaxAirSupply());
    }

    private void checkConnectedOrganelleStatus() {
        //checks every connected organelle if it's alive or valid
        if(level() instanceof ServerLevel slvl) {
            for(UUID uuid : connectedEntitiesUUIDs) {
                if(slvl.getEntity(uuid) != null) {
                    //do additional checks
                } else {
                    connectedEntitiesUUIDs.remove(uuid);
                }
            }
        }
    }

    public void pullFarAwayOrganelles() {
        if(level() instanceof ServerLevel slvl) {
            for(UUID uuid : connectedEntitiesUUIDs) {
                Entity organelle = slvl.getEntity(uuid);
                if(organelle.distanceTo(this) > 12f) {
                    Vec3 movement = new Vec3(getX() - organelle.getX(), getY() - organelle.getY(), getZ() - organelle.getZ()).normalize();
                    organelle.setDeltaMovement(movement.scale(0.5f));
                }
            }
        }
    }

    public void connectOrganelle(UUID uuid) {
        if(level() instanceof ServerLevel slvl) {
            if(slvl.getEntity(uuid) != null) {
                connectedEntitiesUUIDs.add(uuid);
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

    @Override
    protected void registerGoals() {
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 5D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.DRIPSTONE_BLOCK_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void remove(RemovalReason pReason) {

        super.remove(pReason);
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public void knockback(double strength, double xRatio, double zRatio) {
        this.setDeltaMovement(0, 0, 0);
    }

    @Override
    public boolean save(CompoundTag pCompound) {
        pCompound.putInt("homunculus_connected_organelle_amount", connectedEntitiesUUIDs.toArray().length);
        for(int i=0; i<connectedEntitiesUUIDs.toArray().length; i++) {
            String keyName = "homunculus_connected_organelles;"+i;
            pCompound.putUUID(keyName, connectedEntitiesUUIDs.get(i));
        }
        return super.save(pCompound);
    }

    @Override
    public void load(CompoundTag pCompound) {
        int l = pCompound.getInt("homunculus_connected_organelle_amount");
        for(int i=0; i<l; i++) {
            String keyName = "homunculus_connected_organelles;"+i;
            UUID extracted_id = pCompound.getUUID(keyName);
            connectedEntitiesUUIDs.add(extracted_id);
        }
        super.load(pCompound);
    }
}
