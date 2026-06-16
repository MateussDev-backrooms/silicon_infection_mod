package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.*;

public class DirectiveSignalingProteinData extends SavedData {
    private static final String DATA_SAVE_NAME = "silicon_directiveSignalingProteinData";

    private static final float DECAY_RATE = 0.1f;

    private static final float DIFFUSION_THRESHOLD = 1000f;
    private static final float DIFFUSION_RATE = 0.25f;

    public static final Map<ChunkPos, EnumMap<DSPType, Float>> dspMap = new HashMap<>();
    private static final float PARTICLE_VISIBILITY_THRESHOLD = 10f;

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        return pCompoundTag;
    }

    public static DirectiveSignalingProteinData load(CompoundTag tag) {
        DirectiveSignalingProteinData data = new DirectiveSignalingProteinData();
        //TODO: Serialization
        return data;
    }

    public static DirectiveSignalingProteinData get(ServerLevel slvl) {
        return slvl.getDataStorage().computeIfAbsent(
                DirectiveSignalingProteinData::load,
                DirectiveSignalingProteinData::new,
                DATA_SAVE_NAME
        );
    }

    public void tick(ServerLevel slvl) {
        Map<ChunkPos, EnumMap<DSPType, Float>> pendingDiffusion = new HashMap<>();

        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> entry : dspMap.entrySet()) {
            EnumMap<DSPType, Float> field = entry.getValue();

            for (DSPType type : DSPType.values()) {
                float value = field.getOrDefault(type, 0f);
                if (value <= 0f) continue;

                //Decay
                field.put(type, Math.max(0f, value - DECAY_RATE));

                // ollect diffusion into staging map, do not touch dspMap here
                if (value > DIFFUSION_THRESHOLD) {
                    diffuseChunk(entry.getKey(), slvl, field, type, value, pendingDiffusion);
                }
            }
        }

        // Now safe to modify dspMap — iteration is finished
        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> pending : pendingDiffusion.entrySet()) {
            dspMap.computeIfAbsent(pending.getKey(), k -> new EnumMap<>(DSPType.class))
                    .forEach((type, amount) ->
                            dspMap.get(pending.getKey()).merge(type, amount, Float::sum));
        }
    }

    private void diffuseChunk(
            ChunkPos pos,
            ServerLevel level,
            EnumMap<DSPType, Float> field,
            DSPType type,
            float value,
            Map<ChunkPos, EnumMap<DSPType, Float>> pending) {

        ChunkPos[] neighbours = {
                new ChunkPos(pos.x + 1, pos.z),
                new ChunkPos(pos.x - 1, pos.z),
                new ChunkPos(pos.x, pos.z + 1),
                new ChunkPos(pos.x, pos.z - 1)
        };

        float overflow = value - DIFFUSION_THRESHOLD;
        float share = overflow * DIFFUSION_RATE / 4f;
        float totalPushed = 0f;

        for (ChunkPos neighbour : neighbours) {
            if (!level.hasChunk(neighbour.x, neighbour.z)) continue;

            float currentValue = dspMap
                    .getOrDefault(neighbour, new EnumMap<>(DSPType.class))
                    .getOrDefault(type, 0f);
            float stagedValue = pending
                    .getOrDefault(neighbour, new EnumMap<>(DSPType.class))
                    .getOrDefault(type, 0f);

            if (currentValue + stagedValue >= DIFFUSION_THRESHOLD) continue;

            float capacity = DIFFUSION_THRESHOLD - (currentValue + stagedValue);
            float actualShare = Math.min(share, capacity);

            pending.computeIfAbsent(neighbour, k -> new EnumMap<>(DSPType.class))
                    .merge(type, actualShare, Float::sum);

            totalPushed += actualShare;
        }

        field.put(type, value - totalPushed);
    }

    public void spawnDSPParticles(ServerLevel slvl) {
        RandomSource random = slvl.random;

        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> entry : dspMap.entrySet()) {
            ChunkPos pos = entry.getKey();
            if (!slvl.hasChunk(pos.x, pos.z)) continue;

            for (Map.Entry<DSPType, Float> typeEntry : entry.getValue().entrySet()) {
                float density = typeEntry.getValue();
                if (density < PARTICLE_VISIBILITY_THRESHOLD) continue;

                int count = (int) Math.min(density / 100f, 1000f); //cap at 20 per chunk per type
                SimpleParticleType particle = particleForType(typeEntry.getKey());

                for (int i = 0; i < count; i++) {
                    double x = (pos.getMinBlockX() + random.nextFloat() * 16);
                    double y = slvl.getHeight(Heightmap.Types.MOTION_BLOCKING,
                            (int) x, pos.getMinBlockZ()) + random.nextFloat() * 3f;
                    double z = (pos.getMinBlockZ() + random.nextFloat() * 16);

                    slvl.sendParticles(particle, x, y, z, 1,
                            0.5, 0.1, 0.5,
                            0.01
                    );
                }
            }
        }
    }

    private SimpleParticleType particleForType(DSPType type) {
        return switch (type) {
            case D_D_DAMAGEDIRECTIVE    -> ParticleTypes.CRIMSON_SPORE;
            case D_BM_BIOMASSDEFICIT    -> ParticleTypes.WARPED_SPORE;
            case D_BP_BIOMASSOVERGENERATION -> ParticleTypes.SPORE_BLOSSOM_AIR;
            case D_MM_MOBDEFICIT        -> ParticleTypes.MYCELIUM;
            case D_MP_MOBOVERPOPULATION -> ParticleTypes.ASH;
        };
    }
}
