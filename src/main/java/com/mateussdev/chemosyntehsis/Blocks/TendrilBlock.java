package com.mateussdev.chemosyntehsis.Blocks;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.Vegetated.veg_bulb.VegetativeBulb;
import com.mateussdev.chemosyntehsis.Systems.MobCapSystem.GlobalMobCap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TendrilBlock extends MultifaceBlock {
    private MultifaceSpreader spreader;
    public TendrilBlock(Properties pProperties) {
        super(pProperties
                .speedFactor(0.7f)
                .strength(0.2f)
                .instabreak()
                .noCollission()
        );
        spreader = new MultifaceSpreader(this);
    }

    @Override
    public boolean isRandomlyTicking(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);

        //Sulfurization if not close to an organelle
        int radius = 8;
        AABB area = new AABB(pPos.east(radius).south(radius).below(radius/2), pPos.west(radius).north(radius).above(radius/2));
        List<? extends BaseOrganelle> potentialSupporters = pLevel.getEntitiesOfClass(BaseOrganelle.class, area, entity -> entity.isAlive());
        boolean shouldSulfurize = true;

        if(potentialSupporters.isEmpty()) {
            sulfurize(pPos, pState, pLevel);
        } else {

            //Check the candidates for distance
            for(BaseOrganelle organelle : potentialSupporters) {
                //check distance square
                if(organelle.blockPosition().distSqr(pPos) <= organelle.tendrilSupportRadius()*organelle.tendrilSupportRadius()) {
                    shouldSulfurize = false;
                }
            }

            if(shouldSulfurize) sulfurize(pPos, pState, pLevel);
        }

        //Tendril spreading

        if(pRandom.nextFloat() < 0.45) {
            //Spread randomly if didn't spread to corpse
            this.getSpreader().spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
        }


        //Transform connected blocks

        /// TODO MAKE A BETTER SYSTEM

        //Have a chance to spawn a bulb randomly
        if(pRandom.nextFloat() < 0.1 && GlobalMobCap.canSpawnUnique(pLevel, ModEntities.VEG_BULB.get(), pPos, GlobalMobCap.BULB_CAP, 128)) {
            boolean canSpawnBulb = true;
            for(BaseOrganelle organelle : potentialSupporters) {
                if(organelle.blockPosition().distSqr(pPos) < 4) {
                    canSpawnBulb = false;
                    break;
                }
            }

            if(canSpawnBulb) {
                VegetativeBulb bulb = ModEntities.VEG_BULB.get().create(pLevel);
                bulb.moveTo(pPos.getCenter());
                pLevel.sendParticles(
                        ParticleTypes.POOF,
                        pPos.getX() + 0.5,
                        pPos.getY(),
                        pPos.getZ() + 0.5,
                        1,
                        0,
                        0,
                        0,
                        0.1
                );
                pLevel.playSound(null, pPos, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.HOSTILE, 1f, 1f);
                pLevel.addFreshEntity(bulb);
            }
        }




    }

    private void sulfurize(BlockPos pPos, BlockState pState, ServerLevel pLevel) {
        BlockState sulfurState = ModBlocks.SULFURED_TENDRILS.get().defaultBlockState();

        //fix multi-face states
        for (Direction dir : Direction.values()) {
            BooleanProperty prop = getFaceProperty(dir);
            if (pState.hasProperty(prop)) {
                sulfurState = sulfurState.setValue(prop, pState.getValue(prop));
            }
        }

        pLevel.setBlock(pPos, sulfurState, 3);
        pLevel.playSound(null, pPos, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.BLOCKS, 1f, 1f);
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }
}
