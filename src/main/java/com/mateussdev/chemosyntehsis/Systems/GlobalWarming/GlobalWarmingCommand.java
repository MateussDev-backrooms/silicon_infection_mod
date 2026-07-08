package com.mateussdev.chemosyntehsis.Systems.GlobalWarming;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class GlobalWarmingCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("globalwarming")
                .then(Commands.literal("add")
                        .then(Commands.argument("points", IntegerArgumentType.integer())
                                .executes(ctx -> {
                                    int points = IntegerArgumentType.getInteger(ctx, "points");
                                    ServerLevel level = ctx.getSource().getLevel();
                                    GlobalWarmingData data = GlobalWarmingData.get(level);
                                    data.addPoints(points);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Added " + points + " greenhouse points."), false);
                                    return 1;
                                })))
                .then(Commands.literal("addDegrees")
                        .then(Commands.argument("degrees", FloatArgumentType.floatArg())
                                .executes(ctx -> {
                                    float degrees = FloatArgumentType.getFloat(ctx, "degrees");
                                    int points = (int)(degrees * 100);
                                    ServerLevel level = ctx.getSource().getLevel();
                                    GlobalWarmingData data = GlobalWarmingData.get(level);
                                    data.addPoints(points);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Added " + degrees + "°C worth of greenhouse points (" + points + " points)."), false);
                                    return 1;
                                })))
                .then(Commands.literal("get")
                        .executes(ctx -> {
                            ServerLevel level = ctx.getSource().getLevel();
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            GlobalWarmingData data = GlobalWarmingData.get(level);

                            float points = data.getPoints();
                            float relativeDegrees = data.getRelativeTemperature();
                            float finalTemp = data.getAbsoluteTemperature(level, player.blockPosition());

                            ctx.getSource().sendSuccess(() ->
                                    Component.literal("Volatile greenhouse gas particles per cubic meter: " + points +
                                            "\nLocal Surface Temperature: " + String.format("%.2f", finalTemp) + "°C" +
                                            "\nRelative Increase: " + String.format("%.2f", relativeDegrees) + "°C"
                                    ), false);
                            return 1;
                        }))
                .then(Commands.literal("setphase")
                        .then(Commands.argument("phase", IntegerArgumentType.integer(0, GlobalWarmingData.PHASE_THRESHOLD.length - 1))
                                .executes(ctx -> {
                                    int phaseIndex = Math.min(IntegerArgumentType.getInteger(ctx, "phase"), 6);
                                    ServerLevel level = ctx.getSource().getLevel();
                                    GlobalWarmingData data = GlobalWarmingData.get(level);
                                    int newPoints = GlobalWarmingData.PHASE_THRESHOLD[phaseIndex];
                                    data.addPoints(newPoints - data.getPoints()); // Difference to reach new phase
                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                            "Set phase to " + phaseIndex + " (" + "eff" + "). Greenhouse points: " + newPoints
                                    ), false);
                                    return 1;
                                }))
                )
        );
    }
}
