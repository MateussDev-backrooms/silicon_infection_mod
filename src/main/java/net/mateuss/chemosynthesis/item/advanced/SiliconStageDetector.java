package net.mateuss.chemosynthesis.item.advanced;

import net.minecraft.core.BlockPos;
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

public class SiliconStageDetector extends Item {

    public SiliconStageDetector(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide()) {
            BlockPos pos = pContext.getClickedPos();
            Player player = pContext.getPlayer();

            pContext.getLevel().playSound(player, pos, SoundEvents.GUARDIAN_AMBIENT, SoundSource.PLAYERS, 1f, 1f);
            player.sendSystemMessage(Component.literal("Hello silicon guy!"));
        }

        return InteractionResult.SUCCESS;
    }
}
