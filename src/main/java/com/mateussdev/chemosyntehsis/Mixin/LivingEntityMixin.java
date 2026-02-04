package com.mateussdev.chemosyntehsis.Mixin;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.ITethered;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ITethered {
    @Unique
    private SynchedEntityData dataTracker(){
        return ((LivingEntity) (Object) this).getEntityData();
    }

    @Override
    public void setTethered(boolean tethered) {
        LivingEntity self = (LivingEntity)(Object)this;
        self.getEntityData().set(IS_TETHERED, tethered);
        // DEBUG LOG
        if (!self.level().isClientSide) {
            StaticSiliconiteMethods.debugLog("Set tethered to {"+tethered+"} for entity: "+self.getName().getString());
        }
    }

    @Override
    public boolean isTethered() {
        return ((LivingEntity)(Object)this).getEntityData().get(IS_TETHERED);
    }

    @Unique
    private void test() {
        StaticSiliconiteMethods.debugLog("mixin is workering");
    }

    @Unique
    private static final EntityDataAccessor<Boolean> IS_TETHERED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    @Inject(at = @At("HEAD"), method = "addAdditionalSaveData")
    private void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        tag.putBoolean(Chemosynthesis.MODID + "_is_tethered", this.isTethered());
    }

    @Inject(at = @At("HEAD"), method = "readAdditionalSaveData")
    private void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(Chemosynthesis.MODID + "_is_tethered")) {
            this.setTethered(tag.getBoolean(Chemosynthesis.MODID + "_is_tethered"));
        }
        StaticSiliconiteMethods.debugLog("Blah loaded tethered");
    }

    @Inject(at = @At("HEAD"), method = "defineSynchedData")
    private void defineSynchedData(CallbackInfo ci) {
        ((LivingEntity)(Object)this).getEntityData().define(IS_TETHERED, false);
    }
}
