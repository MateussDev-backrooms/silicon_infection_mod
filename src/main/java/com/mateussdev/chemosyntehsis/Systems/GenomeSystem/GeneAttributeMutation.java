package com.mateussdev.chemosyntehsis.Systems.GenomeSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.UUID;

public class GeneAttributeMutation {
    //This class will handle changing the attributes of a mob by modifying ratios between certain values
    //These values are then used to multiply the attributes of a mob when a gene is applied to it, changing their health, damage, speed and etc

    //Eg: value of 1 represents 1/1, which distributes the health and speed ratios equally
    //value of 4 represents 4/1, which means attribute A gets multiplied by 2 and attribute B gets divided by 2
    //value of 0.25 represents 1/4, which means attribute A gets multiplied by 0.50 (IE divided by 4) and attribute B gets divided by 0.50 (IE multiplier by 2)

    //This system prevents OP mobs from being generated, and ensures that certain attributes have strengths and weaknesses

    public float health_speed = 1f;
    public float health_armor = 1f;
    public float armor_attackDamage = 1f;
    public float armorToughness_armor = 1f;
    public float attackSpeed_attackDamage = 1f;
    public float attackSpeed_attackKnockback = 1f;
    public float speed_knockbackResistance = 1f;
    public float followRange_attackDamage = 1f;
    public float followRange_speed = 1f;

    public void applyAttributesSequentially(Mob mob) {
        //First, calculate the exact multipliers of the attributes
        //For a given ratio between attrA_attrB, we add a multiplier to the variables here
        //If the attribute is on the left side (attrA), we add its square root
        //If the attribute is on the right side (attrB), we add 1 / its square root

        UUID _id1 = UUID.randomUUID(); //layer 1
        UUID _id2 = UUID.randomUUID(); //layer 2

        float healthMult = Mth.sqrt(health_speed) * Mth.sqrt(health_armor) * (1/Mth.sqrt(followRange_speed));
        float speedMult = (1/Mth.sqrt(health_speed)) * Mth.sqrt(speed_knockbackResistance);
        float armorMult = Mth.sqrt(armor_attackDamage) * (1/Mth.sqrt(health_armor)) * (1/Mth.sqrt(armorToughness_armor));
        float armorToughnessMult = Mth.sqrt(armorToughness_armor);
        float attackDamageMult = (1/Mth.sqrt(armor_attackDamage)) * (1/Mth.sqrt(attackSpeed_attackDamage)) * (1/Mth.sqrt(followRange_attackDamage));
        float attackSpeedMult = Mth.sqrt(attackSpeed_attackKnockback) * Mth.sqrt(attackSpeed_attackDamage);
        float attackKnockbackMult = (1/Mth.sqrt(attackSpeed_attackKnockback));
        float knockbackResistanceMult = (1/Mth.sqrt(speed_knockbackResistance));
        float followRangeMult = Mth.sqrt(followRange_attackDamage) * Mth.sqrt(followRange_speed);

        //Make attribute instances easier to read by putting them into vars
        AttributeInstance health = mob.getAttribute(Attributes.MAX_HEALTH);
        AttributeInstance speed = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        AttributeInstance armor = mob.getAttribute(Attributes.ARMOR);
        AttributeInstance armorToughness = mob.getAttribute(Attributes.ARMOR_TOUGHNESS);
        AttributeInstance attackDmg = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance attackSpeed = mob.getAttribute(Attributes.ATTACK_SPEED);
        AttributeInstance attackKnockback = mob.getAttribute(Attributes.ATTACK_KNOCKBACK);
        AttributeInstance knockbackResistance = mob.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        AttributeInstance followRange = mob.getAttribute(Attributes.FOLLOW_RANGE);

        //Reset the existing modifiers
        health.removeModifiers();
        speed.removeModifiers();
        armor.removeModifiers();
        armorToughness.removeModifiers();
        attackDmg.removeModifiers();
        attackSpeed.removeModifiers();
        attackKnockback.removeModifiers();
        knockbackResistance.removeModifiers();
        followRange.removeModifiers();

        //Apply a +1 to attributes that are zero by default
        applyBaselineToNullAttribute(mob, health, 1f, _id1);
        applyBaselineToNullAttribute(mob, speed, 1f, _id1);
        applyBaselineToNullAttribute(mob, armor, 1f, _id1);
        applyBaselineToNullAttribute(mob, armorToughness, 1f, _id1);
        applyBaselineToNullAttribute(mob, attackDmg, 1f, _id1);
        applyBaselineToNullAttribute(mob, attackSpeed, 1f, _id1);
        applyBaselineToNullAttribute(mob, attackKnockback, 1f, _id1);
        applyBaselineToNullAttribute(mob, knockbackResistance, 1f, _id1);
        applyBaselineToNullAttribute(mob, followRange, 1f, _id1);

        //Apply modifier multipliers
        applyMultiplierToAttribute(mob, health, healthMult, _id2);
        applyMultiplierToAttribute(mob, speed, speedMult/3, _id2); //nerf speed cuz what the hell
        applyMultiplierToAttribute(mob, armor, armorMult, _id2);
        applyMultiplierToAttribute(mob, armorToughness, armorToughnessMult, _id2);
        applyMultiplierToAttribute(mob, attackDmg, attackDamageMult, _id2);
        applyMultiplierToAttribute(mob, attackSpeed, attackSpeedMult, _id2);
        applyMultiplierToAttribute(mob, attackKnockback, attackKnockbackMult, _id2);
        applyMultiplierToAttribute(mob, knockbackResistance, knockbackResistanceMult, _id2);
        applyMultiplierToAttribute(mob, followRange, followRangeMult, _id2);
    }

