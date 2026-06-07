package com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CustomAI;

import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class TetheredNearestAttackableTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {

    public TetheredNearestAttackableTargetGoal(Mob mob) {
        super(mob, LivingEntity.class, 10, true, false, TetheredNearestAttackableTargetGoal::isValidTetheredTarget);
    }

    private static boolean isValidTetheredTarget(LivingEntity target) {
        if (StaticSiliconiteMethods.isMobFromChemosynthesisMod(target)) return false;
        if (target instanceof Mob targetMob && UniversalTethering.isTethered(targetMob)) return false;
        return true;
    }

    @Override
    public boolean canUse() {
        return super.canUse();
    }
}