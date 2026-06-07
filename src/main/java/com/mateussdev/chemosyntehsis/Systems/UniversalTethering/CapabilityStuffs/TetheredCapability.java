package com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs;

import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.ITetheredHook;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public class TetheredCapability
{
    private boolean tethered = false;


    private final java.util.List<ITetheredHook> hooks = new java.util.ArrayList<>();

    public boolean isTethered() { return tethered; }

    public void setTethered(boolean val) { this.tethered = val; }

    public void addHook(ITetheredHook hook) { hooks.add(hook); }

    public java.util.List<ITetheredHook> getHooks() { return hooks; }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("tethered", tethered);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        this.tethered = tag.getBoolean("tethered");
    }
}
