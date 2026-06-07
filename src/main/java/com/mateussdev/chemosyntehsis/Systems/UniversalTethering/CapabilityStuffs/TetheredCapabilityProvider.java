package com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TetheredCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static final Capability<TetheredCapability> TETHERED_CAP =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final TetheredCapability instance = new TetheredCapability();
    private final LazyOptional<TetheredCapability> lazyOptional = LazyOptional.of(() -> instance);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return TETHERED_CAP.orEmpty(cap, lazyOptional);
    }

    @Override
    public CompoundTag serializeNBT() { return instance.serializeNBT(); }

    @Override
    public void deserializeNBT(CompoundTag tag) { instance.deserializeNBT(tag); }

    public void invalidate() { lazyOptional.invalidate(); }
}
