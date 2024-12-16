package net.Mateuss.Chemosynthesis.entity.living_entities.pure;

import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.Nullable;

public class EntitySiliconTripod extends SiliconiteBase {
    public EntitySiliconTripod(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }





    @Override
    public void baseTick() {
        super.baseTick();
    }

    @Override
    protected int requiredMetabolismForEvolution() {
        return 60;
    }

    @Override
    public void evolve() {
        EntitySiliconRoller roller = ModEntities.SILICON_ROLLER.get().create(this.level());
        if(roller != null) {
            roller.moveTo(this.getX()+this.random.nextDouble(), this.getY(), this.getZ()+this.random.nextDouble());
            this.level().addFreshEntity(roller);
            ServerLevel lvl = (ServerLevel) this.level();
            lvl.sendParticles(
                    ParticleTypes.EXPLOSION,
                    this.getX() + 0.5,
                    this.getY() + 0.5,
                    this.getZ() + 0.5,
                    1,
                    0,
                    0,
                    0,
                    0.1
            );
            this.discard();
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.0d));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 3D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_DAMAGE, 1D);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.DRIPSTONE_BLOCK_PLACE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DRIPSTONE_BLOCK_FALL;
    }

    @Override
    public boolean isAffectedByPotions() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }
}
