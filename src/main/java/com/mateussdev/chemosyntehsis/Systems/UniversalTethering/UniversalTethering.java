package com.mateussdev.chemosyntehsis.Systems.UniversalTethering;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModNetworking;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.HurtByNonSiliconiteGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredCapability;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredCapabilityProvider;
import com.mateussdev.chemosyntehsis.Util.Packets.TetheredSyncPacket;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CustomAI.SpreadInfectionGoal;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CustomAI.TetheredGenericMeleeAttackGoal;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CustomAI.TetheredNearestAttackableTargetGoal;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import java.util.Map;

public class UniversalTethering {

    public static Map<EntityType<?>, EntityType<? extends BaseTethered>> handmadeTetheredMobs =
            //Defines all tetherable mobs and their tether result
            //the key is the target mob and the value is the tether result
            Map.of(
                    //Define all tether pairs here
                    EntityType.ZOMBIE, ModEntities.TETH_ZOMBIE.get(),
                    EntityType.HUSK, ModEntities.TETH_ZOMBIE.get(),
                    EntityType.DROWNED, ModEntities.TETH_ZOMBIE.get(),
                    EntityType.COW, ModEntities.TETH_COW.get(),
                    EntityType.SKELETON, ModEntities.TETH_SKELETON.get(),
                    EntityType.STRAY, ModEntities.TETH_SKELETON.get(),
                    EntityType.ENDERMAN, ModEntities.TETH_ENDERMAN.get()
            );

    public static void tryTetherMob(Mob tetherTarget, ServerLevel serverLevel) {
        EntityType<? extends LivingEntity> handmadeTetheredVariant = handmadeTetheredMobs.get(tetherTarget.getType());
        if(handmadeTetheredVariant==null) {
            //Split into chunks depending on the bounding box size
            if(StaticSiliconiteMethods.boundingBoxVolume(tetherTarget.getBoundingBox()) < 0f) {
                StaticSiliconiteMethods.splitIntoChunks(serverLevel, tetherTarget.blockPosition(), Mth.clamp(Mth.ceil(StaticSiliconiteMethods.boundingBoxVolume(tetherTarget.getBoundingBox())), 2, 64));
                tetherTarget.discard();
            } else {
                SiliconiteParticles.spawnTransformationParticle(serverLevel, tetherTarget.blockPosition());
                serverLevel.playSound(
                        null,
                        tetherTarget.blockPosition(),
                        SoundEvents.ZOMBIE_INFECT,
                        SoundSource.HOSTILE,
                        1f,
                        1f);
                universalTetherMob(tetherTarget);
            }
            return;
        }

        //spawn the handmade entity instead of the universal one
        LivingEntity tethered_result = handmadeTetheredVariant.create(serverLevel);
        if(tethered_result!=null) {
            tethered_result.moveTo(tetherTarget.getX(), tetherTarget.getY(), tetherTarget.getZ());
            SiliconiteParticles.spawnTransformationParticle(serverLevel, tetherTarget.blockPosition());
            serverLevel.playSound(
                    null,
                    tetherTarget.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            serverLevel.addFreshEntity(tethered_result);
            tetherTarget.discard();
        }
    }

    public static void universalTetherMob(Mob mob) {
        mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP).ifPresent(cap -> {
            if (!cap.isTethered()) {
                cap.setTethered(true);

                //Register the standard tethered behavior
                StandardTetheredBehavior behavior = new StandardTetheredBehavior();
                cap.addHook(behavior);
                behavior.onTether(mob);

                injectTetheredGoals(mob);

                //Stop from targeting anything
                mob.setTarget(null);
                mob.setLastHurtByMob(null);

                //Heal mob
                mob.setHealth(mob.getMaxHealth());

                //Stawp this mob from being targeted by anything nearby
                clearAsTargetFromNearby(mob);

                // Sync to all clients that can see this entity
                if (mob.level() instanceof ServerLevel serverLevel) {
                    ModNetworking.CHANNEL.send(
                            net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY.with(() -> mob),
                            new TetheredSyncPacket(mob.getId(), true)
                    );
                }
            }
        });
    }

    private static void clearAsTargetFromNearby(Mob newlyTethered) {
        // Search in a reasonable radius — 32 blocks covers any realistic combat scenario
        var nearbyMobs = newlyTethered.level().getEntitiesOfClass(
                Mob.class,
                newlyTethered.getBoundingBox().inflate(32),
                nearby -> nearby != newlyTethered && nearby.getTarget() == newlyTethered
        );

        for (Mob nearby : nearbyMobs) {
            // Only clear if it's a tethered mob — untethered enemies can keep fighting each other
            if (isTethered(nearby)) {
                nearby.setTarget(null);
            }
        }
    }

    public static boolean isTethered(Mob mob) {
        return mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP)
                .map(TetheredCapability::isTethered)
                .orElse(false);
    }

    // Called both on tethering AND on mob load from NBT
    public static void injectTetheredGoals(Mob mob) {
        // Strip vanilla target goals
        mob.targetSelector.getAvailableGoals().removeIf(wrapped ->
                wrapped.getGoal() instanceof NearestAttackableTargetGoal ||
                        wrapped.getGoal() instanceof HurtByTargetGoal
        );

        // Check if the mob has any melee attack goal already
        boolean hasMeleeGoal = mob.goalSelector.getAvailableGoals().stream()
                .anyMatch(wrapped ->
                        wrapped.getGoal() instanceof net.minecraft.world.entity.ai.goal.MeleeAttackGoal
                );

        // Passive mobs have no melee goal — give them one
        if (!hasMeleeGoal && mob instanceof PathfinderMob pm) {
            mob.goalSelector.addGoal(1, new TetheredGenericMeleeAttackGoal(pm));
        }

        if(mob instanceof PathfinderMob pm) {
           mob.targetSelector.addGoal(1, new HurtByNonSiliconiteGoal(pm).setAlertOthers(new Class[]{BaseSiliconite.class}));
        }
        // Inject tethered targeting
        mob.targetSelector.addGoal(2, new TetheredNearestAttackableTargetGoal(mob));
        mob.goalSelector.addGoal(2, new SpreadInfectionGoal(mob));
    }
}
