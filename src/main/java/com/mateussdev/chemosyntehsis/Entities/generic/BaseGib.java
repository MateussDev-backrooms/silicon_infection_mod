package com.mateussdev.chemosyntehsis.Entities.generic;


import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

import static com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods.spawnBloodBurst;

public abstract class BaseGib extends Entity implements GeoEntity {

    protected int age;
    protected int lifetime = 2400;

    protected boolean mustMerge = false;

    public static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(BaseGib.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(BaseGib.class, EntityDataSerializers.FLOAT);

    private static final int MERGE_COUNT = 4;
    private static final int MERGE_RADIUS = 3;

    protected int evolution_t = 0;

    public BaseGib(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }



    private final AnimatableInstanceCache anim_cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return anim_cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    // ===== Core behavior ===== //

    @Override
    protected void defineSynchedData() {
        entityData.define(SCALE, random.nextFloat()/1.5f + 0.5f);
        entityData.define(ROTATION, random.nextFloat()*360f);
    }

    @Override
    public void tick() {
        super.tick();

        //Gravity
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.08, 0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.8));



        if (this.level() instanceof ServerLevel slvl) {
            if(lifetime > 0 && ++age > lifetime) {
                this.discard();
            }

            //Blood particles
            if(getDeltaMovement().length() > 0.01) {
                DustParticleOptions blood = new DustParticleOptions(
                        new Vector3f(0.8f, 0.0f, 0.0f),
                        2.0f
                );

                slvl.sendParticles(
                        blood,
                        position().x + this.getBbWidth()/2,
                        position().y + this.getBbWidth()/2,
                        position().z + this.getBbWidth()/2,
                        1,
                        0.0,
                        0.0,
                        0.0,
                        0.1
                );
            }

            //Merging
            if(tickCount > 100 && !isPassenger()) {
                //Don't turn into flesh pile instantly
                if (mustMerge && evolution_t++ > 20) {
                    mergeIntoFleshPile();
                }
                if(tickCount % 60 == 0) {
                    List<BaseGib> nearby = level().getEntitiesOfClass(
                            BaseGib.class,
                            this.getBoundingBox().inflate(MERGE_RADIUS),
                            c -> c != this && !c.mustMerge
                    );

                    if (nearby.size() + 1 >= MERGE_COUNT) {
                        initiateMerge(nearby);
                    }
                }

            }
        }
    }

    // ===== Gib merging into flesh pile ===== //

    private void initiateMerge(List<BaseGib> others) {
        this.mustMerge = true;

        for (BaseGib c : others) {
            c.mustMerge = true;
        }

        if (level() instanceof ServerLevel slvl) {
            Vec3 center = this.position();

            // Suck the others inward (visual feedback)
            for (BaseGib c : others) {
                Vec3 dir = center.subtract(c.position()).normalize();
                c.setDeltaMovement(dir.scale(0.33f));
            }

            spawnBloodBurst(slvl, blockPosition());

            slvl.scheduleTick(this.blockPosition(), Blocks.AIR, 20);
        }
    }

    private void mergeIntoFleshPile() {
        if (!(level() instanceof ServerLevel slvl)) return;

        List<BaseGib> all = slvl.getEntitiesOfClass(
                BaseGib.class,
                this.getBoundingBox().inflate(MERGE_RADIUS),
                c -> c.mustMerge
        );

        // Only ONE gib does the spawn
        if (all.stream().anyMatch(c -> c.getId() < this.getId())) return;

        // Create flesh pile
        slvl.setBlock(blockPosition(), ModBlocks.FLESH_PILE.get().defaultBlockState(), 3);


        // Consume all chunks
        for (BaseGib c : all) {
            c.discard();
        }
    }

    // ===== Overrides ===== //

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        age = tag.getInt("age");
        lifetime = tag.getInt("lifetime");
        mustMerge = tag.getBoolean("must_merge");
        entityData.set(SCALE, tag.getFloat("scale"));
        entityData.set(ROTATION, tag.getFloat("rotation"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("age", age);
        tag.putInt("lifetime", lifetime);
        tag.putBoolean("must_merge", mustMerge);
        tag.putFloat("scale", entityData.get(SCALE));
        tag.putFloat("rotation", entityData.get(ROTATION));
    }

    @Override
    public boolean isPushable() {
        return true;
    }
}
