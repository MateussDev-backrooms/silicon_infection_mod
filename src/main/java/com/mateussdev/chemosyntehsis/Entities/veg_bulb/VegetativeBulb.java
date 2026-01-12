package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import com.mateussdev.chemosyntehsis.Blocks.TendrilBlock;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

import static com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods.spawnBloodBurst;
import static net.minecraft.world.level.block.MultifaceBlock.getFaceProperty;

public class VegetativeBulb extends BaseOrganelle {
    public VegetativeBulb(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

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
    protected boolean shouldSpawnTendrils() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();

        if (mustEvolve && evolution_t++ > 20) {
            mergeIntoRoller();
        }

        if(tickCount % 180 == 0) {
            if (!level().isClientSide && !this.mustEvolve) {
                List<VegetativeBulb> nearby = level().getEntitiesOfClass(
                        VegetativeBulb.class,
                        this.getBoundingBox().inflate(1.2D),
                        c -> c != this && !c.mustEvolve
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
                c -> c.mustEvolve
        );

        // Only ONE chunk does the spawn
        if (all.stream().anyMatch(c -> c.getId() < this.getId())) return;

        // Effects
        spawnBloodBurst(slvl, this.blockPosition());
        slvl.playSound(null, blockPosition(), SoundEvents.WARDEN_EMERGE, SoundSource.HOSTILE, 1f, 3f);

        // Spawn Cluster
        VegetativeRoller vegetativeRoller = ModEntities.VEG_ROLLER.get().create(slvl);
        vegetativeRoller.moveTo(this.getX(), this.getY(), this.getZ());
        slvl.addFreshEntity(vegetativeRoller);

        // Consume all chunks
        for (VegetativeBulb c : all) {
            c.discard();
        }
    }

    private void initiateMerge(List<VegetativeBulb> others) {
        this.mustEvolve = true;

        for (VegetativeBulb c : others) {
            c.mustEvolve = true;
        }

        if (level() instanceof ServerLevel slvl) {
            spawnBloodBurst(slvl, blockPosition());

            slvl.scheduleTick(this.blockPosition(), Blocks.AIR, 20);
        }
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    protected int evolvesAtMetabolism() {
        return 20;
    }

    

    @Override
    public void evolve() {
        if(this.level() instanceof ServerLevel slvl) {
            List<BaseOrganelle> nearby_vegs = slvl.getEntitiesOfClass(
                    BaseOrganelle.class,
                    this.getBoundingBox().inflate(2),
                    c -> true
            );
            if(nearby_vegs.size() < 5) {
                VegetativeRoller roller = ModEntities.VEG_ROLLER.get().create(slvl);
                roller.moveTo(blockPosition().getCenter());
                slvl.addFreshEntity(roller);
                StaticSiliconiteMethods.spawnTransformationParticle(slvl, blockPosition());
                this.discard();
            }

        }
    }
}
