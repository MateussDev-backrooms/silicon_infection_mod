package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;

public class GeneAttributeMutation {
    //This class will handle changing the attributes of a mob by modifying ratios between certain values
    //These values are then used to multiply the attributes of a mob when a gene is applied to it, changing their health, damage, speed and etc

    //Eg: value of 1 represents 1/1, which distributes the health and speed ratios equally
    //value of 4 represents 4/1, which means attribute A gets multiplied by 2 and attribute B gets divided by 2
    //value of 0.25 represents 1/4, which means attribute A gets multiplied by 0.50 (IE divided by 4) and attribute B gets divided by 0.50 (IE multiplier by 2)

    //This system prevents OP mobs from being generated, and ensures that certain attributes have strengths and weaknesses

    public float health_speed = 1f;
    public float armor_attackDamage = 1f;
    public float attackSpeed_attackKnockback = 1f;
    public float speed_knockbackResistance = 1f;
    public float followRange_attackDamage = 1f;

    public void applyAttributesSequentially(Mob mob) {
        //TODO: Actually apply the ratios during applyGene() method
    }

    public int calculateCost() {
        //TODO: calculate the cost of the attribute modification by checking how far each attribute ratio is from 1, tallying up and then rounding to th enearest int
        return 1;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("health_speed", this.health_speed);
        tag.putFloat("armor_attackDamage", this.armor_attackDamage);
        tag.putFloat("attackSpeed_attackKnockback", this.attackSpeed_attackKnockback);
        tag.putFloat("speed_knockbackResistance", this.speed_knockbackResistance);
        tag.putFloat("followRange_attackDamage", this.followRange_attackDamage);

        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.health_speed = tag.getFloat("health_speed");
        this.armor_attackDamage = tag.getFloat("armor_attackDamage");
        this.attackSpeed_attackKnockback = tag.getFloat("attackSpeed_attackKnockback");
        this.speed_knockbackResistance = tag.getFloat("speed_knockbackResistance");
        this.followRange_attackDamage = tag.getFloat("followRange_attackDamage");
    }

    public GeneAttributeMutation copy() {
        GeneAttributeMutation copy = new GeneAttributeMutation();
        copy.health_speed = this.health_speed;
        copy.armor_attackDamage = this.armor_attackDamage;
        copy.attackSpeed_attackKnockback = this.attackSpeed_attackKnockback;
        copy.speed_knockbackResistance = this.speed_knockbackResistance;
        copy.followRange_attackDamage = this.followRange_attackDamage;
        return copy;
    }
}
