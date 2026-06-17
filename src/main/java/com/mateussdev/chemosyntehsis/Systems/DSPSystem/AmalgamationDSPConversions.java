package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_turret.AmalTurret;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;

import java.util.List;

public class AmalgamationDSPConversions {

    public static final List<EntityType<? extends BaseAmalgamation>> PROTECTIVE_AMALGAMATIONS = List.of(
            ModEntities.AMAL_TURRET.get()
    );

    public static final List<EntityType<? extends BaseAmalgamation>> MOBGEN_AMALGAMATIONS = List.of(
            ModEntities.AMAL_SPAWNER.get()
    );

    public static final List<EntityType<? extends BaseAmalgamation>> BIOGEN_AMALGAMATIONS = List.of(
            ModEntities.AMAL_ZOMBIE.get()
    );

    public static void convertToProtective(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            slvl.playSound(null, mob.blockPosition(), SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE);
            SiliconiteParticles.spawnTransformationParticle(slvl, mob.blockPosition());

            BaseAmalgamation turret = PROTECTIVE_AMALGAMATIONS.get(mob.getRandom().nextInt(PROTECTIVE_AMALGAMATIONS.size())).create(slvl);
            turret.moveTo(mob.getPosition(0));
            turret.setHealth(mob.getHealth());

            slvl.addFreshEntity(turret);
            mob.discard();
        }
    }

    public static void convertToMobgen(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            slvl.playSound(null, mob.blockPosition(), SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE);
            SiliconiteParticles.spawnTransformationParticle(slvl, mob.blockPosition());

            BaseAmalgamation amalgamation = MOBGEN_AMALGAMATIONS.get(mob.getRandom().nextInt(MOBGEN_AMALGAMATIONS.size())).create(slvl);
            amalgamation.moveTo(mob.getPosition(0));
            amalgamation.setHealth(mob.getHealth());

            slvl.addFreshEntity(amalgamation);
            mob.discard();
        }
    }
}
