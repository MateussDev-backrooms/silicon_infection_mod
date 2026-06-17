package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPThreshold;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPType;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.IDspReceptor;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class BaseAmalgamation extends BaseOrganelle implements IDspReceptor {
    protected BaseAmalgamation(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private int basePlacementT;
    private int regenT;
    private int blocksPlaced = 0;
    private boolean hasPlacedVeinBlock = false;

    private int damagedT = 0;

    private static final int DAMAGE_DSP_TIMER_DURATION = 100; //5 seconds
    private static final float DAMAGE_DSP_AMOUNT = 5f;

    protected final EnumMap<DSPType, Float> internalBuffer = new EnumMap<>(DSPType.class);
    protected final List<DSPThreshold> thresholds = new ArrayList<>();

    @Override
    public EnumMap<DSPType, Float> getInternalBuffer() { return internalBuffer; }

    @Override
    public List<DSPThreshold> getThresholds() { return thresholds; }

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            if (basePlacementT < 60) {

                //Platform creation
                if (basePlacementT % 5 == 0) {
                    Direction attachDir = entityData.get(ALIGNMENT);
                    Direction buildDir = attachDir.getOpposite();

                    //The centre of our construction platform
                    BlockPos centerOrigin = this.blockPosition().relative(buildDir);


                    BlockPos p1, p2;

                    if (attachDir.getAxis() == Direction.Axis.Y) {
                        p1 = centerOrigin.offset(-1, 0, -1);
                        p2 = centerOrigin.offset(1, 0, 1);
                    } else if (attachDir == Direction.NORTH || attachDir == Direction.SOUTH) {
                        p1 = centerOrigin.offset(-1, -1, 0);
                        p2 = centerOrigin.offset(1, 1, 0);
                    } else {
                        p1 = centerOrigin.offset(0, -1, -1);
                        p2 = centerOrigin.offset(0, 1, 1);
                    }

                    int count = 0;
                    for (BlockPos pos : BlockPos.betweenClosed(p1, p2)) {

                        if (count == blocksPlaced) {
                            if(!hasPlacedVeinBlock && random.nextFloat() < 0.4) {
                                slvl.setBlock(pos, ModBlocks.VEIN_BLOCK.get().defaultBlockState(), 3);
                                hasPlacedVeinBlock = true;
                            } else {
                                slvl.setBlock(pos, ModBlocks.AMALGAMATED_FLESH_BLOCK.get().defaultBlockState(), 3);
                            }
                            SiliconiteParticles.spawnBloodHit(slvl, pos.getCenter());
                            blocksPlaced++;
                            break;
                        }
                        count++;
                    }

                    AABB removeBox = getBoundingBox().move(attachDir.getNormal().getX(), attachDir.getNormal().getY(), attachDir.getNormal().getZ());

                    for(BlockPos pos : BlockPos.randomBetweenClosed(random,
                            3, Math.round((float) removeBox.minX), Math.round((float) removeBox.minY), Math.round((float) removeBox.minZ),
                            Math.round((float) removeBox.maxX), Math.round((float) removeBox.maxY), Math.round((float) removeBox.maxZ)
                    )) {
                        BlockState state = slvl.getBlockState(pos);

                        if(state.getDestroySpeed(slvl, pos) < 20f) {
                            slvl.destroyBlock(pos, false);
                        }
                    }
                }
                basePlacementT++;
            }

            //TODO: Only regen if there are nearby healers
            if(regenT<240) regenT++;
            else {
                heal(1f);
                regenT=0;
            }

            if(damagedT <= 0) {
                //Cannot absorb when damaged
                absorbDSP(this);
            }

            //When damaged release damaged dsp
            if(damagedT > 0) {
                emitDSP(DSPType.D_D_DAMAGEDIRECTIVE, DAMAGE_DSP_AMOUNT, this);
                damagedT--;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        regenT = 0; //Do not regen when actively hurt

        //Release onDamaged directives
        damagedT = DAMAGE_DSP_TIMER_DURATION;
        return super.hurt(pSource, pAmount);
    }

    @Override
    public GeoBone[] getBulbsArray(GeoModel<?> model) {
        return new GeoBone[0];
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        basePlacementT = tag.getInt("base_placement_timer");
        blocksPlaced = tag.getInt("blocks_placed");
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("base_placement_timer", basePlacementT);
        tag.putInt("blocks_placed", blocksPlaced);
    }
}
