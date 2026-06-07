package com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CustomAI;
// SpreadInfectionGoal.java — a passive goal that just marks the mob as "spreading"
// The actual spread logic lives in the event handler below

import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SpreadInfectionGoal extends Goal {
    // This goal exists primarily as a marker and tick hook
    // Heavy lifting is done in LivingHurtEvent to stay compatible
    // with any melee implementation from any mod
    private final Mob mob;

    public SpreadInfectionGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.noneOf(Goal.Flag.class));
    }

    @Override
    public boolean canUse() { return UniversalTethering.isTethered(mob); }

    @Override
    public boolean canContinueToUse() { return canUse(); }
}