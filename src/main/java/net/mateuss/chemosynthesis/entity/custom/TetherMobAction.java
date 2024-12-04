package net.mateuss.chemosynthesis.entity.custom;

import net.mateuss.chemosynthesis.block.ModBlocks;
import net.mateuss.chemosynthesis.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class TetherMobAction {
    public void TetherMob(ServerLevel pLevel, LivingEntity pEntity, LivingEntity pSelf, boolean discardSelf) {
        LivingEntity tethered_result = null;

        if(pEntity.getType() == EntityType.ZOMBIE || pEntity.getType() == EntityType.HUSK || pEntity.getType() == EntityType.DROWNED) {
            tethered_result = ModEntities.TETH_ZOMBIE.get().create(pLevel);
        }
        if(pEntity.getType() == EntityType.COW) {
            tethered_result = ModEntities.TETH_COW.get().create(pLevel);
        }
        if(pEntity.getType() == EntityType.PIG) {
            tethered_result = ModEntities.TETH_PIG.get().create(pLevel);
        }
        if(pEntity.getType() == EntityType.SHEEP) {
            tethered_result = ModEntities.TETH_SHEEP.get().create(pLevel);
        }

        //spawn the chosen entity
        if(tethered_result!=null) {
            tethered_result.moveTo(pEntity.getX(), pEntity.getY(), pEntity.getZ());
            pLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    pEntity.getX() + 0.5,
                    pEntity.getY() + 0.5,
                    pEntity.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    0,    // X offset for randomness
                    0,    // Y offset
                    0,    // Z offset
                    0.1         // Speed of the particle
            );
            pLevel.playSound(
                    null,
                    pEntity.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);
            pLevel.addFreshEntity(tethered_result);

            if(discardSelf) {
                pSelf.discard();
            }
        }
    }

    public void turnIntoHomunculus(ServerLevel pLevel, LivingEntity pSelf) {
        pSelf.discard();
    }

    public void spawnTripods(ServerLevel level, LivingEntity entity, int amountMin, int amountMax) {
        if(!level.isClientSide() && entity.isDeadOrDying()) {
            //play effects
            for (int i = 0; i < 80; i++) {  // Spawn multiple particles for effect
                double offsetX = (level.random.nextDouble() - 0.5) * 0.5;
                double offsetY = level.random.nextDouble() * 0.5;
                double offsetZ = (level.random.nextDouble() - 0.5) * 0.5;

                level.sendParticles(
                        ParticleTypes.SMOKE,   // Example green particle
                        entity.getX() + 0.5,
                        entity.getY() + 0.5,
                        entity.getZ() + 0.5,
                        1,          // Number of particles per spawn call
                        offsetX,    // X offset for randomness
                        offsetY,    // Y offset
                        offsetZ,    // Z offset
                        0.1         // Speed of the particle
                );
            }

            //sound
            level.playSound(null,
                    entity.blockPosition(),
                    SoundEvents.SQUID_DEATH,
                    SoundSource.HOSTILE
            );

            //spawn the tripods
            int k = level.random.nextIntBetweenInclusive(amountMin, amountMax);

            for(int i=0; i<k; i++) {
                EntitySiliconTripod tripod = ModEntities.SILICON_TRIPOD.get().create(level);
                if(tripod != null) {
                    tripod.moveTo(entity.getX()+level.random.nextDouble(), entity.getY(), entity.getZ()+level.random.nextDouble());
                    tripod.setDeltaMovement((level.random.nextDouble()*2.0d-1.0d)*0.33d, 0.5d, (level.random.nextDouble()*2.0d-1.0d)*0.33d);
                    level.addFreshEntity(tripod);
                }
            }
        }
    }


}
