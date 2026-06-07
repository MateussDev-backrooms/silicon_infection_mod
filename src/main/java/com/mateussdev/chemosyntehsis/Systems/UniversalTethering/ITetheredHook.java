package com.mateussdev.chemosyntehsis.Systems.UniversalTethering;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface ITetheredHook {
    default void onTick(Mob mob) {}

    default void onSpreadInfection(Mob spreader, Mob victim) {}

    default void onHurt(Mob mob, LivingEntity attacker, float damage) {}

    default void onTether(Mob mob) {}
}
