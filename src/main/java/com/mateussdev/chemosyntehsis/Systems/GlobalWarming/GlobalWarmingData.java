package com.mateussdev.chemosyntehsis.Systems.GlobalWarming;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.saveddata.SavedData;

public class GlobalWarmingData extends SavedData {
    private static final String DATA_SAVE_NAME = "silicon_greenhousePoints";
    private float greenhousePoints = 0;

    public String[] phaseNames = {"Dormant", "Interphase", "Prophase", "Metaphase", "Anaphase", "Telophase", "Cytokinesis"};
    public static final int[] PHASE_THRESHOLD = {0, 100, 500, 2500, 10000, 50000, 100000};

    public static GlobalWarmingData get(Level level) {
        return level.getServer().overworld().getDataStorage().computeIfAbsent(
                GlobalWarmingData::load,
                GlobalWarmingData::new,
                DATA_SAVE_NAME
        );
    }

    public void addPoints(float amount) {
        greenhousePoints += amount;
        setDirty();
    }

    public float getPoints() {
        return greenhousePoints;
    }

    public static GlobalWarmingData load(CompoundTag tag) {
        GlobalWarmingData data = new GlobalWarmingData();
        data.greenhousePoints = tag.getInt("GreenhousePoints");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putFloat("GreenhousePoints", greenhousePoints);
        return tag;
    }

    public float getRelativeTemperature() {
        return greenhousePoints / 100.0f;
    }

    public float getAbsoluteTemperature(Level level, BlockPos pos) {
        Biome biome = level.getBiome(pos).value();
        float biomeBaseTemp = Math.min(biome.getBaseTemperature()*25, 45); //transform biome temperature into Celsius.

        long dayTime = level.getDayTime() % 24000L;
        double timeRatio = dayTime / 24000.0;
        double sunFactor = -Math.cos(timeRatio * 2 * Math.PI);
        float timeAdditionalTemp = (float)(sunFactor * 5.0f); // +-5C based on time of day

        return getRelativeTemperature() + biomeBaseTemp + timeAdditionalTemp;
    }

    public int getPhase() {

        for (int i = 0; i < PHASE_THRESHOLD.length - 1; i++) {
            if (greenhousePoints >= PHASE_THRESHOLD[i] && greenhousePoints < PHASE_THRESHOLD[i + 1]) {
                return i;
            }
        }
        return -1;
    }


}
