package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class BaseAmalgamation extends BaseOrganelle{
    protected BaseAmalgamation(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private int basePlacementT;
    private int blocksPlaced = 0;
    private boolean hasPlacedVeinBlock = false;

    @Override
    public void tick() {
        super.tick();

        if(level() instanceof ServerLevel slvl) {
            if (basePlacementT < 60) {

                // Place one block every 5 ticks
                if (basePlacementT % 5 == 0) {
                    // Determine the build direction (Opposite to where we are attached)
                    Direction attachDir = entityData.get(ALIGNMENT);
                    Direction buildDir = attachDir.getOpposite();

                    // The center of our construction platform
                    BlockPos centerOrigin = this.blockPosition().relative(buildDir);

                    // Define the 3x3 area based on the alignment
                    // We need different loops for vertical vs horizontal surfaces
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
                            StaticSiliconiteMethods.spawnBloodHit(slvl, pos.getCenter());
                            blocksPlaced++;
                            break;
                        }
                        count++;
                    }

                    for(BlockPos pos : BlockPos.randomBetweenClosed(random,
                            3, Math.round((float) getBoundingBox().minX), Math.round((float) getBoundingBox().minY), Math.round((float) getBoundingBox().minZ),
                            Math.round((float) getBoundingBox().maxX), Math.round((float) getBoundingBox().maxY), Math.round((float) getBoundingBox().maxZ)
                    )) {
                        BlockState state = slvl.getBlockState(pos);

                        if(state.getDestroySpeed(slvl, pos) < 20f) {
                            slvl.destroyBlock(pos, false);
                        }
                    }
                }
                basePlacementT++;
            }
        }
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
