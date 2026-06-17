package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_converter;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_turret.AmalTurret;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseAmalgamation;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.AmalgamationDSPConversions;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPThreshold;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPType;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class AmalConverter extends BaseAmalgamation {
    public AmalConverter(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        thresholds.add(new DSPThreshold(DSPType.D_D_DAMAGEDIRECTIVE, 400, () -> {
            AmalgamationDSPConversions.convertToProtective(this);
        }));
        thresholds.add(new DSPThreshold(DSPType.D_MM_MOBDEFICIT, 400, () -> {
            AmalgamationDSPConversions.convertToMobgen(this);
        }));
    }

    private static final EntityDataAccessor<Integer> CONVERT_RADIUS = SynchedEntityData.defineId(AmalConverter.class, EntityDataSerializers.INT);

    // ##### Entity setup and stats ##### //
    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 30D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 6D)
                .add(Attributes.ARMOR_TOUGHNESS, 3D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ATTACK_SPEED, 2D)
                .add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            if(tickCount % 200 == 0) {
                //Increase radius
                entityData.set(CONVERT_RADIUS, Math.min(20, entityData.get(CONVERT_RADIUS)+1));
                playSound(SoundEvents.FIRE_EXTINGUISH);
//                StaticSiliconiteMethods.debugLog(entityData.get(CONVERT_RADIUS)+"");

                //convert blocks in that radius
                AABB area = new AABB(blockPosition()).inflate(entityData.get(CONVERT_RADIUS));

                for(BlockPos pos : BlockPos.betweenClosed(
                        BlockPos.containing(area.minX - 0.5, area.minY - 0.5, area.minZ - 0.5),
                        BlockPos.containing(area.maxX + 0.5, area.maxY + 0.5, area.maxZ + 0.5))) {
                    BlockState state = slvl.getBlockState(pos);
                    if(pos.distSqr(blockPosition()) <= entityData.get(CONVERT_RADIUS)*entityData.get(CONVERT_RADIUS)
                            && !state.isAir()
                            && state.getDestroySpeed(slvl, pos) > 0.2) {
                        //In the infection radius
                        ResourceLocation blockKey = BuiltInRegistries.BLOCK.getKey(state.getBlock());
                        Block convertToBlock = StaticSiliconiteMethods.infectionConversionMap.get(state.getBlock());

                        double dstSquared = pos.distSqr(blockPosition());
                        double dstNormalized = dstSquared/(entityData.get(CONVERT_RADIUS)*entityData.get(CONVERT_RADIUS));

                        double dstBiased = -1*(dstNormalized*dstNormalized)+2*dstNormalized;

                        //Gradient
                        if(dstBiased < 0.2) convertToBlock = ModBlocks.SILICATE_BLOCK_L1.get();
                        else if(dstBiased < 0.45) convertToBlock = ModBlocks.SILICATE_BLOCK_L2.get();
                        else if(dstBiased < 0.66) convertToBlock = ModBlocks.SILICATE_BLOCK_L3.get();

                        if(state.getBlock() == convertToBlock) continue;
                        if(StaticSiliconiteMethods.blockConversionBlacklist.contains(state.getBlock())) continue;
                        if(convertToBlock == null && blockKey.getNamespace().equals(Chemosynthesis.MODID)) continue;
                        if(convertToBlock == null && !blockKey.getNamespace().equals(Chemosynthesis.MODID)) convertToBlock = ModBlocks.SILICATE_BLOCK_L3.get();
//                        slvl.destroyBlock(pos, false);
                        slvl.setBlock(pos, convertToBlock.defaultBlockState(), 3);
                    }
                }
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CONVERT_RADIUS, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(CONVERT_RADIUS, tag.getInt("convert_radius"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("convert_radius", entityData.get(CONVERT_RADIUS));
    }
}
