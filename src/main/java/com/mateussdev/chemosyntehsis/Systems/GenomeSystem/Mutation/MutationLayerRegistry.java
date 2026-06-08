package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight_Layer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class MutationLayerRegistry {
    //Used for associating classNames to actual renderers for the Mutation Sync Packet
    //Woah networking...

    private static final Map<String, Function<GeoRenderer<?>, GeoRenderLayer<?>>> REGISTRY = new HashMap<>();

    static {
        REGISTRY.put("MutationFlight", renderer -> new MutationFlight_Layer<>(renderer));
        // Register further mutations here
    }

    public static GeoRenderLayer<?> createLayer(String mutationName, GeoRenderer<?> renderer) {
        var factory = REGISTRY.get(mutationName);
        return factory != null ? factory.apply(renderer) : null;
    }
}
