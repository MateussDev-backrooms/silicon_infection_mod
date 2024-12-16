package net.Mateuss.Chemosynthesis.core;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.Mateuss.Chemosynthesis.stage_system.InfectionProgressManager;
import net.Mateuss.Chemosynthesis.stage_system.InfectionProgress;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                net.minecraft.commands.Commands.literal("chemosynthesis")
                        .then(net.minecraft.commands.Commands.literal("add_points")
                                .then(net.minecraft.commands.Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(ModCommands::addPoints)))
                        .then(net.minecraft.commands.Commands.literal("display_points")
                                .executes(ModCommands::displayPoints))
        );
    }

    private static int addPoints(CommandContext<CommandSourceStack> context) {
        int amount = IntegerArgumentType.getInteger(context, "amount");
        ServerLevel level = context.getSource().getLevel();
        InfectionProgress points = InfectionProgressManager.get(level);

        points.addPoints(amount);
        context.getSource().sendSuccess(
                () -> Component.literal("Added " + amount + " points. Total points: " + points.getPoints()), true
        );
        return amount;
    }
    private static int displayPoints(CommandContext<CommandSourceStack> context) {
        ServerLevel level = context.getSource().getLevel();
        InfectionProgress points = InfectionProgressManager.get(level);

        context.getSource().sendSuccess(
                () -> Component.literal("Current infection phase: " + points.getStageString() + " ( " + points.getPoints() +" points )"), true
        );
        return points.getPoints();
    }
}
