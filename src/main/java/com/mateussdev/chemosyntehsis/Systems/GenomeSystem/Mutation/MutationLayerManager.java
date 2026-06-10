package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class MutationLayerManager {
    private static final Map<GeoRenderer<?>, Set<ResourceLocation>> addedLayers = new WeakHashMap<>();

    public static boolean hasLayer(GeoRenderer<?> renderer, ResourceLocation mutationId) {
        return addedLayers.getOrDefault(renderer, Collections.emptySet()).contains(mutationId);
    }

    public static void markLayerAdded(GeoRenderer<?> renderer, ResourceLocation mutationId) {
        addedLayers.computeIfAbsent(renderer, k -> new HashSet<>()).add(mutationId);
    }
}