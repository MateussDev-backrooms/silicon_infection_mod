package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import com.mateussdev.chemosyntehsis.Blocks.TendrilBlock;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods.spawnBloodBurst;
import static net.minecraft.world.level.block.MultifaceBlock.getFaceProperty;

public class VegetativeBulb extends BaseOrganelle {
    public VegetativeBulb(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        this.setNoGravity(true);
        this.setYBodyRot(0);
    }

    public static final EntityDataAccessor<Vector3f> ALIGNMENT = SynchedEntityData.defineId(VegetativeBulb.class, EntityDataSerializers.VECTOR3);
    private boolean hasSettled = false;

    public int evolution_t = 0;
    public boolean mustEvolve = false;

    private BlockPos tendrilPos = blockPosition();

    //directional stuffs
    private Direction chosenDir = Direction.UP;
    Vec3[] dirs = {
            new Vec3(0, 1, 0), new Vec3(0, -1, 0),
            new Vec3(1, 0, 0), new Vec3(-1, 0, 0),
            new Vec3(0, 0, 1), new Vec3(0, 0, -1),
    };

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
    public boolean isNoGravity() {
        return true;
    }

    public void calculateAttachOrientation() {
        if(!hasSettled) {
            Vec3 origin = this.position().add(0, this.getBbHeight() / 2f, 0);
            if(this.level() instanceof ServerLevel slvl) {




                double dist = 1.2d;
                Vec3 closestNormal = null;
                double shortestDist = Double.MAX_VALUE;
                Vec3 closestPosition = null;

                int i=0;
                for(Vec3 dir : dirs) {
                    i++;
                    BlockHitResult hitResult = slvl.clip(new ClipContext(origin, dir.scale(dist).add(origin), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));

                    if(hitResult.getType() == HitResult.Type.BLOCK) {
                        double raycastDist = origin.distanceTo(hitResult.getLocation());

                        if(raycastDist < shortestDist) {
                            shortestDist = raycastDist;
                            closestNormal = new Vec3(
                                    hitResult.getDirection().getNormal().getX(),
                                    hitResult.getDirection().getNormal().getY(),
                                    hitResult.getDirection().getNormal().getZ()
                            );
                            closestPosition = hitResult.getLocation();
                            chosenDir = Direction.getNearest(closestNormal.x, closestNormal.y, closestNormal.z);
                            tendrilPos = blockPosition().offset(chosenDir.getNormal().multiply(0));
                        }
                    }
                }

                if(closestNormal != null) {
                    setAttachNormal(closestNormal);
                    this.moveTo(closestPosition);
                    hasSettled = true;
                }

            }
        }
    }

    public Map<Vector3f, Vec3> directionRotHashMap =
            new HashMap<>();

    @Override
    public void tick() {
        super.tick();
        this.setYBodyRot(0);
        if(this.level() instanceof ServerLevel slvl) {
            if(tickCount % 40 == 0) {
                if(slvl.getBlockState(blockPosition()).getBlock() instanceof TendrilBlock) {
                    //TODO do shit every tick
                } else {
                    BlockState tendrils = ModBlocks.TENDRILS.get().defaultBlockState();

                    //fix multi-face states
                    for (Direction dir : Direction.values()) {
                        BooleanProperty prop = getFaceProperty(dir);
                        tendrils = tendrils.setValue(prop, dir == chosenDir.getOpposite());
                    }

                    slvl.setBlock(tendrilPos, tendrils, 3);
                }
            }
        }

        if (mustEvolve && evolution_t++ > 20) {
            mergeIntoCluster();
        }

        if(tickCount % 20 == 0) {
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

    private void mergeIntoCluster() {
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

    public void setAttachNormal(Vec3 normal) {
        entityData.set(ALIGNMENT, normal.normalize().toVector3f());
    }

    public Vector3f getAttachNormal() {
        return entityData.get(ALIGNMENT);
    }

    public Vec3 getNormalRot() {
        return directionRotHashMap.get(entityData.get(ALIGNMENT));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        calculateAttachOrientation();

        directionRotHashMap.put(new Vector3f(1.0f, 0.0f, 0.0f), new Vec3(0, 0, 90));
        directionRotHashMap.put(new Vector3f(-1.0f, 0.0f, 0.0f), new Vec3(0, 0, -90));
        directionRotHashMap.put(new Vector3f(0.0f, 1.0f, 0.0f), new Vec3(0, 0, 0));
        directionRotHashMap.put(new Vector3f(0.0f, -1.0f, 0.0f), new Vec3(0, 0, 180));
        directionRotHashMap.put(new Vector3f(0.0f, 0.0f, 1.0f), new Vec3(-90, 0, 0));
        directionRotHashMap.put(new Vector3f(0.0f, 0.0f, -1.0f), new Vec3(90, 0, 0));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(ALIGNMENT, new Vector3f(0, 1, 0));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putFloat("alignment_x", entityData.get(ALIGNMENT).x);
        tag.putFloat("alignment_y", entityData.get(ALIGNMENT).y);
        tag.putFloat("alignment_z", entityData.get(ALIGNMENT).z);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        entityData.set(ALIGNMENT, new Vector3f(
                tag.getFloat("alignment_x"),
                tag.getFloat("alignment_y"),
                tag.getFloat("alignment_z")
        ));

        chosenDir = Direction.getNearest(tag.getFloat("alignment_x"), tag.getFloat("alignment_y"), tag.getFloat("alignment_z"));
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
