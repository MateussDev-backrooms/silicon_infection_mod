package com.mateussdev.chemosyntehsis.Items;

import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class TetheringWand extends Item {
    public TetheringWand(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(!(pInteractionTarget instanceof Mob mob)) return InteractionResult.FAIL;
        if(!(pPlayer.level() instanceof ServerLevel slvl)) return InteractionResult.FAIL;
        UniversalTethering.tryTetherMob(mob, slvl);
        return InteractionResult.SUCCESS;
    }
}
