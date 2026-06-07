package com.mateussdev.chemosyntehsis.Particles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SiliconiteParticles {
    public static void spawnBloodBurst(ServerLevel slvl, BlockPos blockPos) {
        DustParticleOptions blood = new DustParticleOptions(new Vector3f(0.8f, 0.0f, 0.0f), 3.0f);
        DustParticleOptions smallBlood = new DustParticleOptions(new Vector3f(0.7f, 0.1f, 0.1f), 1.5f);

        slvl.sendParticles(
                blood,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                15,
                0.3,
                0.5,
                0.3,
                0.1
        );

        slvl.sendParticles(
                smallBlood,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                25,
                0.5,
                0.7,
                0.5,
                0.15
        );
    }

    public static void spawnBloodHit(ServerLevel slvl, Vec3 blockPos) {
        DustParticleOptions blood = new DustParticleOptions(new Vector3f(0.8f, 0.0f, 0.0f), 1.3f);
        DustParticleOptions darkBlood = new DustParticleOptions(new Vector3f(0.6f, 0.0f, 0.0f), 1.5f);

        slvl.sendParticles(
                blood,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                5,
                0.3,
                -0.5,
                0.3,
                0.8
        );

        slvl.sendParticles(
                darkBlood,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                3,
                0.15,
                -0.3,
                0.15,
                0.4
        );
    }

    public static void spawnTransformationParticle(ServerLevel slvl, BlockPos pos) {
        slvl.sendParticles(
                ParticleTypes.EXPLOSION,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                1,
                0,
                0,
                0,
                0.1
        );
    }
}
