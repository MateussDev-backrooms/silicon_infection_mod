package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.stream.Collectors;

public class DSPCommand {
    // ---- Registration ----
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("dsp")
                        .requires(source -> source.hasPermission(2))

                        // Query (with optional radius)
                        .then(Commands.literal("query")
                                .executes(ctx -> executeQuery(ctx, 3))            // default radius = 3
                                .then(Commands.argument("radius", IntegerArgumentType.integer(1, 16))
                                        .executes(ctx -> executeQuery(ctx, IntegerArgumentType.getInteger(ctx, "radius")))))

                        // Add
                        .then(Commands.literal("add")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (DSPType type : DSPType.values()) {
                                                builder.suggest(type.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0.01f))
                                                .executes(DSPCommand::executeAddDSP))))

                        // Set (override)
                        .then(Commands.literal("set")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (DSPType type : DSPType.values()) {
                                                builder.suggest(type.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0f))
                                                .executes(DSPCommand::executeSetDSP))))

                        // Remove (subtract)
                        .then(Commands.literal("remove")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (DSPType type : DSPType.values()) {
                                                builder.suggest(type.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0.01f))
                                                .executes(DSPCommand::executeRemoveDSP))))

                        // Receptors query (unchanged)
                        .then(Commands.literal("receptors")
                                .then(Commands.literal("query")
                                        .executes(DSPCommand::executeReceptorQuery)))
        );
    }

    // ---- Query with radius ----
    private static int executeQuery(CommandContext<CommandSourceStack> ctx, int radius) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ChunkPos center = new ChunkPos(BlockPos.containing(source.getPosition()));

        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(level);

        // Gather all chunks within the square radius
        List<ChunkPos> chunksInRange = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                chunksInRange.add(new ChunkPos(center.x + dx, center.z + dz));
            }
        }

        // Collect all DSP types for column headers
        DSPType[] allTypes = DSPType.values();

        // Prepare table data: row = chunk, columns = DSP values
        Map<ChunkPos, EnumMap<DSPType, Float>> chunkData = new LinkedHashMap<>();
        for (ChunkPos pos : chunksInRange) {
            EnumMap<DSPType, Float> field = data.dspMap.get(pos);
            if (field == null || field.isEmpty()) {
                // Still include the chunk with zeros
                EnumMap<DSPType, Float> empty = new EnumMap<>(DSPType.class);
                for (DSPType type : allTypes) empty.put(type, 0f);
                chunkData.put(pos, empty);
            } else {
                // Fill missing types with 0
                for (DSPType type : allTypes) {
                    field.putIfAbsent(type, 0f);
                }
                chunkData.put(pos, field);
            }
        }

        // Build table header
        StringBuilder header = new StringBuilder();
        header.append("Chunk");
        for (DSPType type : allTypes) {
            header.append(String.format(" | %6s", abbreviateType(type)));
        }
        String separator = "-".repeat(header.length());

        source.sendSuccess(() -> Component.literal(
                "=== DSP Levels within " + radius + " chunks of [" + center.x + ", " + center.z + "] ==="
        ).withStyle(ChatFormatting.GOLD), false);

        source.sendSuccess(() -> Component.literal(header.toString()).withStyle(ChatFormatting.WHITE), false);
        source.sendSuccess(() -> Component.literal(separator).withStyle(ChatFormatting.DARK_GRAY), false);

        // Print each row
        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> entry : chunkData.entrySet()) {
            ChunkPos pos = entry.getKey();
            EnumMap<DSPType, Float> values = entry.getValue();

            StringBuilder row = new StringBuilder();
            row.append(String.format("[%3d, %3d]", pos.x, pos.z));
            for (DSPType type : allTypes) {
                float val = values.getOrDefault(type, 0f);
                String formatted = (val > 0) ? String.format("%6.1f", val) : "  0.0 ";
                row.append(" | ").append(formatted);
            }
            source.sendSuccess(() -> Component.literal(row.toString()).withStyle(ChatFormatting.GREEN), false);
        }

        return 1;
    }

    // ---- Add DSP ----
    private static int executeAddDSP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(BlockPos.containing(source.getPosition()));

        DSPType type = parseDSPType(ctx, source, "type");
        float amount = FloatArgumentType.getFloat(ctx, "amount");

        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(level);
        data.dspMap
                .computeIfAbsent(chunkPos, k -> new EnumMap<>(DSPType.class))
                .merge(type, amount, Float::sum);
        data.setDirty();

        source.sendSuccess(() -> Component.literal(
                String.format("Added %.2f %s to chunk [%d, %d]",
                        amount, type.name(), chunkPos.x, chunkPos.z)
        ).withStyle(ChatFormatting.AQUA), false);

        return 1;
    }

    // ---- Set DSP (override) ----
    private static int executeSetDSP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(BlockPos.containing(source.getPosition()));

        DSPType type = parseDSPType(ctx, source, "type");
        float amount = FloatArgumentType.getFloat(ctx, "amount");

        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(level);
        data.dspMap
                .computeIfAbsent(chunkPos, k -> new EnumMap<>(DSPType.class))
                .put(type, amount);
        data.setDirty();

        source.sendSuccess(() -> Component.literal(
                String.format("Set %s to %.2f in chunk [%d, %d]",
                        type.name(), amount, chunkPos.x, chunkPos.z)
        ).withStyle(ChatFormatting.AQUA), false);

        return 1;
    }

    // ---- Remove DSP (subtract) ----
    private static int executeRemoveDSP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(BlockPos.containing(source.getPosition()));

        DSPType type = parseDSPType(ctx, source, "type");
        float amount = FloatArgumentType.getFloat(ctx, "amount");

        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(level);
        EnumMap<DSPType, Float> field = data.dspMap.get(chunkPos);
        if (field == null) {
            source.sendFailure(Component.literal("No DSP data in this chunk."));
            return 0;
        }

        float current = field.getOrDefault(type, 0f);
        float newValue = Math.max(0f, current - amount);
        field.put(type, newValue);
        data.setDirty();

        source.sendSuccess(() -> Component.literal(
                String.format("Removed %.2f %s (new value: %.2f) in chunk [%d, %d]",
                        amount, type.name(), newValue, chunkPos.x, chunkPos.z)
        ).withStyle(ChatFormatting.AQUA), false);

        return 1;
    }

    // ---- Receptors query (unchanged, but we keep it) ----
    private static int executeReceptorQuery(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        BlockPos origin = BlockPos.containing(source.getPosition());

        List<Mob> receptors = level.getEntitiesOfClass(
                Mob.class,
                new AABB(origin).inflate(32),
                e -> e instanceof IDspReceptor
        );

        if (receptors.isEmpty()) {
            source.sendSuccess(() -> Component.literal(
                    "No DSP receptors found within 32 blocks."
            ).withStyle(ChatFormatting.GRAY), false);
            return 1;
        }

        List<Mob> active = receptors.stream()
                .filter(m -> ((IDspReceptor) m).getInternalBuffer().values().stream().anyMatch(v -> v > 0f))
                .toList();

        List<Mob> empty = receptors.stream()
                .filter(m -> ((IDspReceptor) m).getInternalBuffer().values().stream().noneMatch(v -> v > 0f))
                .toList();

        source.sendSuccess(() -> Component.literal(
                "=== Directive Signaling Protein Receptors within 32 blocks (" + receptors.size() + " found) ==="
        ).withStyle(ChatFormatting.GOLD), false);

        for (Mob mob : active) {
            printReceptor(source, origin, mob);
        }

        if (!empty.isEmpty()) {
            source.sendSuccess(() -> Component.literal(
                    "--- Empty Buffers (" + empty.size() + ") ---"
            ).withStyle(ChatFormatting.DARK_GRAY), false);

            for (Mob mob : empty) {
                BlockPos mobPos = mob.blockPosition();
                double distance = Math.sqrt(origin.distSqr(mobPos));
                source.sendSuccess(() -> Component.literal(
                        String.format("  [%s] %s @ (%.0f, %.0f, %.0f) | dist: %.1f",
                                mob.getUUID().toString().substring(0, 8),
                                mob.getType().getDescriptionId(),
                                mob.getX(), mob.getY(), mob.getZ(),
                                distance)
                ).withStyle(ChatFormatting.DARK_GRAY), false);
            }
        }

        return 1;
    }

    private static void printReceptor(CommandSourceStack source, BlockPos origin, Mob mob) {
        IDspReceptor receptor = (IDspReceptor) mob;
        EnumMap<DSPType, Float> buffer = receptor.getInternalBuffer();

        BlockPos mobPos = mob.blockPosition();
        double distance = Math.sqrt(origin.distSqr(mobPos));

        source.sendSuccess(() -> Component.literal(
                String.format("  [%s] %s @ (%.0f, %.0f, %.0f) | dist: %.1f",
                        mob.getUUID().toString().substring(0, 8),
                        mob.getType().getDescriptionId(),
                        mob.getX(), mob.getY(), mob.getZ(),
                        distance)
        ).withStyle(ChatFormatting.YELLOW), false);

        for (DSPType type : DSPType.values()) {
            float value = buffer.getOrDefault(type, 0f);
            if (value <= 0f) continue;

            float capacity = receptor.bufferCapacity();
            int filled = (int) ((value / capacity) * 20);
            String bar = "#".repeat(filled) + "-".repeat(20 - filled);

            ChatFormatting barColor = value / capacity > 0.75f ? ChatFormatting.RED
                    : value / capacity > 0.4f  ? ChatFormatting.YELLOW
                      : ChatFormatting.GREEN;

            source.sendSuccess(() -> Component.literal(
                    String.format("    %-35s %s %.1f/%.0f", type.name(), bar, value, capacity)
            ).withStyle(barColor), false);
        }

        List<DSPThreshold> thresholds = receptor.getThresholds();
        if (!thresholds.isEmpty()) {
            source.sendSuccess(() -> Component.literal(
                    "    Thresholds:"
            ).withStyle(ChatFormatting.AQUA), false);

            for (DSPThreshold threshold : thresholds) {
                float current = buffer.getOrDefault(threshold.type(), 0f);
                boolean ready = current >= threshold.threshold();
                source.sendSuccess(() -> Component.literal(
                        String.format("      %s %.1f/%.0f %s",
                                threshold.type().name(),
                                current,
                                threshold.threshold(),
                                ready ? "[READY]" : "")
                ).withStyle(ready ? ChatFormatting.RED : ChatFormatting.GRAY), false);
            }
        }
    }

    // ---- Helper: parse DSPType with error handling ----
    private static DSPType parseDSPType(CommandContext<CommandSourceStack> ctx, CommandSourceStack source, String argName) throws CommandSyntaxException {
        String typeName = StringArgumentType.getString(ctx, argName);
        try {
            return DSPType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal(
                    "Unknown DSP type: '" + typeName + "'. Valid types: " +
                            Arrays.stream(DSPType.values())
                                    .map(DSPType::name)
                                    .collect(Collectors.joining(", "))
            ));
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
        }
    }

    // ---- Helper: abbreviate DSP type (e.g., "D_D_DAMAGEDIRECTIVE" → "D_D") ----
    private static String abbreviateType(DSPType type) {
        String name = type.name();
        String[] parts = name.split("_");
        if (parts.length >= 2) {
            return parts[0] + "_" + parts[1];
        }
        return name; // fallback
    }
}
