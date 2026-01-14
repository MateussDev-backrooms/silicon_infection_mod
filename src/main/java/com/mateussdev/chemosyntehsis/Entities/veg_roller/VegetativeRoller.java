package com.mateussdev.chemosyntehsis.Entities.veg_roller;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.vasc_roller.VascularRoller;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VegetativeRoller extends BaseOrganelle {
    public VegetativeRoller(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setNoGravity(true);
        this.setYBodyRot(0);
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected int evolvesAtMetabolism() {
        return 50;
    }

    @Override
    public void evolve() {
        if(this.level() instanceof ServerLevel slvl) {
            List<VascularRoller> vasculars = slvl.getEntitiesOfClass(
                    VascularRoller.class,
                    this.getBoundingBox().inflate(3),
                    c -> true
            );
            if(vasculars.isEmpty() && random.nextFloat() < 0.7f) {
                VascularRoller vascularRoller = ModEntities.VASC_ROLLER.get().create(slvl);
                vascularRoller.moveTo(position());
                slvl.addFreshEntity(vascularRoller);
                StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
                this.discard();
            } else {
                List<SiliconRoller> rollers = slvl.getEntitiesOfClass(
                        SiliconRoller.class,
                        this.getBoundingBox().inflate(3),
                        c -> true
                );
                if(rollers.size() > 5) {
                    //reset metabolism
                    entityData.set(METABOLISM_VALUE, 0);
                    return;
                }
                SiliconRoller roller = ModEntities.SILICON_ROLLER.get().create(slvl);
                roller.moveTo(position());
                slvl.addFreshEntity(roller);
                StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
                this.discard();
            }
        }
    }

    @Override
    public boolean shouldSpawnTendrils() {
        return true;
    }
}
