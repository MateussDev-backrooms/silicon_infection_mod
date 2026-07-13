package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class GenomeCommand {

    private static final SimpleCommandExceptionType ERROR_NOT_A_MOB =
            new SimpleCommandExceptionType(Component.literal("Target must be a living mob"));
    private static final SimpleCommandExceptionType ERROR_NO_GENOME =
            new SimpleCommandExceptionType(Component.literal("Target does not support genome modification"));
    private static final SimpleCommandExceptionType ERROR_UNKNOWN_MUTATION =
            new SimpleCommandExceptionType(Component.literal("Unknown mutation type"));
    private static final SimpleCommandExceptionType ERROR_CANNOT_MUTATE =
            new SimpleCommandExceptionType(Component.literal("This mob cannot accept that mutation"));
    private static final SimpleCommandExceptionType ERROR_NO_GENE =
            new SimpleCommandExceptionType(Component.literal("Target has no gene"));
    private static final SimpleCommandExceptionType ERROR_INVALID_COUNT =
            new SimpleCommandExceptionType(Component.literal("Count must be between 1 and the total number of registered mutations"));

    // Suggestion provider for mutation types (reads from DeferredRegister)
    public static final SuggestionProvider<CommandSourceStack> MUTATION_SUGGESTIONS =
            (ctx, builder) -> SharedSuggestionProvider.suggest(
                    ModMutations.MUTATION_TYPES.getEntries().stream()
                            .map(ro -> ro.getId().getPath())
                            .toArray(String[]::new),
                    builder
            );

    public static void register(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("genome")
                        .requires(source -> source.hasPermission(2)) // op permission level
                        .then(Commands.argument("entity", EntityArgument.entity())
                                // add_mutation
                                .then(Commands.literal("add_mutation")
                                        .then(Commands.argument("mutation_type", StringArgumentType.word())
                                                .suggests(MUTATION_SUGGESTIONS)
                                                .executes(GenomeCommand::addMutation)
                                        )
                                )
                                // remove_mutation
                                .then(Commands.literal("remove_mutation")
                                        .then(Commands.argument("mutation_type", StringArgumentType.word())
                                                .suggests(MUTATION_SUGGESTIONS)
                                                .executes(GenomeCommand::removeMutation)
                                        )
                                )
                                // remove_all_mutations
                                .then(Commands.literal("remove_all_mutations")
                                        .executes(GenomeCommand::removeAllMutations)
                                )
                                // list_mutations
                                .then(Commands.literal("list_mutations")
                                        .executes(GenomeCommand::listMutations)
                                )
                        )
        );
    }

    // ---------- add_mutation ----------
    private static int addMutation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx, "entity");
        Mob mob = validateMob(entity);
        IGenomeModifiable genMod = validateGenMod(mob);

        String mutationName = StringArgumentType.getString(ctx, "mutation_type");
        ResourceLocation mutationId = new ResourceLocation(Chemosynthesis.MODID, mutationName);
        MutationType type = getMutationType(mutationId);

        // Create a new mutation instance with a unique ID
        int uniqueId = new Random().nextInt(Integer.MAX_VALUE);
        Mutation newMutation = type.create(uniqueId);

        // Check if the mob can accept this mutation
        if (!newMutation.canMutateMob(mob)) {
            throw ERROR_CANNOT_MUTATE.create();
        }

        //Warn if the mutation wouldn't be added during normal gameplay
        if(!isTierFree(newMutation, genMod.getGene().mutations)) {
            ctx.getSource().sendSuccess(() -> Component.literal("Warning: The "+mutationName+" mutation is of the same tier as another mutation in the mob. This combination will not be possible during normal gameplay").withStyle(ChatFormatting.YELLOW), false);
        }

        // Get or create the gene
        Gene gene = genMod.getGene();
        if (gene == null) {
            gene = new Gene();
        }

        // Prevent duplicate mutations of the same type
        if (gene.mutations.stream().anyMatch(m -> m.getTypeId().equals(mutationId))) {
            ctx.getSource().sendSuccess(() -> Component.literal("Mob already has that mutation"), false);
            return 0;
        }

        gene.mutations.add(newMutation);
        genMod.setGeneOrigin(UUID.randomUUID());
        genMod.applyGene(gene);

        ctx.getSource().sendSuccess(
                () -> Component.literal("Added mutation '" + mutationName + "' to " + mob.getName().getString()),
                true
        );
        return 1;
    }

    private static boolean isTierFree(Mutation comparator, List<Mutation> mutations) {
        int tier = comparator.tier();
        for(Mutation mut : mutations) {
            if(mut.tier() == tier) {
                return false;
            }
        }
        return true;
    }

    // ---------- remove_mutation ----------
    private static int removeMutation(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx, "entity");
        Mob mob = validateMob(entity);
        IGenomeModifiable genMod = validateGenMod(mob);

        String mutationName = StringArgumentType.getString(ctx, "mutation_type");
        ResourceLocation mutationId = new ResourceLocation(Chemosynthesis.MODID, mutationName);

        Gene gene = genMod.getGene();
        if (gene == null) {
            throw ERROR_NO_GENE.create();
        }

        // Find and remove the mutation
        boolean removed = gene.mutations.removeIf(m -> m.getTypeId().equals(mutationId));
        if (!removed) {
            ctx.getSource().sendFailure(Component.literal("Mob does not have mutation '" + mutationName + "'"));
            return 0;
        }

        // Re-apply the gene (this will call onRemove on the removed mutation and re-init the rest)
        genMod.applyGene(gene);

        ctx.getSource().sendSuccess(
                () -> Component.literal("Removed mutation '" + mutationName + "' from " + mob.getName().getString()),
                true
        );
        return 1;
    }

    // ---------- remove_all_mutations ----------
    private static int removeAllMutations(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx, "entity");
        Mob mob = validateMob(entity);
        IGenomeModifiable genMod = validateGenMod(mob);

        Gene gene = genMod.getGene();
        if (gene == null || gene.mutations.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Mob has no mutations to remove"));
            return 0;
        }

        gene.mutations.clear();
        genMod.applyGene(gene); // re-apply to reset AI etc.

        ctx.getSource().sendSuccess(
                () -> Component.literal("Removed all mutations from " + mob.getName().getString()),
                true
        );
        return 1;
    }

    // ---------- list_mutations ----------
    private static int listMutations(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(ctx, "entity");
        Mob mob = validateMob(entity);
        IGenomeModifiable genMod = validateGenMod(mob);

        Gene gene = genMod.getGene();
        if (gene == null || gene.mutations.isEmpty()) {
            ctx.getSource().sendSuccess(() -> Component.literal("No mutations present"), false);
            return 0;
        }

        // Get the mutation state compound (the entire MUTATION_STATE)
        CompoundTag allStates = mob.getEntityData().get(genMod.getMutationStateAccessor());

        ctx.getSource().sendSuccess(() -> Component.literal("--- Mutations for " + mob.getName().getString() + " ---"), false);
        for (Mutation mutation : gene.mutations) {
            ResourceLocation typeId = mutation.getTypeId();
            String typeName = typeId.getPath();
            CompoundTag state = allStates.getCompound(typeId.toString());

            // Build a readable string of the state
            String stateStr = state.isEmpty() ? "{}" : state.toString(); // simple, but you can customise

            ctx.getSource().sendSuccess(
                    () -> Component.literal("- " + typeName + ": " + stateStr),
                    false
            );
        }
        return gene.mutations.size();
    }

    // ---------- Utility methods ----------
    private static Mob validateMob(Entity entity) throws CommandSyntaxException {
        if (!(entity instanceof Mob mob)) {
            throw ERROR_NOT_A_MOB.create();
        }
        return mob;
    }

    private static IGenomeModifiable validateGenMod(Mob mob) throws CommandSyntaxException {
        if (!(mob instanceof IGenomeModifiable genMod)) {
            throw ERROR_NO_GENOME.create();
        }
        return genMod;
    }

    private static MutationType getMutationType(ResourceLocation id) throws CommandSyntaxException {
        RegistryObject<MutationType> ro = ModMutations.findMutationType(id);
        if (ro == null) {
            throw ERROR_UNKNOWN_MUTATION.create();
        }
        return ro.get();
    }
}
