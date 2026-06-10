package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
import java.util.function.Supplier;

public class MutationType {
    private final ResourceLocation id;
    private final Function<Integer, Mutation> factory; // now takes mutationId

    public MutationType(ResourceLocation id, Function<Integer, Mutation> factory) {
        this.id = id;
        this.factory = factory;
    }

    public ResourceLocation getId() { return id; }

    public Mutation create(int mutationId) {
        return factory.apply(mutationId);
    }

    public Mutation createClientSide() {
        return factory.apply(0);
    }
}
