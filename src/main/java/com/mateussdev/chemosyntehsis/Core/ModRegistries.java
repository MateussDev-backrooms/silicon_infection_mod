package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;


@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {
    public static final ResourceLocation MUTATION_TYPE_REGISTRY_KEY =
            new ResourceLocation(Chemosynthesis.MODID, "mutation_type");

    // The actual registry instance – filled during NewRegistryEvent
    public static IForgeRegistry<MutationType> MUTATION_TYPE_REGISTRY = null;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        RegistryBuilder<MutationType> builder = new RegistryBuilder<>();
        builder.setName(MUTATION_TYPE_REGISTRY_KEY);
        // Optional: set a default key if needed
        // builder.setDefaultKey(new ResourceLocation(Chemosynthesis.MODID, "empty"));
        MUTATION_TYPE_REGISTRY = event.create(builder).get();
    }
}