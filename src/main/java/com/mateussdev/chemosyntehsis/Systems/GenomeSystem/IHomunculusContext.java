package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public interface IHomunculusContext {
    ServerLevel getServerLevel();

    Vec3 getPosition();

    void spawnGenomeCarrier(Gene gene);

    void onCycleCompleted(List<Gene> topGenes);

    UUID getHomunculusId();

    Mob getHomunculusMob();
}
