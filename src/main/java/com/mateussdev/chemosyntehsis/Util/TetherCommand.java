package com.mateussdev.chemosyntehsis.Util;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;
import java.util.Iterator;

public class TetherCommand {

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tether").requires((p_137812_) -> {
            return p_137812_.hasPermission(2);
        })).executes((p_137817_) -> {
            return kill((CommandSourceStack)p_137817_.getSource(), ImmutableList.of(((CommandSourceStack)p_137817_.getSource()).getEntityOrException()));
        })).then(Commands.argument("targets", EntityArgument.entities()).executes((p_137810_) -> {
            return kill((CommandSourceStack)p_137810_.getSource(), EntityArgument.getEntities(p_137810_, "targets"));
        })));
    }

    private static int kill(CommandSourceStack pSource, Collection<? extends Entity> pTargets) {
        Iterator var2 = pTargets.iterator();

        while(var2.hasNext()) {
            Entity e = (Entity)var2.next();
            if(e instanceof LivingEntity le)
            StaticSiliconiteMethods.tetherMob(pSource.getLevel(), le);
        }

        if (pTargets.size() == 1) {
            pSource.sendSuccess(() -> {
                return Component.literal("Tethered: "+new Object[]{((Entity)pTargets.iterator().next()).getDisplayName()});
            }, true);
        } else {
            pSource.sendSuccess(() -> {
                return Component.literal("Tethered multiple entities: "+new Object[]{pTargets.size()});
            }, true);
        }

        return pTargets.size();
    }
}
