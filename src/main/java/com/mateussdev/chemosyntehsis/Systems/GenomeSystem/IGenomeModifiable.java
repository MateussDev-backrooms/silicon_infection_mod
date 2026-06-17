package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import java.util.List;
import java.util.UUID;

public interface IGenomeModifiable {

    boolean applyGene(Gene gene);
    Gene getGene();

    void setGeneOrigin(UUID homunculusId);
    UUID getGeneOrigin();
    void reportFitness(float points);

    boolean hasMutationType(ResourceLocation typeID);

    //Client-server sync stuffs
    EntityDataAccessor<CompoundTag> getMutationStateAccessor();
    default CompoundTag getMutationState(Mob mob, ResourceLocation mutationTypeId, EntityDataAccessor<CompoundTag> mutationStateAccessor) {
        CompoundTag allStates = mob.getEntityData().get(mutationStateAccessor);
        return allStates.getCompound(mutationTypeId.toString());
    }

    default void setMutationState(Mob mob, ResourceLocation mutationTypeId, EntityDataAccessor<CompoundTag> mutationStateAccessor, CompoundTag state) {
        CompoundTag allStates = mob.getEntityData().get(mutationStateAccessor);
        // Copy to force dirty flag
        CompoundTag newAllStates = allStates.copy();
        newAllStates.put(mutationTypeId.toString(), state);
        mob.getEntityData().set(mutationStateAccessor, newAllStates);
    }

}
