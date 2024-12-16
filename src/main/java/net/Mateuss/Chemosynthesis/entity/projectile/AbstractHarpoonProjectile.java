package net.Mateuss.Chemosynthesis.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractHarpoonProjectile extends AbstractArrow {
    protected AbstractHarpoonProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel, boolean canAttachToBlocks, boolean canAttachToMobs, boolean dragItself) {
        super(pEntityType, pLevel);
        this.canAttachToBlocks = canAttachToBlocks;
        this.canAttachToMobs = canAttachToMobs;
        this.dragItself = dragItself;
    }

    public enum AttachTypes {
        None,
        Block,
        Mob,
        Reeling
    }

    public static final EntityDataAccessor<Boolean> Attached = SynchedEntityData.defineId(AbstractHarpoonProjectile.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> AttachedType = SynchedEntityData.defineId(AbstractHarpoonProjectile.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Optional<UUID>> TargetUUID = SynchedEntityData.defineId(AbstractHarpoonProjectile.class, EntityDataSerializers.OPTIONAL_UUID);

    public static final EntityDataAccessor<Vector3f> HarpoonPos = SynchedEntityData.defineId(AbstractHarpoonProjectile.class, EntityDataSerializers.VECTOR3);
    public static final EntityDataAccessor<Integer> HarpoonLength = SynchedEntityData.defineId(AbstractHarpoonProjectile.class, EntityDataSerializers.INT);

    public final boolean dragItself;

    public boolean isAttached() {
        return entityData.get(Attached);
    }

    public void setAttached(boolean state) {
        entityData.set(Attached, state);
    }

    public int getCurrentAttachType() {
        return entityData.get(AttachedType);
    }

    public void setCurrentAttachType(int type) {
        entityData.set(AttachedType, type);
    }

    public void setCurrentAttachType(AttachTypes type) {
        entityData.set(AttachedType, type.ordinal());
    }

    public UUID getTargetUUID() {
        return entityData.get(TargetUUID).orElse(null);
    }

    public void setTargetUUID(UUID uuid) {
        entityData.set(TargetUUID, Optional.of(uuid));
    }

    private Entity target;
    @Nullable
    public Entity getTarget() {
        if(target == null) {
            if(!level().isClientSide) {
                ServerLevel lvl = (ServerLevel) level();
                if(getTargetUUID() != null) {
                    return target = lvl.getEntity(getTargetUUID());
                }
            }
        } else {
            if(getTargetUUID() != target.getUUID()) {
                setTargetUUID(target.getUUID());
            }
            return target;
        }
        return null;
    }

    public void setTarget(@Nullable Entity newTarget) {
        this.target = newTarget;
        if(newTarget != null) {
            setTargetUUID(newTarget.getUUID());
        }
    }

    public Vec3 getHarpoonedPos() {
        Vector3f pos = entityData.get(HarpoonPos);
        return new Vec3(pos.x, pos.y, pos.z);
    }
    public void setHarpoonPos(Vec3 pos) {
        entityData.set(HarpoonPos, pos.toVector3f());
    }

    public int getHarpoonLeftLength() {
        return entityData.get(HarpoonLength);
    }
    public void setHarpoonLength(int freshness) {
        entityData.set(HarpoonLength, freshness);
    }
    public void decreaseLength(int amount) {
        setHarpoonLength(getHarpoonLeftLength() - amount);
    }

    public double strength = 1;
    public final boolean canAttachToBlocks;
    public final boolean canAttachToMobs;

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(Attached, false);
        this.entityData.define(AttachedType, AttachTypes.None.ordinal());
        this.entityData.define(TargetUUID, Optional.empty());
        this.entityData.define(HarpoonPos, new Vector3f());
        this.entityData.define(HarpoonLength, 200);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Attached", isAttached());
        tag.putInt("AttachedType", getCurrentAttachType());
        if (getTargetUUID() != null){
            tag.putUUID("TargetUUID", getTargetUUID());
        }
        if (getHarpoonedPos() != null){
            tag.putDouble("blockPosX", getHarpoonedPos().x);
            tag.putDouble("blockPosY", getHarpoonedPos().y);
            tag.putDouble("blockPosZ", getHarpoonedPos().z);
        }
        tag.putInt("harpoonFreshness", getHarpoonLeftLength());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(Attached, pCompound.getBoolean("Attached"));
        entityData.set(AttachedType, pCompound.getInt("AttachedType"));
        if(pCompound.hasUUID("TargetUUID")) {
            setTargetUUID(pCompound.getUUID("TargetUUID"));
        }

    }

    public void DragParentToTarget(double strength) {
        Entity parent = getOwner();
        Entity target = getTarget();
        if(parent != null && target != null) {
            Vec3 force = parent.position().subtract(target.position()).normalize().scale(strength);
            parent.setDeltaMovement(getDeltaMovement().add(force));
        }
    }
    public void DragParentToPos(double strength) {
        Entity parent = getOwner();
        Vec3 target = getHarpoonedPos();
        if(parent != null && target != null) {
            Vec3 force = target.subtract(parent.position()).normalize().scale(strength);
            parent.setDeltaMovement(getDeltaMovement().add(force));
        }
    }
    public void DragTargetToParent(double strength) {
        Entity parent = getOwner();
        Entity target = getTarget();
        if(parent != null && target != null) {
            Vec3 force = parent.position().subtract(target.position()).normalize().scale(strength);
            target.setDeltaMovement(getDeltaMovement().add(force));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity parent = getOwner();
        if(parent == null) return;
        Entity target = pResult.getEntity();

        //handle player blocking
        if(target instanceof Player player && player.isBlocking()) {
            player.disableShield(true);
            RetractHarpoon();
            return;
        }
        //otherwise
        setAttached(true);
        setCurrentAttachType(AttachTypes.Mob);
        setHarpoonPos(target.position());
        setTarget(target);
        setDeltaMovement(0, 0, 0);
        setPosToHarpoonPos();
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        setAttached(true);
        setCurrentAttachType(AttachTypes.Block);
        setHarpoonPos(pResult.getLocation());
    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        return p_36743_ != getOwner();
    }

    @Override
    public void tick() {
        super.tick();

        if(getOwner() != null) {
            decreaseLength(1);
            if(getHarpoonLeftLength() > 0 && getCurrentAttachType() != AttachTypes.Reeling.ordinal()) {
                setPosToHarpoonPos();
                if(getOwner() instanceof LivingEntity LE && isAttached()) {
                    if(LE != null) {
                        if(getCurrentAttachType() == AttachTypes.Mob.ordinal()) {
                            //only grapple if there is a line of sight
                            if(getTarget() == null){
                                RetractHarpoon();
                            }
                            if(LE.hasLineOfSight(getTarget())) {
                                decreaseLength(1);
                            }
                            if(dragItself) DragParentToTarget(strength);
                            else DragTargetToParent(strength);

                            if(LE.distanceTo(getTarget()) < 1) {
                                RetractHarpoon();
                            }
                        }
                        else if(getCurrentAttachType() == AttachTypes.Block.ordinal()) {
                            decreaseLength(1);
                            DragParentToPos(strength);
                            if(Mth.sqrt((float) LE.distanceToSqr(getHarpoonedPos())) < 1) {
                                RetractHarpoon();
                            }
                        }
                    } else {
                        this.discard();
                    }
                }
                setNoGravity(ShouldApplyGravity());
            } else {
                RetractHarpoon();
            }

        } else {
            this.discard();
        }

    }

    protected void setPosToHarpoonPos() {
        if(getCurrentAttachType() == AttachTypes.Mob.ordinal() && getTarget() != null) {
            setPos(getTarget().position().add(0, getTarget().getBbHeight()/2, 0));
        }
        else if(getCurrentAttachType() == AttachTypes.Block.ordinal()) {
            setPos(getHarpoonedPos());
        }
    }

    protected void RetractHarpoon() {
        setAttached(false);
        setCurrentAttachType(AttachTypes.Reeling);
        setTarget(null);
        setNoPhysics(true);
        Entity parent = getOwner();
        if((parent != null && this.distanceTo(getOwner()) > 2) && getHarpoonLeftLength() > -200) {
            Vec3 retractDirection = parent.position().subtract(position()).normalize();
            this.setDeltaMovement(retractDirection.scale(strength));
        }
        else {
            discard();
        }
    }

    public void shoot(Vec3 direction, float velocity, float inaccuracy, Entity parent, double strength, int length) {
        setOwner(parent);
        this.strength = strength;
        setPos(parent.position().add(0, 0.5, 0));
        setHarpoonLength(length);

        super.shoot(direction.x, direction.y, direction.z, velocity, inaccuracy);
    }

    protected boolean ShouldApplyGravity(){
        if (getOwner() != null){
            return distanceTo(getOwner()) < 15;
        }
        return true;
    }
}
