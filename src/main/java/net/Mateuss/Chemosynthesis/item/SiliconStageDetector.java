package net.Mateuss.Chemosynthesis.item;

import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.projectile.ProjectileBrachaticHarpoon;
import net.Mateuss.Chemosynthesis.entity.projectile.ProjectileBulb;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SiliconStageDetector extends Item {

    public SiliconStageDetector(Properties pProperties) {
        super(pProperties);
    }

//    @Override
//    public InteractionResult useOn(UseOnContext pContext) {
//        if (!pContext.getLevel().isClientSide()) {
//            BlockPos pos = pContext.getClickedPos();
//            Player player = pContext.getPlayer();
//
//            pContext.getLevel().playSound(player, pos, SoundEvents.GUARDIAN_AMBIENT, SoundSource.PLAYERS, 1f, 1f);
//            player.sendSystemMessage(Component.literal("Hello silicon guy!"));
//        }
//
//        return InteractionResult.SUCCESS;
//    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(!pLevel.isClientSide) {
            Vec3 lookDir = pPlayer.getLookAngle();

            ProjectileBulb harpoon = ModEntities.BULB_PROJECTILE.get().create(pLevel);
            harpoon.setPos(pPlayer.getX(), pPlayer.getEyeY() - 0.1, pPlayer.getZ());
            harpoon.shoot(lookDir.x, lookDir.y, lookDir.z, 2, 0);
            harpoon.setOwner(pPlayer);
            pLevel.addFreshEntity(harpoon);

            pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        pPlayer.swing(pUsedHand, true);
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
