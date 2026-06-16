package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
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

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

public class DSPCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("dsp")
                        .requires(source -> source.hasPermission(2))

                        .then(Commands.literal("query")
                                .executes(DSPCommand::executeQuery))

                        .then(Commands.literal("add")
                                .then(Commands.argument("type", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            for (DSPType type : DSPType.values()) {
                                                builder.suggest(type.name());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes(DSPCommand::executeAddDSP))))
                        .then(Commands.literal("receptors")
                                .then(Commands.literal("query")
                                        .executes(DSPCommand::executeReceptorQuery)))
        );
    }

    private static int executeQuery(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(BlockPos.containing(source.getPosition()));

        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(level);
        EnumMap<DSPType, Float> field = data.dspMap.get(chunkPos);

        source.sendSuccess(() -> Component.literal(
                "=== Directive Signaling Protein Levels at Chunk [" + chunkPos.x + ", " + chunkPos.z + "] ==="
        ).withStyle(ChatFormatting.GOLD), false);

        if (field == null || field.isEmpty()) {
            source.sendSuccess(() -> Component.literal(
                    "No DSP present in this chunk."
            ).withStyle(ChatFormatting.GRAY), false);
            return 1;
        }

        for (DSPType type : DSPType.values()) {
            float value = field.getOrDefault(type, 0f);
            ChatFormatting color = value > 0f ? ChatFormatting.GREEN : ChatFormatting.DARK_GRAY;
            source.sendSuccess(() -> Component.literal(
                    String.format("  %-35s %.2f", type.name(), value)
            ).withStyle(color), false);
        }

        return 1;
    }

    private static int executeAddDSP(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerLevel level = source.getLevel();
        ChunkPos chunkPos = new ChunkPos(BlockPos.containing(source.getPosition()));

        String typeName = StringArgumentType.getString(ctx, "type");
        float amount = FloatArgumentType.getFloat(ctx, "amount");

        DSPType type;
        try {
            type = DSPType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            source.sendFailure(Component.literal(
                    "Unknown DSP type: '" + typeName + "'. Valid types: " +
                            Arrays.stream(DSPType.values())
                                    .map(DSPType::name)
                                    .collect(Collectors.joining(", "))
            ));
            return 0;
        }

        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(level);
        data.dspMap
                .computeIfAbsent(chunkPos, k -> new EnumMap<>(DSPType.class))
                .merge(type, amount, Float::sum);
        data.setDirty();

        DSPType finalType = type;
        source.sendSuccess(() -> Component.literal(
                String.format("Added %.2f %s to chunk [%d, %d]",
                        amount, finalType.name(), chunkPos.x, chunkPos.z)
        ).withStyle(ChatFormatting.AQUA), false);

        return 1;
    }

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
}
