package com.mateussdev.chemosyntehsis.Blocks;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class TendrilBlock extends MultifaceBlock {
    private MultifaceSpreader spreader;
    public TendrilBlock(Properties pProperties) {
        super(pProperties
                .speedFactor(0.7f)
                .strength(0.2f)
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

        //Sulfurization if not close to a bulb
        for (Map.Entry<EntityType<? extends BaseOrganelle>, Integer> entry : StaticSiliconiteMethods.vegetatedRadiusMap.entrySet()) {
            int radius = entry.getValue();
            AABB area = new AABB(
                    pPos.east(radius).south(radius).below(radius/2),
                    pPos.west(radius).north(radius).above(radius/2)
            );
            List<? extends Entity> list = pLevel.getEntitiesOfClass(
                    entry.getKey().getBaseClass(),
                    area,
                    entity -> entity.isAlive());

            if(list.isEmpty()) {
                BlockState sulfurState = ModBlocks.SULFURED_TENDRILS.get().defaultBlockState();

                //fix multi-face states
                for (Direction dir : Direction.values()) {
                    BooleanProperty prop = getFaceProperty(dir);
                    if (pState.hasProperty(prop)) {
                        sulfurState = sulfurState.setValue(prop, pState.getValue(prop));
                    }
                }

                pLevel.setBlock(pPos, sulfurState, 3);
                return;
            }
        }

        //Tendril spreading

        if(pRandom.nextFloat() < 0.45) {
            //Check and spread to nearby corpses and biomush
            int diagonal = 5;
            //check for biomush or corpse
            for (BlockPos pos : BlockPos.betweenClosed(pPos.offset(-diagonal, -diagonal/2, -diagonal), pPos.offset(diagonal, diagonal/2, diagonal))) {
                if (pLevel.getBlockState(pos).getBlock() instanceof BiomushBlock
                || pLevel.getBlockState(pos).getBlock() instanceof BaseCorpseBlock || pLevel.getFluidState(pos).getType() instanceof LavaFluid) {
                    double angle = Math.atan2(pos.getZ() - pPos.getZ(), pos.getX() - pPos.getX());
                    float angle_deg = (float) (angle * (180/Mth.PI));
                    Direction spreadDirection = Direction.fromYRot(angle_deg);
                    this.getSpreader().spreadFromFaceTowardDirection(
                            pState, pLevel, pPos, spreadDirection, spreadDirection, false
                    );
                }
            }
            //Spread randomly if didn't spread to corpse
            this.getSpreader().spreadFromRandomFaceTowardRandomDirection(pState, pLevel, pPos, pRandom);
        }


        //Transform connected blocks
        if(pLevel.random.nextBoolean()) {
            for(Direction dir : Direction.values()) {
                if(pState.getValue(getFaceProperty(dir))) {

                    BlockPos adj = pPos.relative(dir);
                    BlockState adj_state = pLevel.getBlockState(adj);

                    if(adj_state.getBlock() instanceof BaseCorpseBlock) {
                        pLevel.destroyBlock(adj, false);
                        //TODO If block is a corpse or biomush break block and evolve the nearest bulb
                    }

                    Block replacement = StaticSiliconiteMethods.infectionConversionMap.get(adj_state.getBlock());
                    if(replacement != null) {
                        pLevel.setBlockAndUpdate(adj, replacement.defaultBlockState());
                    }
                }
            }
        }

        //Have a chance to spawn a bulb randomly

        if(pRandom.nextFloat() < 0.2) {
            List<BaseOrganelle> nearby_vegs = pLevel.getEntitiesOfClass(
                    BaseOrganelle.class,
                    new AABB(pPos.getX(), pPos.getY(), pPos.getZ(), pPos.getX(), pPos.getY(), pPos.getZ()).inflate(1.5),
                    c -> true
            );
            if(nearby_vegs.isEmpty()) {
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
                pLevel.playSound(null, pPos, SoundEvents.ITEM_PICKUP, SoundSource.HOSTILE, 1f, 1f);
                pLevel.addFreshEntity(bulb);
            }
        }




    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }
}
