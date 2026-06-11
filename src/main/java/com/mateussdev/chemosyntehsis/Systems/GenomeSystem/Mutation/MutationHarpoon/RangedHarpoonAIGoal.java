package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationHarpoon;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.phys.Vec3;

public class RangedHarpoonAIGoal extends Goal {
    private final Mob mob;
    private final Mob rangedAttackMob;
    @Nullable
    private LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;
    private final MutationHarpoon mutationHarpoon;

    public RangedHarpoonAIGoal(Mob pRangedAttackMob, double pSpeedModifier, int pAttackInterval, float pAttackRadius, MutationHarpoon mutation) {
        this(pRangedAttackMob, pSpeedModifier, pAttackInterval, pAttackInterval, pAttackRadius, mutation);
    }

    public RangedHarpoonAIGoal(Mob pRangedAttackMob, double pSpeedModifier, int pAttackIntervalMin, int pAttackIntervalMax, float pAttackRadius, MutationHarpoon mutation) {
        if (!(pRangedAttackMob instanceof LivingEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackMob = pRangedAttackMob;
            this.mob = (Mob)pRangedAttackMob;
            this.speedModifier = pSpeedModifier;
            this.attackIntervalMin = pAttackIntervalMin;
            this.attackIntervalMax = pAttackIntervalMax;
            this.attackRadius = pAttackRadius;
            this.attackRadiusSqr = pAttackRadius * pAttackRadius;
            this.mutationHarpoon = mutation;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }

            float f = (float)Math.sqrt(d0) / this.attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            this.performRangedAttack(this.target, f1);
            this.attackTime = Mth.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double)this.attackRadius, (double)this.attackIntervalMin, (double)this.attackIntervalMax));
        }

    }

    public void performRangedAttack(LivingEntity target, float v) {
        boolean b = mutationHarpoon.harpoonEntity == null;
        // && !b && mutationHarpoon.harpoonEntity.getCurrentAttachType() == 0
        if (attackTime <= 0) {

            BulbHarpoonEntity harpoon = new BulbHarpoonEntity(mob.level(), mob);
            harpoon.setPos(mob.getX(), mob.getY(), mob.getZ());

            Vec3 shootDir = target.getEyePosition().subtract(mob.position());
            mob.level().playSound(null, mob.blockPosition(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 1f, 1f);
            harpoon.shoot(shootDir, 1.2f, 0f, mob, 0.08f, 500);

            mob.level().addFreshEntity(harpoon);
            mutationHarpoon.harpoonEntity = harpoon;
        }
    }
}