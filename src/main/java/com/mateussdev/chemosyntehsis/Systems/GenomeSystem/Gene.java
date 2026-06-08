package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Gene {
    public UUID id;

    //A gene is a set of attribute modifiers and mutations

    //A list of mutations, which will alter the behavior of the recepient
    public List<Mutation> mutations = new ArrayList<>();

    public void addMutation(Mutation mutation) {
        mutations.add(mutation);
        //TODO: Data validation
    }

}
