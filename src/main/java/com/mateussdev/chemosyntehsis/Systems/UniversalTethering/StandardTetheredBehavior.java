package com.mateussdev.chemosyntehsis.Systems.UniversalTethering;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.GlobalWarming.GlobalWarmingData;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class StandardTetheredBehavior implements ITetheredHook {
    private static final int GLOBAL_WARMING_RATE = 64;
    private static final float GLOBAL_WARMING_AMOUNT = 0.01f;
    private static final int METABOLISM_GAIN_RATE = 40;
    private static final int EVOLVE_AT_METABOLISM = 100;
    private static final float BULB_BREAKOFF_CHANCE = 0.4f;
    private static final int CHUNK_COUNT = 5;


    private int tickCounter = 0;
    private int metabolism = 0;
    private boolean dead = false;
    private int deathTimer = 0;

    @Override
    public void onTick(Mob mob) {
        if (!(mob.level() instanceof ServerLevel slvl)) return;
        tickCounter++;

        //Global warming
        if (tickCounter % GLOBAL_WARMING_RATE == 0) {
            GlobalWarmingData.get(slvl).addPoints(GLOBAL_WARMING_AMOUNT);
        }

        //Metabolism TODO: Actually make this
        if (tickCounter % METABOLISM_GAIN_RATE == 0) {
            metabolism++;
        }
        if (mob.isOnFire()) metabolism += 2;

        //If target is from my mod, reset target
        if(mob.getTarget() != null && mob.getTarget() instanceof Mob mobTarget) {
            if((StaticSiliconiteMethods.isMobFromChemosynthesisMod(mob.getTarget()) || UniversalTethering.isTethered(mobTarget))) {
                mob.setTarget(null);
                //Stop all targeting
                for(WrappedGoal goal : mob.targetSelector.getRunningGoals().toList()) {
                    goal.stop();
                }
            }
        }

        //Death explosion
        if (mob.isDeadOrDying() && !dead) {
            dead = true;
        }
        if (dead) {
            deathTimer++;
            if (deathTimer == 19) {
                triggerDeathExplosion(mob, slvl);
            }
        }


    }

    @Override
    public void onHurt(Mob mob, LivingEntity attacker, float damage) {
        if (!(mob.level() instanceof ServerLevel slvl)) return;

        //Bulb breakoff
        //its infinite cuz fuck calculating and manually removing procedurally-rendering bulbs
        if (slvl.random.nextFloat() < BULB_BREAKOFF_CHANCE) {
            BulbProjectileEntity shard = new BulbProjectileEntity(slvl, mob);
            shard.shoot(
                    slvl.random.triangle(0, 1),
                    slvl.random.triangle(0.2, 1),
                    slvl.random.triangle(0, 1),
                    0.8f,
                    10.0f
            );
            slvl.addFreshEntity(shard);
        }

        //Blood hit particles
        SiliconiteParticles.spawnBloodHit(slvl, mob.position());
        slvl.playSound(null, mob.blockPosition(),
                SoundEvents.SLIME_SQUISH_SMALL, SoundSource.HOSTILE, 2f, 0.8f);

        //Alert other tethered if attacker is valid (Nasty)
        if(attacker != null && StaticSiliconiteMethods.shouldAttackMob(attacker)) {
            var nearbyMobs = mob.level().getEntitiesOfClass(
                    Mob.class,
                    mob.getBoundingBox().inflate(32),
                    nearby -> nearby != mob && nearby.getTarget() == mob
            );

            for (Mob nearby : nearbyMobs) {
                if (UniversalTethering.isTethered(nearby)) {
                    nearby.setTarget(attacker);
                }
            }
        }
    }

    @Override
    public void onTether(Mob mob) {
        if (mob.level() instanceof ServerLevel slvl) {
            SiliconiteParticles.spawnTransformationParticle(slvl, mob.blockPosition());
        }
    }



    private void triggerDeathExplosion(Mob mob, ServerLevel slvl) {
        SiliconiteParticles.spawnBloodBurst(slvl, mob.blockPosition());

        slvl.playSound(null, mob.blockPosition(),
                SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 1f, 1f);

        //Spawn chunks
        for (int i = 0; i < CHUNK_COUNT; i++) {
            if (slvl.random.nextFloat() < 0.33f) {
                var chunk = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                if (chunk != null) {
                    chunk.moveTo(mob.getX(), mob.getY(), mob.getZ());
                    chunk.addDeltaMovement(new Vec3(
                            (slvl.random.nextDouble() * 2 - 1) * 0.1,
                            slvl.random.nextDouble() * 0.8,
                            (slvl.random.nextDouble() * 2 - 1) * 0.1
                    ));
                    slvl.addFreshEntity(chunk);
                }
            } else {
                var gib = ModEntities.GIB_FLESH.get().create(slvl);
                if (gib != null) {
                    gib.moveTo(mob.getX(), mob.getY(), mob.getZ());
                    gib.addDeltaMovement(new Vec3(
                            (slvl.random.nextDouble() * 2 - 1) * 0.4,
                            slvl.random.nextDouble() * 0.5,
                            (slvl.random.nextDouble() * 2 - 1) * 0.4
                    ));
                    slvl.addFreshEntity(gib);
                }
            }
        }
    }

    protected void onEvolve(Mob mob, ServerLevel slvl) {
        //TODO: Evolve into amalgamated flesh pile
    }
}
