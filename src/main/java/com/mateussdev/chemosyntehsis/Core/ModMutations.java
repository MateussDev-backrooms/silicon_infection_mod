package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing.MutationBurrowing;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationHarpoon.MutationHarpoon;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming.MutationSwimming;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationTeleportation.MutationTeleportation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.mateussdev.chemosyntehsis.Chemosynthesis.MODID;

public class ModMutations {
    public static final DeferredRegister<MutationType> MUTATION_TYPES =
            DeferredRegister.create(ModRegistries.MUTATION_TYPE_REGISTRY_KEY, MODID);

    public static final RegistryObject<MutationType> FLIGHT = MUTATION_TYPES.register("flight",
            () -> new MutationType(
                    new ResourceLocation(MODID, "flight"),
                    (mutationId) -> new MutationFlight(new ResourceLocation(MODID, "flight"), mutationId)
            ));
    public static final RegistryObject<MutationType> SWIMMING = MUTATION_TYPES.register("swimming",
            () -> new MutationType(
                    new ResourceLocation(MODID, "swimming"),
                    (mutationId) -> new MutationSwimming(new ResourceLocation(MODID, "swimming"), mutationId)
            ));
    public static final RegistryObject<MutationType> HARPOON = MUTATION_TYPES.register("harpoon",
            () -> new MutationType(
                    new ResourceLocation(MODID, "harpoon"),
                    (mutationId) -> new MutationHarpoon(new ResourceLocation(MODID, "harpoon"), mutationId)
            ));
    public static final RegistryObject<MutationType> BURROWING = MUTATION_TYPES.register("burrowing",
            () -> new MutationType(
                    new ResourceLocation(MODID, "burrowing"),
                    (mutationId) -> new MutationBurrowing(new ResourceLocation(MODID, "burrowing"), mutationId)
            ));
    public static final RegistryObject<MutationType> TELEPORTATION = MUTATION_TYPES.register("teleportation",
            () -> new MutationType(
                    new ResourceLocation(MODID, "teleportation"),
                    (mutationId) -> new MutationTeleportation(new ResourceLocation(MODID, "teleportation"), mutationId)
            ));

    public static void register(IEventBus eventBus) {
        MUTATION_TYPES.register(eventBus);
    }

    public static RegistryObject<MutationType> findMutationType(ResourceLocation id) {
        for(RegistryObject<MutationType> ro : MUTATION_TYPES.getEntries()) {
            if(ro.getId().equals(id)) return ro;
        }
        return null;
    }
}
