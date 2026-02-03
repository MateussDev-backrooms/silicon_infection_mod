package com.mateussdev.chemosyntehsis.Mixin;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private SynchedEntityData dataTracker(){
        return ((LivingEntity) (Object) this).getEntityData();
    }

    @Unique
    private static final EntityDataAccessor<Boolean> IS_TETHERED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean(Chemosynthesis.MODID+"_is_tethered", dataTracker().get(IS_TETHERED));
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if(tag.contains(Chemosynthesis.MODID+"_is_tethered")) {
            dataTracker().set(IS_TETHERED, tag.getBoolean(Chemosynthesis.MODID+"_is_tethered"));
        }
    }

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void defineSynchedData(CallbackInfo ci) {

    }
}
