package com.mateussdev.chemosyntehsis.Items;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Gene;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.UUID;

public class MutationWand extends Item {
    private static final String TAG_SELECTED = "selected_mutation";
    public MutationWand(Properties pProperties) {
        super(pProperties);
    }

    // --- Right click in the air: cycle the selected mutation ---
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        List<RegistryObject<MutationType>> entries = ModMutations.MUTATION_TYPES.getEntries().stream().toList();
        if (entries.isEmpty()) {
            player.displayClientMessage(Component.literal("No mutations are registered"), true);
            return InteractionResultHolder.fail(stack);
        }

        CompoundTag tag = stack.getOrCreateTag();
        String current = tag.getString(TAG_SELECTED);

        int currentIndex = -1;
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId().getPath().equals(current)) {
                currentIndex = i;
                break;
            }
        }

        int nextIndex = (currentIndex + 1) % entries.size();
        String nextPath = entries.get(nextIndex).getId().getPath();
        tag.putString(TAG_SELECTED, nextPath);

        player.displayClientMessage(Component.literal("Selected mutation: " + nextPath), true);
        return InteractionResultHolder.success(stack);
    }

    // --- Right click on an entity: toggle the selected mutation on/off ---
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, net.minecraft.world.entity.LivingEntity target, InteractionHand hand) {
        if (player.level().isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(target instanceof Mob mob)) {
            player.displayClientMessage(Component.literal("Only mobs can be violated in this manner"), true);
            return InteractionResult.FAIL;
        }

        if (!(mob instanceof IGenomeModifiable genMod)) {
            player.displayClientMessage(Component.literal("You cannot edit the genes of this creature"), true);
            return InteractionResult.FAIL;
        }

        CompoundTag tag = stack.getOrCreateTag();
        String selectedPath = tag.getString(TAG_SELECTED);
        if (selectedPath.isEmpty()) {
            player.displayClientMessage(Component.literal("Select a mutation first. Right-click the air"), true);
            return InteractionResult.FAIL;
        }

        ResourceLocation mutationId = new ResourceLocation(Chemosynthesis.MODID, selectedPath);
        RegistryObject<MutationType> ro = ModMutations.findMutationType(mutationId);
        if (ro == null) {
            player.displayClientMessage(Component.literal("Unknown mutation. Something has gone horribly wrong."), true);
            return InteractionResult.FAIL;
        }
        MutationType type = ro.get();

        Gene gene = genMod.getGene();
        if (gene == null) {
            gene = new Gene();
        }



        boolean alreadyHas = gene.mutations.stream().anyMatch(m -> m.getTypeId().equals(mutationId));

        if (alreadyHas) {
            // Second right-click: remove it
            gene.mutations.removeIf(m -> m.getTypeId().equals(mutationId));
            genMod.applyGene(gene);
            player.displayClientMessage(Component.literal("Removed '" + selectedPath + "' from " + mob.getName().getString()), true);
        } else {
            // First right-click: add it, if the mob permits
            int uniqueId = new java.util.Random().nextInt(Integer.MAX_VALUE);
            Mutation newMutation = type.create(uniqueId);


            if (!newMutation.canMutateMob(mob)) {
                player.displayClientMessage(Component.literal(mob.getName().getString() + " doesn't support this mutation"), true);
                return InteractionResult.FAIL;
            }
            if(!isTierFree(newMutation, gene.mutations)) {
                player.sendSystemMessage(Component.literal("Warning: The "+ro.getId()+" mutation is of the same tier as another mutation in the mob. This combination will not be possible during normal gameplay").withStyle(ChatFormatting.YELLOW));
            }

            gene.mutations.add(newMutation);
            genMod.setGeneOrigin(UUID.randomUUID());
            genMod.applyGene(gene);
            player.displayClientMessage(Component.literal("Added '" + selectedPath + "' to " + mob.getName().getString()), true);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        CompoundTag tag = stack.getTag();
        String selected = (tag != null && tag.contains(TAG_SELECTED)) ? tag.getString(TAG_SELECTED) : "none";
        tooltip.add(Component.literal("Selected mutation: " + selected));
        tooltip.add(Component.literal("Right-click air: cycle | Right-click mob: toggle"));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    private boolean isTierFree(Mutation comparator, List<Mutation> mutations) {
        int tier = comparator.tier();
        for(Mutation mut : mutations) {
            if(mut.tier() == tier) {
                return false;
            }
        }
        return true;
    }
}
