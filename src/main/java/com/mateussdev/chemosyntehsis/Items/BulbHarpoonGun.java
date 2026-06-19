package com.mateussdev.chemosyntehsis.Items;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.mutated_harpoon.MutatedHarpoonEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BulbHarpoonGun extends Item {
    public BulbHarpoonGun(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        if(pLevel instanceof ServerLevel slvl) {
            MutatedHarpoonEntity harpoon = new MutatedHarpoonEntity(pLevel, pPlayer);
            harpoon.moveTo(pPlayer.getEyePosition());
            harpoon.shoot(pPlayer.getLookAngle().x, pPlayer.getLookAngle().y, pPlayer.getLookAngle().z, 2.0f, 0f);
            slvl.addFreshEntity(harpoon);
        }

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
