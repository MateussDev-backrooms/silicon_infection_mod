package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_zombie;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Interfaces.IBiomassGenerator;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.AmalgamationDSPConversions;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPThreshold;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPType;
import com.mateussdev.chemosyntehsis.Systems.MobCapSystem.GlobalMobCap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AmalZombie extends BaseAmalgamation implements IBiomassGenerator {
    private static final float GIB_THROW_DISTANCE = 1.2f;

    public AmalZombie(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        thresholds.add(new DSPThreshold(DSPType.D_D_DAMAGEDIRECTIVE, 400, () -> {
            AmalgamationDSPConversions.convertToProtective(this);
        }));
        thresholds.add(new DSPThreshold(DSPType.D_MM_MOBDEFICIT, 400, () -> {
            AmalgamationDSPConversions.convertToMobgen(this);
        }));
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 32D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public void tick() {
        super.tick();

        if(this.level() instanceof ServerLevel slvl) {
            if(tickCount % 240 == 0) {
                if(GlobalMobCap.canSpawnUnique(slvl, ModEntities.CHUNK_OF_FLESH.get(), blockPosition(), 300, 128)) {
                    slvl.playSound(
                            null,
                            blockPosition(),
                            SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                            SoundSource.HOSTILE,
                            1f,
                            1f);

                    //Particles
                    SiliconiteParticles.spawnBloodBurst(slvl, blockPosition());

                    burstGibsAndChunks(12);
                } else {
                    slvl.playSound(
                            null,
                            blockPosition(),
                            SoundEvents.WARDEN_HEARTBEAT,
                            SoundSource.HOSTILE,
                            1f,
                            1f);

                    //Particles
                    SiliconiteParticles.spawnBloodBurst(slvl, blockPosition());
                }
            }
        }
    }

    public void burstGibsAndChunks(int count) {
        if (this.level() instanceof ServerLevel slvl) {
            slvl.playSound(null, blockPosition(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 1f, 1f);

            //Particles
            SiliconiteParticles.spawnBloodBurst(slvl, blockPosition());

            //Spawn chunks
            for (int i = 0; i < count; i++) {
                if (slvl.random.nextFloat() < 0.05f) {
                    ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                    chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble() * 2f - 1f) * GIB_THROW_DISTANCE, (slvl.random.nextDouble()) * 0.8f, (slvl.random.nextDouble() * 2f - 1f) * GIB_THROW_DISTANCE));
                    slvl.addFreshEntity(chunkOfFlesh);
                } else {
                    GibFlesh gib = ModEntities.GIB_FLESH.get().create(slvl);
                    gib.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    gib.addDeltaMovement(new Vec3((slvl.random.nextDouble() * 2f - 1f) * GIB_THROW_DISTANCE, (slvl.random.nextDouble()) * 0.5f, (slvl.random.nextDouble() * 2f - 1f) * GIB_THROW_DISTANCE));
                    slvl.addFreshEntity(gib);
                }
            }

        }
    }
}
