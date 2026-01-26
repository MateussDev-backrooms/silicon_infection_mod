package com.mateussdev.chemosyntehsis.Util;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;

public class GlobalMobCap {

    public static final int BULB_CAP = 250;

    public static boolean canSpawnUnique(ServerLevel level, EntityType<?> type, BlockPos pos, int maxCount, int radius) {
        AABB searchBox = new AABB(pos).inflate(radius);

        long count = level.getEntitiesOfClass(BaseSiliconite.class, searchBox,
                EntitySelector.NO_CREATIVE_OR_SPECTATOR.and(entity -> entity.getType() == type)
        ).size();

        return count < maxCount;
    }

    public static boolean canSpawnGeneral(ServerLevel level, BlockPos pos, int maxCount, int radius) {
        AABB searchBox = new AABB(pos).inflate(radius);

        long count = level.getEntitiesOfClass(BaseSiliconite.class, searchBox,
                EntitySelector.NO_CREATIVE_OR_SPECTATOR
        ).size();

        return count < maxCount;
    }
}
