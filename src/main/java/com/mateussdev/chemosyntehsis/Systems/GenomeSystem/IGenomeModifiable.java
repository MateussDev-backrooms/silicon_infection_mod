package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

public interface IGenomeModifiable {

    boolean applyGene(Gene gene);
    Gene getGene();

    void setGeneOrigin(UUID homunculusId);
    UUID getGeneOrigin();
    void reportFitness(float points);

    boolean hasMutationType(ResourceLocation typeID);

}
