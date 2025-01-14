package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mateussdev.chemosyntehsis.Core.ModBlockEntities;
import mod.azure.azurelib.animatable.GeoBlockEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BEVeinBlock extends BlockEntity implements GeoBlockEntity {
    public BEVeinBlock(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.VEIN_BLOCK.get(), pPos, pBlockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //no need yet due to static nature
        //TODO: Add pulsating animation to blood and block itself
    }

    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
