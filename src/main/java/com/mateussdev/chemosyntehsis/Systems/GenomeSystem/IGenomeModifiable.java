package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

public interface IGenomeModifiable {
    boolean applyGene(Gene gene);
    Gene getGene();
    void clearAllGenes();

    void addFitnessPoints(int deltaPoints);
    void removeFitnessPoints(int deltaPoints);

}
