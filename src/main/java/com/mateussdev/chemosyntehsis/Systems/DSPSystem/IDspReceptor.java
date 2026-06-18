package com.mateussdev.chemosyntehsis.Systems.DSPSystem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;

public interface IDspReceptor {

    EnumMap<DSPType, Float> getInternalBuffer();
    List<DSPThreshold> getThresholds();

    default void emitDSP(DSPType type, float v, Mob self) {
        ChunkPos pos = new ChunkPos(self.blockPosition());
        ServerLevel serverLevel = (ServerLevel) self.level();
        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(serverLevel);

        //Write into the aggregate field
        data.dspMap
                .computeIfAbsent(pos, k -> new EnumMap<>(DSPType.class))
                .merge(type, v, Float::sum);
        data.setDirty();
    }

    default void absorbDSP(Mob self) {
        ServerLevel serverLevel = (ServerLevel) self.level();
        ChunkPos pos = new ChunkPos(self.blockPosition());
        DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(serverLevel);

        //Absorb from chunk field
        EnumMap<DSPType, Float> field = data.dspMap.get(pos);
        if (field != null) {
            for (DSPType type : DSPType.values()) {
                float fieldValue = field.getOrDefault(type, 0f);
                if (fieldValue <= 0f) continue;

                if(getInternalBuffer().containsKey(type)) {
                    if (getInternalBuffer().get(type) > bufferCapacity()) continue;
                }


                float absorbed = Math.min(fieldValue, absorbtionSpeed());
                field.merge(type, -absorbed, Float::sum);
                getInternalBuffer().merge(type, absorbed, Float::sum);
            }
        }

        //Decay internal buffer
        for (DSPType type : DSPType.values()) {
            getInternalBuffer().computeIfPresent(type, (k, v) -> Math.max(0f, v - bufferDecayRate()));
        }

        // Check thresholds
        List<DSPThreshold> triggered = new ArrayList<>();
        for (DSPThreshold threshold : getThresholds()) {
            float buffered = getInternalBuffer().getOrDefault(threshold.type(), 0f);
            if (buffered >= threshold.threshold()) {
                triggered.add(threshold);
            }
        }

        for (DSPThreshold threshold : triggered) {
            threshold.onExceed().run();
            getInternalBuffer().merge(threshold.type(), -threshold.threshold(), Float::sum);
        }

        data.setDirty();
    }

    // ===== Customizable ===== //

    default float bufferDecayRate() {
        return 0.1f;
    }

    default float bufferCapacity() {
        return 500f;
    }

    default float absorbtionSpeed() {
        return 5f;
    }
}
