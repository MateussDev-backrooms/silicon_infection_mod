package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseVegetated;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class VegetativeBulb extends BaseVegetated {
    public VegetativeBulb(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setNoGravity(true);
        this.setYBodyRot(0);
    }

    public static final EntityDataAccessor<Vector3f> ALIGNMENT = SynchedEntityData.defineId(VegetativeBulb.class, EntityDataSerializers.VECTOR3);
    private boolean hasSettled = false;

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void calculateAttachOrientation() {
        if(!hasSettled) {
            Vec3 origin = this.position().add(0, this.getBbHeight() / 2f, 0);
            if(this.level() instanceof ServerLevel slvl) {
                Vec3[] dirs = {
                        new Vec3(0, 1, 0), new Vec3(0, -1, 0),
                        new Vec3(1, 0, 0), new Vec3(-1, 0, 0),
                        new Vec3(0, 0, 1), new Vec3(0, 0, -1),
                };

                double dist = 1.2d;
                Vec3 closestNormal = null;
                double shortestDist = Double.MAX_VALUE;
                Vec3 closestPosition = null;

                for(Vec3 dir : dirs) {
                    BlockHitResult hitResult = slvl.clip(new ClipContext(origin, dir.scale(dist).add(origin), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

                    if(hitResult.getType() == HitResult.Type.BLOCK) {
                        double raycastDist = origin.distanceTo(hitResult.getLocation());

                        if(raycastDist < shortestDist) {
                            shortestDist = raycastDist;
                            closestNormal = new Vec3(
                                    hitResult.getDirection().getNormal().getX(),
                                    hitResult.getDirection().getNormal().getY(),
                                    hitResult.getDirection().getNormal().getZ()
                            );
                            closestPosition = hitResult.getLocation();
                        }
                    }
                }

                if(closestNormal != null) {
                    setAttachNormal(closestNormal);
                    this.moveTo(closestPosition);
                    hasSettled = true;
                }

            }
        }
    }

    public Map<Vector3f, Vec3> directionRotHashMap =
            new HashMap<>();

    @Override
    public void tick() {
        super.tick();
        Vector3f vec = entityData.get(ALIGNMENT);
        System.out.println(vec.x +" "+ vec.y +" "+ vec.z);
        Vec3 rot = directionRotHashMap.get(vec);
        System.out.println(rot.x +" "+ rot.y +" "+ rot.z);
        this.setYBodyRot(0);
    }

    public void setAttachNormal(Vec3 normal) {
        entityData.set(ALIGNMENT, normal.normalize().toVector3f());
    }

    public Vector3f getAttachNormal() {
        return entityData.get(ALIGNMENT);
    }

    public Vec3 getNormalRot() {
        return directionRotHashMap.get(entityData.get(ALIGNMENT));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        calculateAttachOrientation();

        directionRotHashMap.put(new Vector3f(1.0f, 0.0f, 0.0f), new Vec3(0, 0, 90));
        directionRotHashMap.put(new Vector3f(-1.0f, 0.0f, 0.0f), new Vec3(0, 0, -90));
        directionRotHashMap.put(new Vector3f(0.0f, 1.0f, 0.0f), new Vec3(0, 0, 0));
        directionRotHashMap.put(new Vector3f(0.0f, -1.0f, 0.0f), new Vec3(0, 0, 180));
        directionRotHashMap.put(new Vector3f(0.0f, 0.0f, 1.0f), new Vec3(-90, 0, 0));
        directionRotHashMap.put(new Vector3f(0.0f, 0.0f, -1.0f), new Vec3(90, 0, 0));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ALIGNMENT, new Vector3f(0, 1, 0));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("alignment_x", entityData.get(ALIGNMENT).x);
        tag.putFloat("alignment_y", entityData.get(ALIGNMENT).y);
        tag.putFloat("alignment_z", entityData.get(ALIGNMENT).z);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(ALIGNMENT, new Vector3f(
                tag.getFloat("alignment_x"),
                tag.getFloat("alignment_y"),
                tag.getFloat("alignment_z")
        ));
    }

    @Override
    public boolean isInWall() {
        return false;
    }
}