    private void applyMultiplierToAttribute(Mob mob, AttributeInstance attribute, double multiplier, UUID uuid) {
        if(multiplier == 1.0d) return;

        if(attribute != null) {
            attribute.removeModifier(uuid);
            attribute.addPermanentModifier(new AttributeModifier(uuid, attribute.toString()+"genemult", multiplier, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }
    private void applyBaselineToNullAttribute(Mob mob, AttributeInstance attribute, double addition, UUID uuid) {
        if(attribute != null && attribute.getValue() <= 0) {
            attribute.removeModifier(uuid);
            attribute.addPermanentModifier(new AttributeModifier(uuid, attribute.toString()+"genemult", addition, AttributeModifier.Operation.ADDITION));
        }
    }

    public int calculateCost() {
        //TODO: calculate the cost of the attribute modification by checking how far each attribute ratio is from 1, tallying up and then rounding to th enearest int
        return 1;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("health_speed", this.health_speed);
        tag.putFloat("health_armor", this.health_armor);
        tag.putFloat("armor_attackDamage", this.armor_attackDamage);
        tag.putFloat("armorToughness_armor", this.armorToughness_armor);
        tag.putFloat("attackSpeed_attackDamage", this.attackSpeed_attackDamage);
        tag.putFloat("attackSpeed_attackKnockback", this.attackSpeed_attackKnockback);
        tag.putFloat("speed_knockbackResistance", this.speed_knockbackResistance);
        tag.putFloat("followRange_attackDamage", this.followRange_attackDamage);
        tag.putFloat("followRange_speed", this.followRange_speed);

        return tag;
    }

    public void deserialize(CompoundTag tag) {
        this.health_speed = tag.getFloat("health_speed");
        this.health_armor = tag.getFloat("health_armor");
        this.armor_attackDamage = tag.getFloat("armor_attackDamage");
        this.armorToughness_armor = tag.getFloat("armorToughness_armor");
        this.attackSpeed_attackDamage = tag.getFloat("attackSpeed_attackDamage");
        this.attackSpeed_attackKnockback = tag.getFloat("attackSpeed_attackKnockback");
        this.speed_knockbackResistance = tag.getFloat("speed_knockbackResistance");
        this.followRange_attackDamage = tag.getFloat("followRange_attackDamage");
        this.followRange_speed = tag.getFloat("followRange_speed");
    }

    public GeneAttributeMutation copy() {
        GeneAttributeMutation copy = new GeneAttributeMutation();
        copy.health_speed = this.health_speed;
        copy.health_armor = this.health_armor;
        copy.armor_attackDamage = this.armor_attackDamage;
        copy.armorToughness_armor = this.armorToughness_armor;
        copy.attackSpeed_attackDamage = this.attackSpeed_attackDamage;
        copy.attackSpeed_attackKnockback = this.attackSpeed_attackKnockback;
        copy.speed_knockbackResistance = this.speed_knockbackResistance;
        copy.followRange_attackDamage = this.followRange_attackDamage;
        copy.followRange_speed = this.followRange_speed;
        return copy;
    }
}
