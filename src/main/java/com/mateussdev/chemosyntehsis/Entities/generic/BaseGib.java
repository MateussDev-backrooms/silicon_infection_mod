package com.mateussdev.chemosyntehsis.Entities.generic;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;

public abstract class BaseGib extends Entity implements GeoEntity {

    protected int age;
    protected int lifetime = -1;

    protected BaseGib(EntityType<?> type, Level level) {
        super(type, level);
    }

    private final AnimatableInstanceCache anim_cache = AzureLibUtil.createInstanceCache(this);
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
    protected void defineSynchedData() {}

    @Override
    public void tick() {
        super.tick();

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08, 0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.8));

        if (!this.level().isClientSide && lifetime > 0 && ++age > lifetime) {
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("Age");
        lifetime = tag.getInt("Lifetime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Age", age);
        tag.putInt("Lifetime", lifetime);
    }

    @Override
    public boolean isPushable() {
        return true;
    }
}
