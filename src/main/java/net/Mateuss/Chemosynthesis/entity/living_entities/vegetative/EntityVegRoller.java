package net.Mateuss.Chemosynthesis.entity.living_entities.vegetative;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;

public class EntityVegRoller extends VegetativeBase {
    public EntityVegRoller(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {

        this.setPersistenceRequired();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0D, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 0, true, false, this::isNotSiliconite));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 12D)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.FOLLOW_RANGE, 30D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 1D)
                .add(Attributes.ATTACK_DAMAGE, 4D);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.DRIPSTONE_BLOCK_BREAK;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WARDEN_DEATH;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void remove(RemovalReason pReason) {

        super.remove(pReason);
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }
}
