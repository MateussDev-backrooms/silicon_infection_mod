package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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

    public final Map<ChunkPos, EnumMap<DSPType, Float>> dspMap = new HashMap<>();
    private static final float PARTICLE_VISIBILITY_THRESHOLD = 10f;

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag list = new ListTag();

        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> entry : dspMap.entrySet()) {
            ChunkPos pos = entry.getKey();
            EnumMap<DSPType, Float> innerMap = entry.getValue();

            CompoundTag chunkTag = new CompoundTag();
            chunkTag.putInt("x", pos.x);
            chunkTag.putInt("z", pos.z);

            CompoundTag dspTag = new CompoundTag();
            for (Map.Entry<DSPType, Float> innerEntry : innerMap.entrySet()) {
                float value = innerEntry.getValue();
                // Skip zero/negative values to save space
                if (value > 0.0f) {
                    dspTag.putFloat(innerEntry.getKey().name(), value);
                }
            }

            // Only save if there's actually data
            if (!dspTag.isEmpty()) {
                chunkTag.put("dsp", dspTag);
                list.add(chunkTag);
            }
        }

        pCompoundTag.put("chunks", list);
        return pCompoundTag;
    }

    public static DirectiveSignalingProteinData load(CompoundTag tag) {
        DirectiveSignalingProteinData data = new DirectiveSignalingProteinData();
        ListTag list = tag.getList("chunks", Tag.TAG_COMPOUND);

        for (int i = 0; i < list.size(); i++) {
            CompoundTag chunkTag = list.getCompound(i);
            int x = chunkTag.getInt("x");
            int z = chunkTag.getInt("z");
            ChunkPos pos = new ChunkPos(x, z);

            CompoundTag dspTag = chunkTag.getCompound("dsp");
            EnumMap<DSPType, Float> innerMap = new EnumMap<>(DSPType.class);

            for (String key : dspTag.getAllKeys()) {
                try {
                    DSPType type = DSPType.valueOf(key);
                    float val = dspTag.getFloat(key);
                    if (val > 0.0f) {
                        innerMap.put(type, val);
                    }
                } catch (IllegalArgumentException ignored) {
                    //Skip unknown enum values
                }
            }

            if (!innerMap.isEmpty()) {
                data.dspMap.put(pos, innerMap);
            }
        }

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

        // First, collect all diffusion (without modifying dspMap)
        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> entry : dspMap.entrySet()) {
            ChunkPos pos = entry.getKey();
            EnumMap<DSPType, Float> field = entry.getValue();
            for (DSPType type : DSPType.values()) {
                float value = field.getOrDefault(type, 0f);
                if (value <= 0f) continue;
                if (value > DIFFUSION_THRESHOLD) {
                    diffuseChunk(pos, slvl, field, type, value, pendingDiffusion);
                }
            }
        }

        // Now apply all changes: decay + diffusion
        for (Map.Entry<ChunkPos, EnumMap<DSPType, Float>> entry : dspMap.entrySet()) {
            EnumMap<DSPType, Float> field = entry.getValue();
            for (DSPType type : DSPType.values()) {
                float value = field.getOrDefault(type, 0f);
                // Apply decay
                value = Math.max(0f, value - DECAY_RATE);
                // Add incoming diffusion from pending
                if (pendingDiffusion.containsKey(entry.getKey())) {
                    value += pendingDiffusion.get(entry.getKey()).getOrDefault(type, 0f);
                }
                field.put(type, value);
                this.setDirty();
            }
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
        this.setDirty();
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
