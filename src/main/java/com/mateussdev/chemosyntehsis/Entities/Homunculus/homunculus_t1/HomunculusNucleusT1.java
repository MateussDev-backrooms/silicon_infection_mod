package com.mateussdev.chemosyntehsis.Entities.Homunculus.homunculus_t1;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.genome.GenomeCarrier;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Gene;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.HomunculusBrain;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IHomunculus;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IHomunculusContext;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HomunculusNucleusT1 extends BaseAmalgamation implements IHomunculus, IHomunculusContext {
    public HomunculusNucleusT1(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.brain = new HomunculusBrain(this);
    }
    private HomunculusBrain brain;
    private static final EntityDataAccessor<Optional<UUID>> HOMUNCULUS_UUID =
            SynchedEntityData.defineId(HomunculusNucleusT1.class, EntityDataSerializers.OPTIONAL_UUID);

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 45D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HOMUNCULUS_UUID, Optional.of(UUID.randomUUID()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("homunculusBrain", brain.serialize());
        tag.putUUID("homunculusUUID", getHomunculusId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("homunculusBrain")) {
            brain.deserialize(tag.getCompound("homunculusBrain"));
        }
        if (tag.hasUUID("homunculusUUID")) {
            this.entityData.set(HOMUNCULUS_UUID, Optional.of(tag.getUUID("homunculusUUID")));
        }
    }

    // ===== Homunculus boilerplate ===== //

    @Override
    public HomunculusBrain getHomunculusBrain() {
        return brain;
    }

    @Override
    public ServerLevel getServerLevel() {
        if(level() instanceof ServerLevel slvl) return slvl;
        return null;
    }

    @Override
    public Vec3 getPosition() {
        return this.position();
    }

    @Override
    public void spawnGenomeCarrier(Gene gene) {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        GenomeCarrier carrier = ModEntities.GENOME_CARRIER.get().create(serverLevel);
        if (carrier != null) {
            carrier.setPos(this.getX(), this.getY() + 1, this.getZ());
            carrier.carriedGene = gene;
            carrier.hostHomunculus = this.getHomunculusId();
            serverLevel.addFreshEntity(carrier);
        }
    }

    @Override
    public void onCycleCompleted(List<Gene> topGenes) {
        //TODO: Sound effect, visuals and animations
    }

    @Override
    public UUID getHomunculusId() {
        return this.getUUID();
    }

    @Override
    public void tick() {
        super.tick();
        if(level() instanceof ServerLevel slvl) {
            brain.tickBrain();
        }
    }
}
