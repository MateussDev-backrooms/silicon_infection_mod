package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Gene {
    public UUID id;

    public Gene() {
        this.id = UUID.randomUUID();
        this.mutations = new ArrayList<>();
    }

    public Gene copy(Random rng) {
        Gene copy = new Gene();
        copy.id = UUID.randomUUID();
        for (Mutation m : this.mutations) {
            copy.mutations.add(m.copy(rng));
        }
        copy.attributeMutation = this.attributeMutation.copy();
        return copy;
    }

    //A gene is a set of attribute modifiers and mutations

    //A list of mutations, which will alter the behavior of the recepient
    public List<Mutation> mutations = new ArrayList<>();

    //One mutation specifically for changing the attributes
    public GeneAttributeMutation attributeMutation = new GeneAttributeMutation();

    public void addMutation(Mutation mutation) {
        mutations.add(mutation);
        //TODO: Data validation
    }

    public int getCost() {
        int collectedCost = 0;
        for(Mutation mutation : mutations) collectedCost += mutation.getCost();

        collectedCost += attributeMutation.calculateCost();
        return collectedCost;
    }

    // ===== Saving Loading ===== //

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        // Store gene UUID
        if (id != null) tag.putUUID("id", id);
        // Store mutations list
        CompoundTag mutationsTag = new CompoundTag();
        for (int i = 0; i < mutations.size(); i++) {
            Mutation m = mutations.get(i);
            CompoundTag mutTag = m.serialize(); // we'll implement this in Mutation
            mutationsTag.put(String.valueOf(i), mutTag);
        }
        tag.put("mutations", mutationsTag);
        return tag;
    }

    public static Gene deserialize(CompoundTag tag) {
        Gene gene = new Gene();
        // Load UUID
        if (tag.hasUUID("id")) {
            gene.id = tag.getUUID("id");
        } else {
            gene.id = UUID.randomUUID(); // or generate new
        }
        // Load mutations
        CompoundTag mutationsTag = tag.getCompound("mutations");
        for (String key : mutationsTag.getAllKeys()) {
            CompoundTag mutTag = mutationsTag.getCompound(key);
            Mutation mutation = Mutation.deserialize(mutTag);
            if (mutation != null) {
                gene.mutations.add(mutation);
            }
        }
        return gene;
    }

    @Override
    public String toString() {
        String mutationsStr = "";
        for(Mutation mutation : mutations) {
            mutationsStr += "; "+mutation.toString();
        }
        return "Gene: "+this.id+" :"+mutationsStr+"; attribute_modification: ";
    }
}
