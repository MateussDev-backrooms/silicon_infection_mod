package com.mateussdev.chemosyntehsis.Entities.vasc_roller;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie.TethZombie;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VascularRoller extends BaseOrganelle {
    public VascularRoller(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected boolean shouldSpawnTendrils() {
        return true;
    }


    //Amalgamation

    @Override
    public void tick() {
        super.tick();
        if(this.level() instanceof ServerLevel slvl) {
            if(tickCount % 40 == 0) {
                //Transform based on nearby
                List<BaseTethered> nearby_tethered = slvl.getEntitiesOfClass(
                        BaseTethered.class,
                        new AABB(blockPosition()).inflate(4)
                );
                if(!nearby_tethered.isEmpty()) {

                    BaseTethered converter = nearby_tethered.get(0);

                    if(converter instanceof TethZombie) {
                        createAmalgamation(ModEntities.AMAL_ZOMBIE.get(), slvl, converter);
                    }

                }
            }
        }
    }

    protected void createAmalgamation(EntityType<? extends BaseAmalgamation> type, ServerLevel slvl, LivingEntity converter) {
        BaseAmalgamation amal = type.create(slvl);
        amal.moveTo(blockPosition().getCenter());
        slvl.addFreshEntity(amal);
        StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
        converter.discard();
        this.discard();
    }
}
