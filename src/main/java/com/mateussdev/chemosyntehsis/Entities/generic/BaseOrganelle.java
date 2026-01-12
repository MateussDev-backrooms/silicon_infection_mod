package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Blocks.TendrilBlock;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.world.level.block.MultifaceBlock.getFaceProperty;

public class BaseOrganelle extends BaseSiliconite {
    protected BaseOrganelle(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
        this.setNoGravity(true);
        this.setYBodyRot(0);
    }

    public static final EntityDataAccessor<Direction> ALIGNMENT = SynchedEntityData.defineId(BaseOrganelle.class, EntityDataSerializers.DIRECTION);
    protected boolean hasSettled = false;

    public int evolution_t = 0;
    public boolean mustEvolve = false;

    protected BlockPos tendrilPos = blockPosition();

    // ===== Gluing to sides of blocks ===== //

    private static final Map<Direction, Vec3> ROTATIONS = Map.of(
            Direction.UP,    new Vec3(0, 0, 0),
            Direction.DOWN,  new Vec3(0, 0, 180),
            Direction.NORTH, new Vec3(90, 0, 0),
            Direction.SOUTH, new Vec3(-90, 0, 0),
            Direction.EAST,  new Vec3(0, 0, 90),
            Direction.WEST,  new Vec3(0, 0, -90)
    );

    protected boolean shouldSpawnTendrils() { return false; }

    @Override
    public void tick() {
        super.tick();
        this.setYBodyRot(0);
        if(this.level() instanceof ServerLevel slvl) {

            if(tickCount % 80 == 0) {
                this.reapplyPosition();
                this.refreshDimensions();
                if(slvl.getBlockState(blockPosition()).getBlock() instanceof TendrilBlock tendrilBlock) {
                    tendrilBlock.randomTick(slvl.getBlockState(blockPosition()), slvl, blockPosition(), random);
                } else {
                    BlockState tendrils = ModBlocks.TENDRILS.get().defaultBlockState();

                    //fix multi-face states
                    for (Direction dir : Direction.values()) {
                        BooleanProperty prop = getFaceProperty(dir);
                        tendrils = tendrils.setValue(prop, dir == chosenDir.getOpposite());
                    }

                    slvl.setBlock(this.blockPosition(), tendrils, 3);
                }
            }

//            if(tickCount % 500 == 0) {
//                hasSettled = false;
//                calculateAttachOrientation();
//            }
        }
    }

    //directional stuffs
    protected Direction chosenDir = Direction.UP;
    protected Vec3[] dirs = {
            new Vec3(0, 1, 0), new Vec3(0, -1, 0),
            new Vec3(1, 0, 0), new Vec3(-1, 0, 0),
            new Vec3(0, 0, 1), new Vec3(0, 0, -1),
    };


    public void calculateAttachOrientation() {
        if(!hasSettled) {
            Vec3 origin = this.position().add(0, this.getBbHeight() / 2f, 0);
            if(this.level() instanceof ServerLevel slvl) {

                double dist = 1.2d;
                Vec3 closestNormal = null;
                double shortestDist = Double.MAX_VALUE;
                Vec3 closestPosition = null;

                int i=0;

                //Check adjacent blocks
                for(Direction dir : Direction.values()) {
                    BlockPos checkpos = blockPosition().relative(dir);
                    if(slvl.getBlockState(checkpos).isCollisionShapeFullBlock(slvl, checkpos)) {
                        hasSettled = true;
                        setAttachDir(dir.getOpposite());
                        return;
                    }
                }

                //Raycast
//                for(Vec3 dir : dirs) {
//                    i++;
//                    BlockHitResult hitResult = slvl.clip(new ClipContext(origin, dir.scale(dist).add(origin), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
//
//                    if(hitResult.getType() == HitResult.Type.BLOCK) {
//                        double raycastDist = origin.distanceTo(hitResult.getLocation());
//
//                        if(raycastDist < shortestDist) {
//                            shortestDist = raycastDist;
//                            closestNormal = new Vec3(
//                                    hitResult.getDirection().getNormal().getX(),
//                                    hitResult.getDirection().getNormal().getY(),
//                                    hitResult.getDirection().getNormal().getZ()
//                            );
//                            closestPosition = hitResult.getLocation();
//                            chosenDir = Direction.getNearest(closestNormal.x, closestNormal.y, closestNormal.z);
//                            tendrilPos = blockPosition().relative(chosenDir.getOpposite());
//                        }
//                    }
//                }

                if(closestNormal != null) {
                    setAttachDir(chosenDir);
                    this.moveTo(closestPosition);
                    hasSettled = true;
                }

            }
        }
    }

    public void setAttachDir(Direction dir) {
        this.chosenDir = dir;
        this.entityData.set(ALIGNMENT, dir);
        this.refreshDimensions();
    }

    public Direction getAttachDir() {
        return entityData.get(ALIGNMENT);
    }

    public Vec3 getNormalRot() {
        return ROTATIONS.get(entityData.get(ALIGNMENT));
    }

    @Override
    protected AABB makeBoundingBox() {
        double w = this.getType().getWidth();
        double h = this.getType().getHeight();

        double real_x = w;
        double real_y = h;
        double real_z = w;

        double off_x = w/2;
        double off_y = 0;
        double off_z = w/2;

        if(entityData.get(ALIGNMENT) == Direction.DOWN) {
            real_x = w;
            real_y = h;
            real_z = w;

            off_x = w/2;
            off_y = h/2;
            off_z = w/2;
        } else if(entityData.get(ALIGNMENT) == Direction.EAST) {
            real_x = h;
            real_y = w;
            real_z = w;

            off_x = w/2;
            off_y = 0;
            off_z = w/2;
        } else if(entityData.get(ALIGNMENT) == Direction.WEST) {
            real_x = h;
            real_y = w;
            real_z = w;

            off_x = h - w/2;
            off_y = 0;
            off_z = w/2;
        } else if(entityData.get(ALIGNMENT) == Direction.SOUTH) {
            real_x = w;
            real_y = w;
            real_z = h;

            off_x = w/2;
            off_y = 0;
            off_z = w/2;
        } else if(entityData.get(ALIGNMENT) == Direction.NORTH) {
            real_x = w;
            real_y = w;
            real_z = h;

            off_x = w/2;
            off_y = 0;
            off_z = h - w/2;
        }



        return new AABB(0, 0, 0, real_x, real_y, real_z).move(this.getX() - off_x, this.getY() - off_y, this.getZ() - off_z);


    }

    //Change hitbox

    // ===== Organelle overrides ===== //

    @Override
    protected void registerGoals() {
        // Properly override with empty implementation
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isNoGravity() { return true; }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    public void push(double pX, double pY, double pZ) {
        // Completely immobile
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
        // No knockback for stationary entities
    }

    @Override
    protected boolean isBrave() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        this.setPos(new Vec3(blockPosition().getX()+0.5f, blockPosition().getY(), blockPosition().getZ()+0.5f));

        calculateAttachOrientation();

    }

    @Override
    protected boolean destructiveTether() {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ALIGNMENT, Direction.UP);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("alignment_dir", chosenDir.get3DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        Direction d = Direction.from3DDataValue(tag.getInt("alignment_dir"));
        this.chosenDir = d;
        this.entityData.set(ALIGNMENT, d);
    }
}