package net.Mateuss.Chemosynthesis.entity.ai_goals;

import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.Mateuss.Chemosynthesis.entity.projectile.AbstractHarpoonProjectile;
import net.minecraft.world.entity.ai.goal.Goal;

public class ThrowHarpoonGoal extends Goal {

    SiliconiteBase parent;
    int reel_distance;
    final int Max_reel_time;
    int reel_t;
    double reel_speed;
    final int Max_shoot_cooldown;
    int shoot_cooldown;

    AbstractHarpoonProjectile harpoon;

    public ThrowHarpoonGoal(SiliconiteBase parent, int reel_distance, double reel_speed, int max_reel_time, int shoot_cooldown) {
        this.parent = parent;
        this.reel_speed = reel_speed;
        this.reel_distance = reel_distance;
        this.Max_reel_time = max_reel_time;
        this.Max_shoot_cooldown = shoot_cooldown;

        this.reel_t = 0;
        this.shoot_cooldown = 0;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean canUse() {

        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}