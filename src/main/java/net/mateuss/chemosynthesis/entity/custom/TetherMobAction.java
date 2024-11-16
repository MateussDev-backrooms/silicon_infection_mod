package net.mateuss.chemosynthesis.entity.custom;

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

        //spawn the chosen entity
        if(tethered_result!=null) {
            tethered_result.moveTo(pEntity.getX(), pEntity.getY(), pEntity.getZ());
            pLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    pSelf.getX() + 0.5,
                    pSelf.getY() + 0.5,
                    pSelf.getZ() + 0.5,
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


}
