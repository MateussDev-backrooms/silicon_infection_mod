package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Random;

public class GenomeCommand {

    private static final SimpleCommandExceptionType ERROR_NOT_A_MOB =
            new SimpleCommandExceptionType(Component.literal("Target must be a living mob"));
    private static final SimpleCommandExceptionType ERROR_NO_GENOME =
            new SimpleCommandExceptionType(Component.literal("Target does not support genome modification"));
    private static final SimpleCommandExceptionType ERROR_UNKNOWN_MUTATION =
            new SimpleCommandExceptionType(Component.literal("Unknown mutation type"));
    private static final SimpleCommandExceptionType ERROR_CANNOT_MUTATE =
            new SimpleCommandExceptionType(Component.literal("This mob cannot accept that mutation"));

    // Suggestion provider for mutation types (reads from DeferredRegister)
    public static final SuggestionProvider<CommandSourceStack> MUTATION_SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    ModMutations.MUTATION_TYPES.getEntries().stream()
                            .map(ro -> ro.getId().getPath())
                            .toArray(String[]::new),
                    builder
            );

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("genome")
                        .requires(source -> source.hasPermission(2)) // op permission level
                        .then(Commands.argument("entity", EntityArgument.entity())
                                .then(Commands.literal("add_mutation")
                                        .then(Commands.argument("mutation_type", StringArgumentType.word())
                                                .suggests(MUTATION_SUGGESTIONS)
                                                .executes(GenomeCommand::addMutation)
                                        )
                                )
                        )
        );
    }

    private static int addMutation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx, "entity");
        if (!(entity instanceof Mob mob)) {
            throw ERROR_NOT_A_MOB.create();
        }
        if (!(mob instanceof IGenomeModifiable genMod)) {
            throw ERROR_NO_GENOME.create();
        }

        String mutationName = StringArgumentType.getString(ctx, "mutation_type");
        ResourceLocation mutationId = new ResourceLocation(Chemosynthesis.MODID, mutationName);
        RegistryObject<MutationType> ro = ModMutations.findMutationType(mutationId);
        if (ro == null) {
            throw ERROR_UNKNOWN_MUTATION.create();
        }
        MutationType type = ro.get();

        // Create a new mutation instance with a unique ID (positive integer)
        int uniqueId = new Random().nextInt(Integer.MAX_VALUE);
        Mutation newMutation = type.create(uniqueId);

        // Check if the mob can accept this mutation
        if (!newMutation.canMutateMob(mob)) {
            throw ERROR_CANNOT_MUTATE.create();
        }

        // Get or create the gene
        Gene gene = genMod.getGene();
        if (gene == null) {
            gene = new Gene();        // assume Gene has a default constructor
        }

        // Optional: prevent duplicate mutations of the same type
        if (gene.mutations.stream().anyMatch(m -> m.getTypeId().equals(mutationId))) {
            ctx.getSource().sendSuccess(() -> Component.literal("Mob already has that mutation"), false);
            return 0;
        }

        // Add the mutation and apply the gene again
        gene.mutations.add(newMutation);
        genMod.applyGene(gene);

        ctx.getSource().sendSuccess(
                () -> Component.literal("Added mutation '" + mutationName + "' to " + mob.getName().getString()),
                true
        );
        return 1;
    }
}
