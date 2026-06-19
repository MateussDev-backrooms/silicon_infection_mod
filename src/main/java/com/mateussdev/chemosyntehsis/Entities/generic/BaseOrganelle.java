package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Blocks.TendrilBlock;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPThreshold;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DSPType;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.IDspReceptor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static net.minecraft.world.level.block.MultifaceBlock.getFaceProperty;

public abstract class BaseOrganelle extends BaseSiliconite {
    protected BaseOrganelle(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPersistenceRequired();
        this.setNoGravity(true);
        this.setYBodyRot(0);

        this.bulbBreakoffChance = 0f;
        this.discardOnTether = false;
    }



    public static final EntityDataAccessor<Direction> ALIGNMENT = SynchedEntityData.defineId(BaseOrganelle.class, EntityDataSerializers.DIRECTION);
    protected boolean hasSettled = false;
    protected boolean updatedHitbox = false;
    protected boolean failedAttachment = false;

    public int evolution_t = 0;

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

    //Set up the calculation of the hitbox to be static
    private static final Map<Direction, BiFunction<Double, Double, AABB>> HITBOX_ORIENTATION = Map.of(
            Direction.UP, (w, h) -> new AABB(-w/2, 0, -w/2, w - w/2, h, w - w/2),
            Direction.DOWN, (w, h) -> new AABB(-w/2, -h/2, -w/2, w - w/2, h - h/2, w - w/2),
            Direction.EAST, (w, h) -> new AABB(-w/2, 0, -w/2, h - w/2, w, w - w/2),
            Direction.WEST, (w, h) -> new AABB(-h - w/2, 0, -w/2, h - (h - w/2), w, w - w/2),
            Direction.NORTH, (w, h) -> new AABB(-w/2, 0, -w/2, w - w/2, w, h - w/2),
            Direction.SOUTH, (w, h) -> new AABB(-w/2, 0, h - w/2, w - w/2, w, h - (h - w/2))
    );
    protected Direction chosenDir = Direction.UP;

    @Override
    public void tick() {
        super.tick();
        this.yBodyRot = this.yBodyRotO;
        if(this.level() instanceof ServerLevel slvl) {

            if(tickCount % 20 == 0) {
                //Check if attached
                if(slvl.getBlockState(blockPosition().relative(chosenDir.getOpposite())).isAir()) {
                    hasSettled = false;
                    calculateAttachOrientation();
                    if(failedAttachment) {
                        setNoGravity(false);
                    }
                }
            }

            if(shouldSpawnTendrils()) {
                if(tickCount % 80 == 0) {

                    if(!(slvl.getBlockState(blockPosition()).getBlock() instanceof TendrilBlock)) {
                        BlockState attachedBS = slvl.getBlockState(blockPosition().relative(chosenDir.getOpposite()));

                        //Prevent tendrils spawning in the air or breaking blocks
                        if(!attachedBS.isAir()) {
                            BlockState currentBS = slvl.getBlockState(blockPosition());
                            //if block is not too hard - break it
                            float hardness = currentBS.getDestroySpeed(slvl, blockPosition());
                            if (hardness > 0 && hardness < 15f) slvl.destroyBlock(blockPosition(), false);

                            BlockState tendrils = ModBlocks.TENDRILS.get().defaultBlockState();

                            //fix multi-face states
                            for (Direction dir : Direction.values()) {
                                BooleanProperty prop = getFaceProperty(dir);
                                tendrils = tendrils.setValue(prop, dir == chosenDir.getOpposite());
                            }

                            slvl.setBlock(this.blockPosition(), tendrils, 3);
                        }
                    }
                }
            }
        }
    }




    public void calculateAttachOrientation() {
        updatedHitbox = false;
        if(!hasSettled) {
            failedAttachment = false;
            if(this.level() instanceof ServerLevel slvl) {
                this.setYBodyRot(0);
                this.yBodyRotO = 0;


                //Check adjacent blocks
                for(Direction dir : Direction.values()) {
                    BlockPos checkpos = blockPosition().relative(dir);
                    if(slvl.getBlockState(checkpos).isCollisionShapeFullBlock(slvl, checkpos)) {
                        hasSettled = true;
                        setAttachDir(dir.getOpposite());
                        return;
                    }
                }
                failedAttachment = true;
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return this.getType().getHeight()/2f;
    }

    public double getAttachedEyeY() {
        Direction myDir = entityData.get(ALIGNMENT);
        if(myDir == Direction.UP) return this.getEyeY();
        else if(myDir == Direction.DOWN) return this.getY() - this.getEyeHeight();
        else {
            return this.getY() + this.getType().getHeight()/2;
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
        Direction alignment = entityData.get(ALIGNMENT);

        //TEMP keeping this if the static map doesn't work
//        double real_x = w;
//        double real_y = h;
//        double real_z = w;
//
//        double off_x = w/2;
//        double off_y = 0;
//        double off_z = w/2;
//
//        if(entityData.get(ALIGNMENT) == Direction.DOWN) {
//            real_x = w;
//            real_y = h;
//            real_z = w;
//
//            off_x = w/2;
//            off_y = h/2;
//            off_z = w/2;
//        } else if(entityData.get(ALIGNMENT) == Direction.EAST) {
//            real_x = h;
//            real_y = w;
//            real_z = w;
//
//            off_x = w/2;
//            off_y = 0;
//            off_z = w/2;
//        } else if(entityData.get(ALIGNMENT) == Direction.WEST) {
//            real_x = h;
//            real_y = w;
//            real_z = w;
//
//            off_x = h - w/2;
//            off_y = 0;
//            off_z = w/2;
//        } else if(entityData.get(ALIGNMENT) == Direction.SOUTH) {
//            real_x = w;
//            real_y = w;
//            real_z = h;
//
//            off_x = w/2;
//            off_y = 0;
//            off_z = w/2;
//        } else if(entityData.get(ALIGNMENT) == Direction.NORTH) {
//            real_x = w;
//            real_y = w;
//            real_z = h;
//
//            off_x = w/2;
//            off_y = 0;
//            off_z = h - w/2;
//        }
//
//
//
//        return new AABB(0, 0, 0, real_x, real_y, real_z).move(this.getX() - off_x, this.getY() - off_y, this.getZ() - off_z);

        return HITBOX_ORIENTATION.getOrDefault(alignment, HITBOX_ORIENTATION.get(Direction.UP))
                .apply(w, h)
                .move(this.getX(), this.getY(), this.getZ());
    }
    // ===== Organelle properties ===== //
    public boolean shouldSpawnTendrils() { return false; }

    public int tendrilSupportRadius() { return 5; }

    // ===== Organelle overrides ===== //

    @Override
    protected void registerGoals() {
        // Properly override with empty implementation
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }

    @Override
    public boolean isNoGravity() { return !failedAttachment; }

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
    protected void tryCheckInsideBlocks() {
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        //DO NOT TRAVEL
        if(!hasSettled) {
            super.travel(pTravelVector);
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        this.setPos(new Vec3(blockPosition().getX()+0.5f, blockPosition().getY(), blockPosition().getZ()+0.5f));
        this.setYBodyRot(0);
        this.yBodyRotO = 0;

        calculateAttachOrientation();

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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }
}