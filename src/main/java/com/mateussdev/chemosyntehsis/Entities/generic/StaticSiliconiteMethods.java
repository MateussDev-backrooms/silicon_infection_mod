package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class StaticSiliconiteMethods {
    public static Map<EntityType<?>, EntityType<? extends BaseTethered>> tetherHashMap =
            //Defines all tetherable mobs and their tether result
            //the key is the target mob and the value is the tether result
            new HashMap<>();

    static {
        //Define all tether pairs here
        tetherHashMap.put(EntityType.ZOMBIE, ModEntities.TETH_ZOMBIE.get());
        tetherHashMap.put(EntityType.HUSK, ModEntities.TETH_ZOMBIE.get());
        tetherHashMap.put(EntityType.DROWNED, ModEntities.TETH_ZOMBIE.get());
    }

    public static void tetherMob(ServerLevel serverLevel, LivingEntity tetherTarget) {
        EntityType<? extends LivingEntity> tethered_result_type = tetherHashMap.get(tetherTarget);
        if(tethered_result_type==null) {
            return;
        }
        LivingEntity tethered_result = tethered_result_type.create(serverLevel);
        //spawn the chosen entity
        if(tethered_result!=null) {
            tethered_result.moveTo(tetherTarget.getX(), tetherTarget.getY(), tetherTarget.getZ());
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    tetherTarget.getX() + 0.5,
                    tetherTarget.getY() + 0.5,
                    tetherTarget.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    0,    // X offset for randomness
                    0,    // Y offset
                    0,    // Z offset
                    0.1         // Speed of the particle
            );
            serverLevel.playSound(
                    null,
                    tetherTarget.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);
            serverLevel.addFreshEntity(tethered_result);
        }
    }

    public static boolean isTetherable(LivingEntity entity) {
        return tetherHashMap.containsKey(entity.getType());
    }
    public static boolean isTetherable(EntityType<LivingEntity> entityType) {
        return tetherHashMap.containsKey(entityType);
    }

    public static boolean isMobFromChemosynthesisMod(LivingEntity entity) {
        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }
}
