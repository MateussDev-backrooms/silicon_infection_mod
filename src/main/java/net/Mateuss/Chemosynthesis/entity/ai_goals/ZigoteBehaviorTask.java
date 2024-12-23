package net.Mateuss.Chemosynthesis.entity.ai_goals;

import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

public class ZigoteBehaviorTask extends Goal {
    private final SiliconiteBase mob;
    private final double speed;
    private final int radius;
    private BlockPos targetPos;
    private int waitTicks;
    private boolean wasDamaged;

    public ZigoteBehaviorTask(SiliconiteBase mob, double speed, int radius) {
        this.mob = mob;
        this.speed = speed;
        this.radius = radius;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        // Only start if not already executing and has a world
        return mob.level() instanceof ServerLevel;
    }

    @Override
    public void start() {
        findNewPosition();
    }

    @Override
    public boolean canContinueToUse() {
        // Continue as long as the mob hasn't spawned/despawned
        return true;
    }

    @Override
    public void tick() {
        if (wasDamaged) {
            findNewPosition(); // React to damage
            wasDamaged = false;
        }

        if (mob.getNavigation().isDone() && targetPos != null) {
            if (waitTicks > 0) {
                waitTicks--;
            } else {
                mob.evolve();
            }
        }
    }

    private void findNewPosition() {
        RandomSource random = mob.getRandom();

        // Random position within radius
        int x = mob.getBlockX() + random.nextInt(radius * 2) - radius;
        int y = mob.getBlockY(); // Keep on the same vertical level for simplicity
        int z = mob.getBlockZ() + random.nextInt(radius * 2) - radius;

        targetPos = new BlockPos(x, y, z);
        mob.getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), speed);
        waitTicks = 200; // 10 seconds (20 ticks per second)
    }

    @Override
    public void stop() {
        // Reset state if stopped
        targetPos = null;
        waitTicks = 0;
        wasDamaged = false;
    }

    public void onDamageTaken() {
        wasDamaged = true;
    }
}
