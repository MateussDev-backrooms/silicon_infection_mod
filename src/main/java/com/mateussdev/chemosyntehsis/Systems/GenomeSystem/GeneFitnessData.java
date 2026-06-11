package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import java.util.UUID;

public class GeneFitnessData {
    private final Gene gene;
    private final UUID geneId;
    private float fitness = 0;

    public GeneFitnessData(Gene gene, UUID geneId) {
        this.gene = gene;
        this.geneId = geneId;
    }

    public void addFitness(float points) { this.fitness += points; }
    public float getFitness() { return fitness; }
    public Gene getGene() { return gene; }
    public UUID getGeneId() { return geneId; }
}
