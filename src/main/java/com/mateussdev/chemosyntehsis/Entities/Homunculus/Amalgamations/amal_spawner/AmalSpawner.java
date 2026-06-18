package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_spawner;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_turret.AmalTurret;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.AmalgamationDSPConversions;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPThreshold;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPType;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Util.GlobalMobCap;
import net.minecraft.core.particles.ParticleTypes;
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

import java.util.List;

public class AmalSpawner extends BaseAmalgamation {
    public AmalSpawner(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        thresholds.add(new DSPThreshold(DSPType.D_D_DAMAGEDIRECTIVE, 400, () -> {
            AmalgamationDSPConversions.convertToProtective(this);
        }));
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    private final List<EntityType<? extends BaseTethered>> spawnables = List.of(
            ModEntities.TETH_ZOMBIE.get(),
            ModEntities.TETH_COW.get(),
            ModEntities.TETH_SHEEP.get(),
            ModEntities.TETH_PIG.get(),
            ModEntities.TETH_SKELETON.get(),
            ModEntities.TETH_ENDERMAN.get()
    );

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            if(tickCount % 240 == 0) {
                if(random.nextFloat() < 0.4f && GlobalMobCap.canSpawnGeneral(slvl, blockPosition(), 600, 128)) {
                    slvl.playSound(
                            null,
                            blockPosition(),
                            SoundEvents.FIRE_EXTINGUISH,
                            SoundSource.HOSTILE,
                            1f,
                            1f);

                    //Particles
                    StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());
                    slvl.sendParticles(ParticleTypes.FLAME, position().x, position().y, position().z, 5, 0.0f, 0.2f, 0.0f, 0.2f);

                    //Spawn random mob

                    BaseTethered spawn_mob = spawnables.get(random.nextInt(spawnables.size())).create(slvl);
                    spawn_mob.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    spawn_mob.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.8f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(spawn_mob);
                } else {
                    slvl.playSound(
                            null,
                            blockPosition(),
                            SoundEvents.WARDEN_HEARTBEAT,
                            SoundSource.HOSTILE,
                            1f,
                            1f);

                    //Particles
                    StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());
                }
            }
        }
    }
}
