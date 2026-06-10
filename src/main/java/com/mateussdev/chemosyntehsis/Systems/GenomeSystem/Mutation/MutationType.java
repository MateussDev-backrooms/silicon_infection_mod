package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class MutationType {
    private final ResourceLocation id;
    private final Supplier<Mutation> clientFactory; // only used on client

    public MutationType(ResourceLocation id, Supplier<Mutation> clientFactory) {
        this.id = id;
        this.clientFactory = clientFactory;
    }

    public ResourceLocation getId() { return id; }
    public Mutation createClientSide() { return clientFactory.get(); }
}
