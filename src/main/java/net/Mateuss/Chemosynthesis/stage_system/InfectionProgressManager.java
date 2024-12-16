package net.Mateuss.Chemosynthesis.stage_system;

import net.minecraft.server.level.ServerLevel;

public class InfectionProgressManager {
    private static final String DATA_NAME = "chemosynthesis_infection";

    public static InfectionProgress get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(InfectionProgress::load, InfectionProgress::new, DATA_NAME);
    }
}
