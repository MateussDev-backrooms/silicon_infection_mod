package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public interface IGenomeModifiable {

    boolean applyGene(Gene gene);
    Gene getGene();
    void clearAllGenes();

    void addFitnessPoints(int deltaPoints);
    void removeFitnessPoints(int deltaPoints);

    boolean hasMutationType(ResourceLocation typeID);

}
