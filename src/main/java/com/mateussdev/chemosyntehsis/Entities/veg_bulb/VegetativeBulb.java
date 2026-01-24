package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods.spawnBloodBurst;

public class VegetativeBulb extends BaseOrganelle {
    public VegetativeBulb(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    public boolean mustMerge = false;

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public boolean shouldSpawnTendrils() {
        return true;
    }
    List<VegetativeBulb> nearby = new ArrayList<>();
    @Override
    public void tick() {
        super.tick();

        if (mustMerge && evolution_t++ > 20) {
            mergeIntoRoller();
        }

        if(tickCount % 180 == 0) {
            if (!level().isClientSide && !this.mustMerge) {
                nearby = level().getEntitiesOfClass(
                        VegetativeBulb.class,
                        new AABB(blockPosition()).inflate(1D),
                        c -> c != this && !c.mustMerge
                );

                if (nearby.size() + 1 >= 5) {
                    initiateMerge(nearby);
                }
            }
        }
    }

    private void mergeIntoRoller() {
        if (!(level() instanceof ServerLevel slvl)) return;

        List<VegetativeBulb> all = slvl.getEntitiesOfClass(
                VegetativeBulb.class,
                this.getBoundingBox().inflate(1.2D),
                c -> c.mustMerge
        );

        // Only ONE chunk does the spawn
        if (all.stream().anyMatch(c -> c.getId() < this.getId())) return;

        // Effects
        spawnBloodBurst(slvl, this.blockPosition());
        slvl.playSound(null, blockPosition(), SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 1f, 3f);

        // Spawn Cluster
        VegetativeRoller vegetativeRoller = ModEntities.VEG_ROLLER.get().create(slvl);
        vegetativeRoller.moveTo(this.getX(), this.getY(), this.getZ());
        slvl.addFreshEntity(vegetativeRoller);

        if(slvl.getEntities(vegetativeRoller, vegetativeRoller.getBoundingBox()).size() > 1) vegetativeRoller.discard();

        // Consume all chunks
        for (VegetativeBulb c : all) {
            c.discard();
        }
    }

    private void initiateMerge(List<VegetativeBulb> others) {
        this.mustMerge = true;

        for (VegetativeBulb c : others) {
            c.mustMerge = true;
        }

        if (level() instanceof ServerLevel slvl) {
            Vec3 center = this.position();
            for (VegetativeBulb c : others) {
                Vec3 dir = center.subtract(c.position()).normalize();
                c.setDeltaMovement(dir.scale(0.5));
            }

            slvl.scheduleTick(this.blockPosition(), Blocks.AIR, 20);
        }
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    protected int evolvesAtMetabolism() {
        return 50;
    }

    

    @Override
    public void evolve() {
        if(this.level() instanceof ServerLevel slvl) {
            List<BaseOrganelle> nearby_vegs = slvl.getEntitiesOfClass(
                    BaseOrganelle.class,
                    this.getBoundingBox().inflate(1.6),
                    c -> true
            );
            if(nearby_vegs.size() < 5) {
                VegetativeRoller roller = ModEntities.VEG_ROLLER.get().create(slvl);
                roller.moveTo(blockPosition().getCenter());
                slvl.addFreshEntity(roller);
                StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
                this.discard();
            } else {
                //Jump somewhere farther away
                BulbProjectileEntity shard = new BulbProjectileEntity(level(),this);
                Vec3i shootDir = getAttachDir().getNormal();
                shard.shoot(
                        (float) shootDir.getX() * (random.nextFloat()*6f),
                        (float) shootDir.getY() * (random.nextFloat()*6f),
                        (float) shootDir.getZ() * (random.nextFloat()*6f),
                        0.8f, // speed
                        10.0f // inaccuracy
                );
                slvl.playSound(null, blockPosition(), SoundEvents.ARROW_SHOOT, SoundSource.HOSTILE, 1f, 3f);
                level().addFreshEntity(shard);
                this.discard();
            }

        }
    }
}
