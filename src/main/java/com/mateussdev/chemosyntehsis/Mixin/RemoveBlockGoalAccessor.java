package com.mateussdev.chemosyntehsis.Mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.gen.Invoker;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.ai.goal.RemoveBlockGoal.class)
public interface RemoveBlockGoalAccessor {
    @Invoker
    BlockPos callGetPosWithBlock(BlockPos pPos, BlockGetter pLevel);
}
