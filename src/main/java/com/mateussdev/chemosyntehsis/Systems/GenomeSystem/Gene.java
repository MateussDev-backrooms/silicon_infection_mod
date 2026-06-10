package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gene {
    public UUID id;

    public Gene() {
        this.id = UUID.randomUUID();
        this.mutations = new ArrayList<>();
    }
    //A gene is a set of attribute modifiers and mutations

    //A list of mutations, which will alter the behavior of the recepient
    public List<Mutation> mutations = new ArrayList<>();

    public void addMutation(Mutation mutation) {
        mutations.add(mutation);
        //TODO: Data validation
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

}
