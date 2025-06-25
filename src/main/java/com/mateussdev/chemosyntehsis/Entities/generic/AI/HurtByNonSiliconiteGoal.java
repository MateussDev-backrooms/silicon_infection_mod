package com.mateussdev.chemosyntehsis.Entities.generic.AI;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import org.jetbrains.annotations.Nullable;

public class HurtByNonSiliconiteGoal extends HurtByTargetGoal {
    public HurtByNonSiliconiteGoal(PathfinderMob pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
    }

    @Override
    protected boolean canAttack(@Nullable LivingEntity pPotentialTarget, TargetingConditions pTargetPredicate) {
        return !(pPotentialTarget instanceof BaseSiliconite) && super.canAttack(pPotentialTarget, pTargetPredicate);
    }
}
