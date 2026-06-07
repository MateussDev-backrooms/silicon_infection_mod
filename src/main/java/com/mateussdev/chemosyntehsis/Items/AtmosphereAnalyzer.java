package com.mateussdev.chemosyntehsis.Items;

import com.mateussdev.chemosyntehsis.Systems.GlobalWarming.GlobalWarmingData;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class AtmosphereAnalyzer extends Item {
    public AtmosphereAnalyzer(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        displayDiagnostics(pContext.getLevel(), pContext.getPlayer());

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        displayDiagnostics(pLevel, pPlayer);

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    private void displayDiagnostics(Level pLevel, Player pPlayer) {
        if(!pLevel.isClientSide()) {
            GlobalWarmingData globalWarmingData = GlobalWarmingData.get(pLevel);

            float points = globalWarmingData.getPoints();
            float relativeDegrees = globalWarmingData.getRelativeTemperature();
            float absoluteDegrees = globalWarmingData.getAbsoluteTemperature(pLevel, pPlayer.blockPosition());
            pPlayer.sendSystemMessage(Component.literal(
                    "[============ [ATMOSPHERE ANALYZER] ============]"+
                            "\n| Volatile greenhouse gas particles per cubic meter: " + points +
                            "\n| Local Surface Temperature: " + String.format("%.2f", absoluteDegrees) + "°C" +
                            "\n| Relative Increase: " + String.format("%.2f", relativeDegrees) + "°C"+
                            "\n[===============================================]"
            ));
            pLevel.playSound(null, pPlayer.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS);
        }
    }
}
