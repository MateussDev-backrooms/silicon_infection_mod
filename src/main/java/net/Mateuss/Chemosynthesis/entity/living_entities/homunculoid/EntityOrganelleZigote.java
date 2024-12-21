package net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid;

import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.SiliconiteBase;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.VegetativeBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.UUID;

public class EntityOrganelleZigote extends SiliconiteBase {
    public EntityOrganelleZigote(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private UUID parentHomunculusUUID=null;
    private EntityHomunculus parentHomunculusEntity=null;

    public float veinThickness = 0.1f + this.level().random.nextFloat()*0.25f;
    public float yOffset = this.level().random.nextFloat()*0.66f;
    public float randomBrightness = this.level().random.nextFloat()*0.4f;

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
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FOLLOW_RANGE, 25D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_DAMAGE, 1D);
    }

    public EntityHomunculus getParent() {
        return parentHomunculusEntity;
    }

    public void setParentUUID(UUID uuid) {
        parentHomunculusUUID = uuid;
    }

    public void setParentEntity(EntityHomunculus homunculus) {
        parentHomunculusEntity = homunculus;
        parentHomunculusUUID = homunculus.getUUID();
    }

    public Entity findNearestHomunculus() {
        List<Entity> nearbyList = level().getEntities(
                (Entity) null, new AABB(this.blockPosition()).inflate(12),
                entity -> entity instanceof EntityHomunculus
        );
        for (Entity entity : nearbyList) {
            if(distanceTo(entity) <= 12) {
                return entity;
            }
        }
        return null;
    }
}
