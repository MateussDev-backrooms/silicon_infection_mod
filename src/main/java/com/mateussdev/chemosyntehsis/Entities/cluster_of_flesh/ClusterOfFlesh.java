package com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import net.minecraft.core.particles.DustParticleOptions;
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
import org.joml.Vector3f;

public class ClusterOfFlesh extends BaseSiliconite {
    public ClusterOfFlesh(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    //##### Entity setup and stats #####//
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 22D)
                .add(Attributes.MOVEMENT_SPEED, 0.33D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 4D);
    }

    public int deathTime = 0;
    @Override
    protected void tickDeath() {
        ++deathTime;

        if (deathTime == 8 && !level().isClientSide) {
            explodeIntoBulbs();
            spawnBloodBurst();
            splitIntoChunks(5);
            level().playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
        }
    }

    public void explodeIntoBulbs() {
        for (int i = 0; i < 8; i++) {
            BulbProjectileEntity shard = new BulbProjectileEntity(level(),this);
            shard.shoot(
                    level().random.triangle(0, 1),
                    level().random.triangle(0.2, 1),
                    level().random.triangle(0, 1),
                    1.2f, // speed
                    10.0f // inaccuracy
            );
            level().addFreshEntity(shard);
        }
    }

    private void spawnBloodBurst() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        DustParticleOptions blood = new DustParticleOptions(
                new Vector3f(0.8f, 0.2f, 0.0f),
                3.0f
        );

        serverLevel.sendParticles(
                blood,
                this.getX(),
                this.getY() + 1.0,
                this.getZ(),
                30,
                0.3,
                0.5,
                0.3,
                0.1
        );
    }

    // ===== Bulb setup ===== //

    public void splitIntoChunks(int count) {
        if(level() instanceof ServerLevel slvl) {
            slvl.playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

            //Spawn chunks
            for (int i = 0; i < count; i++) {
                if(slvl.random.nextFloat() < 0.75f) {
                    ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                    chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.8f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(chunkOfFlesh);
                } else {
                    GibFlesh gib = ModEntities.GIB_FLESH.get().create(slvl);
                    gib.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    gib.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.5f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(gib);
                }
            }

        }
    }
}
