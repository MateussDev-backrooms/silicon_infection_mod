package com.mateussdev.chemosyntehsis.Entities.Amalgamations.amal_zombie;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie.TethZombie;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.Interfaces.IBiomassGenerator;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Util.GlobalMobCap;
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
    public AmalZombie(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
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

        if(level() instanceof ServerLevel slvl) {
            if(tickCount % 240 == 0) {
                if(random.nextFloat() < 0.4f && GlobalMobCap.canSpawnUnique(slvl, ModEntities.TETH_ZOMBIE.get(), blockPosition(), 600, 128)) {
                    //Zombie mitosis

                    slvl.playSound(
                            null,
                            blockPosition(),
                            SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                            SoundSource.HOSTILE,
                            1f,
                            1f);

                    //Particles
                    StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

                    TethZombie tethZombie = ModEntities.TETH_ZOMBIE.get().create(slvl);
                    tethZombie.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    tethZombie.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.8f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(tethZombie);
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
