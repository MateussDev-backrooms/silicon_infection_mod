package com.mateussdev.chemosyntehsis.Entities.generic;


import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class BaseGib extends Entity implements GeoEntity {

    protected int age;
    protected int lifetime = -1;

    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(BaseGib.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(BaseGib.class, EntityDataSerializers.FLOAT);

    public BaseGib(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }



    private final AnimatableInstanceCache anim_cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return anim_cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    // ===== Core behavior ===== //

    @Override
    protected void defineSynchedData() {
        entityData.define(SCALE, random.nextFloat()/1.5f + 0.5f);
        entityData.define(ROTATION, random.nextFloat()*360f);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08, 0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.8));



        if (this.level() instanceof ServerLevel slvl) {
            if(lifetime > 0 && ++age > lifetime) {
                this.discard();
            }

            if(getDeltaMovement().length() > 0.01) {
                DustParticleOptions blood = new DustParticleOptions(
                        new Vector3f(0.8f, 0.0f, 0.0f),
                        2.0f
                );

                slvl.sendParticles(
                        blood,
                        position().x,
                        position().y,
                        position().z,
                        1,
                        0.0,
                        0.0,
                        0.0,
                        0.1
                );
            }
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("Age");
        lifetime = tag.getInt("Lifetime");
        entityData.set(SCALE, tag.getFloat("scale"));
        entityData.set(ROTATION, tag.getFloat("rotation"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", age);
        tag.putInt("Lifetime", lifetime);
        tag.putFloat("scale", entityData.get(SCALE));
        tag.putFloat("rotation", entityData.get(ROTATION));
    }

    @Override
    public boolean isPushable() {
        return true;
    }
}
