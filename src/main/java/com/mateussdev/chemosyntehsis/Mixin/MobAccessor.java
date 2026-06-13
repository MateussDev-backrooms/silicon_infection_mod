package com.mateussdev.chemosyntehsis.Mixin;

import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.gen.Accessor;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.Mob.class)
public interface MobAccessor {
    @Accessor
    MoveControl getMoveControl();

    @Accessor
    void setMoveControl(MoveControl moveControl);

    @Accessor
    PathNavigation getNavigation();

    @Accessor
    void setNavigation(PathNavigation navigation);

    @Accessor
    LookControl getLookControl();

    @Accessor
    void setLookControl(LookControl lookControl);
}
