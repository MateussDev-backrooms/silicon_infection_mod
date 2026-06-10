package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight_Layer;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class MutationLayerRegistry {
    //Used for associating classNames to actual renderers for the Mutation Sync Packet
    //Woah networking...

    private static final Map<String, BiFunction<GeoRenderer<?>, Integer, GeoRenderLayer<?>>> REGISTRY = new HashMap<>();

    static {
//        REGISTRY.put("MutationFlight", (renderer, entityId) -> new MutationFlight_Layer<>(renderer, entityId)); //TODO: Fix
    }



    //Lame but should work - set up the animation for all the mutations - they will do their thing only if the mutation is present
//    public static void registerAllMutationAnimControllers(GeoAnimatable mob, AnimatableManager.ControllerRegistrar controllers) {
//        //MutationFlight
//        controllers.add(new AnimationController<>(mob, "mutation_flight", 5, event -> {
//            if (true) {
//                StaticSiliconiteMethods.debugLog("running animations");
//                return event.setAndContinue(RawAnimation.begin().thenLoop("active"));
//            }
//            StaticSiliconiteMethods.debugLog("not running animations");
//            return PlayState.STOP;
//        }));
//    }

    public static GeoRenderLayer<?> createLayer(String mutationName, GeoRenderer<?> renderer, int entityId) {
        var factory = REGISTRY.get(mutationName);
        return factory != null ? factory.apply(renderer, entityId) : null;
    }
}
