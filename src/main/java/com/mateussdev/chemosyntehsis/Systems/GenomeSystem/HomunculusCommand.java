package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomunculusCommand {

    private static final int RADIUS = 50;

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("homunculus")
                        .requires(src -> src.hasPermission(2))
                        .then(Commands.literal("nearby")
                                .then(Commands.literal("getData")
                                        .executes(ctx -> showNearbyHomunculi(ctx.getSource()))
                                )
                        )
        );
    }

    private static int showNearbyHomunculi(CommandSourceStack source) {
        if (!(source.getEntity() instanceof Player player)) {
            source.sendFailure(Component.literal("Only players can use this command"));
            return 0;
        }

        AABB area = player.getBoundingBox().inflate(RADIUS);
        List<Entity> homunculi = player.level().getEntitiesOfClass(Entity.class, area,
                e -> e instanceof IHomunculus && e.isAlive());

        if (homunculi.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No homunculi found within " + RADIUS + " blocks"), false);
            return 0;
        }

        for (Entity e : homunculi) {
            IHomunculus h = (IHomunculus) e;
            HomunculusBrain brain = h.getHomunculusBrain();
            if (brain == null) continue;

            sendHomunculusData(source, e, brain);
        }
        return homunculi.size();
    }

    private static void sendHomunculusData(CommandSourceStack source, Entity entity, HomunculusBrain brain) {
        source.sendSuccess(() -> Component.literal(""), false);
        source.sendSuccess(() -> Component.literal("Homunculus: " + entity.getUUID() + ", " +
                        "[" + String.format("%.1f", entity.getX()) + ", " +
                        String.format("%.1f", entity.getY()) + ", " +
                        String.format("%.1f", entity.getZ()) + "]")
                .withStyle(ChatFormatting.GOLD), false);
        source.sendSuccess(() -> Component.literal("=======================").withStyle(ChatFormatting.DARK_GRAY), false);

        // Gene pool points
        source.sendSuccess(() -> Component.literal("Gene Pool Points: " + brain.getGenePoolBalance())
                .withStyle(ChatFormatting.AQUA), false);
        // Next generation timer
        int timer = brain.getCycleTimer();
        int duration = brain.getCycleDuration();
        source.sendSuccess(() -> Component.literal("Next Generation: " + timer + "/" + duration)
                .withStyle(ChatFormatting.AQUA), false);

        // Example list
        source.sendSuccess(() -> Component.literal("=== Example list:").withStyle(ChatFormatting.GREEN), false);
        List<Gene> examples = brain.getExampleList();
        if (examples.isEmpty()) {
            source.sendSuccess(() -> Component.literal("  (none)").withStyle(ChatFormatting.GRAY), false);
        } else {
            for (Gene gene : examples) {
                source.sendSuccess(() -> Component.literal("> Gene " + gene.id.toString().substring(0,8) + "... : F: N/A")
                        .withStyle(ChatFormatting.YELLOW), false);
                appendGeneAttributeModification(source, gene);
                appendMutations(source, gene);
            }
        }

        // Tracked genes (with fitness)
        source.sendSuccess(() -> Component.literal("=== Tracked genes:").withStyle(ChatFormatting.GREEN), false);
        Map<UUID, GeneFitnessData> tracked = brain.getTrackedGenes();
        if (tracked.isEmpty()) {
            source.sendSuccess(() -> Component.literal("  (none)").withStyle(ChatFormatting.GRAY), false);
        } else {
            for (GeneFitnessData data : tracked.values()) {
                String idShort = data.getGeneId().toString().substring(0,8);
                source.sendSuccess(() -> Component.literal("> Gene " + idShort + "... : F: " + String.format("%.2f", data.getFitness()))
                        .withStyle(ChatFormatting.YELLOW), false);
                appendMutations(source, data.getGene());
            }
        }

        source.sendSuccess(() -> Component.literal("=======================").withStyle(ChatFormatting.DARK_GRAY), false);
    }

    private static void appendMutations(CommandSourceStack source, Gene gene) {
        if (gene.mutations.isEmpty()) {
            source.sendSuccess(() -> Component.literal("  ---Mutations:---\n  > (none)").withStyle(ChatFormatting.GRAY), false);
        } else {
            source.sendSuccess(() -> Component.literal("  ---Mutations:---").withStyle(ChatFormatting.GRAY), false);
            for (Mutation m : gene.mutations) {
                String mutName = m.getClass().getSimpleName().replace("Mutation", "");
                source.sendSuccess(() -> Component.literal("  > " + mutName).withStyle(ChatFormatting.DARK_PURPLE), false);
            }
        }
    }

    private static void appendGeneAttributeModification(CommandSourceStack source, Gene gene) {
        source.sendSuccess(() -> Component.literal("---Attribute mutation ratios:---").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("   (higher number -> more biased towards attribute A)").withStyle(ChatFormatting.DARK_BLUE), false);
        source.sendSuccess(() -> Component.literal("   (smaller number -> more biased towards attribute B)").withStyle(ChatFormatting.DARK_BLUE), false);
        source.sendSuccess(() -> Component.literal("-------------------------------").withStyle(ChatFormatting.DARK_BLUE), false);
        GeneAttributeMutation genmut = gene.attributeMutation;
        source.sendSuccess(() -> Component.literal("[ health/speed = "+genmut.health_speed+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ health/armor = "+genmut.health_armor+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ armor/attack damage = "+genmut.armor_attackDamage+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ armor toughness/armor = "+genmut.armorToughness_armor+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ attack speed/attack damage = "+genmut.attackSpeed_attackDamage+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ attack speed/attack knockback = "+genmut.attackSpeed_attackKnockback+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ speed/knockbackResistance = "+genmut.speed_knockbackResistance+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ follow range/attack damage = "+genmut.followRange_attackDamage+" ]").withStyle(ChatFormatting.BLUE), false);
        source.sendSuccess(() -> Component.literal("[ follow range/speed = "+genmut.followRange_speed+" ]").withStyle(ChatFormatting.BLUE), false);
    }
}