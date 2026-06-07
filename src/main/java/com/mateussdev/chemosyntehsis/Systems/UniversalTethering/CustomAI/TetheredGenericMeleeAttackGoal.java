package com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CustomAI;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class TetheredGenericMeleeAttackGoal extends MeleeAttackGoal {

    private static final double MOVEMENT_SPEED = 1.2D;
    private static final float FALLBACK_DAMAGE = 3.0f;
    private static final float KNOCKBACK_STRENGTH = 0.4f;

    private final Mob mob;

    public TetheredGenericMeleeAttackGoal(PathfinderMob mob) {
        super(mob, MOVEMENT_SPEED, true);
        this.mob = mob;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distSq) {
        double reachSq = this.getAttackReachSqr(target);

        if (distSq <= reachSq && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();

            // Use the mob's own attack damage attribute if it has one,
            // otherwise fall back to our hardcoded value
            float damage = FALLBACK_DAMAGE;
            if (mob.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)) {
                damage = (float) mob.getAttributeValue(Attributes.ATTACK_DAMAGE);
            }

            target.hurt(
                    mob.damageSources().mobAttack(mob),
                    damage
            );

            // Manual knockback since passive mobs have no knockback attribute
            double dx = mob.getX() - target.getX();
            double dz = mob.getZ() - target.getZ();
            double len = Math.sqrt(dx * dx + dz * dz);
            if (len > 0) {
                target.setDeltaMovement(
                        target.getDeltaMovement().add(
                                (-dx / len) * KNOCKBACK_STRENGTH,
                                0.15,
                                (-dz / len) * KNOCKBACK_STRENGTH
                        )
                );
            }
        }
    }
}
