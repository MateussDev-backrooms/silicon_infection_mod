package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller;
import com.mateussdev.chemosyntehsis.GlobalWarming.GlobalWarmingData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

public class BaseTethered extends BaseSiliconite {
    protected BaseTethered(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected int globalWarmingRate = 64;
    protected int chunk_count = 5;

    protected boolean explodeOnDeath() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Movement anim controller
        controllers.add(new AnimationController<>(this, "movement", 5, event ->
        {
            //death anim
            if(this.isDeadOrDying()) {
                return event.setAndContinue(RawAnimation.begin().then("death", Animation.LoopType.PLAY_ONCE));
            }

            return event.setAndContinue(
                    // If moving, play the walking animation
                    event.isMoving() ? RawAnimation.begin().thenLoop("walk"):
                            // If not moving, play the idle animation
                            RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    protected boolean destructiveTether() {
        return false;
    }

    @Override
    protected float getTetherChance() {
        return 0.6f;
    }

    @Override
    protected boolean isBrave() {
        return true;
    }

    private int t = 0;
    @Override
    public void tick() {
        t++;
        if(t%globalWarmingRate==0) {
            if(level() instanceof ServerLevel slvl) {
                GlobalWarmingData data = GlobalWarmingData.get(slvl);
                data.addPoints(0.1f);
            }
        }

        super.tick();
    }

    protected int deathTime = 0;
    @Override
    public void tickDeath() {
        if(explodeOnDeath()) {
            ++this.deathTime;

            if(this.level() instanceof ServerLevel slvl) {
                if (this.deathTime == 40) {
                    this.splitIntoChunks(5);
                    this.releaseGasIntoAtmosphere(slvl, 1f);
                    this.level().broadcastEntityEvent(this, (byte)60);
                    this.remove(RemovalReason.KILLED);
                }
            }

        } else {
            super.tickDeath();
        }
    }

    public void turnIntoBiomush() {
        if(level() instanceof ServerLevel slvl) {
            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

            slvl.setBlock(blockPosition(), ModBlocks.BIOMUSH.get().defaultBlockState(), 2);
        }
    }

    public void splitIntoChunks(int count) {
        if(level() instanceof ServerLevel slvl) {
            slvl.playSound(
                    null,
                    blockPosition(),
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, blockPosition());

            //Spawn chunks
            for (int i = 0; i < count; i++) {
                if(slvl.random.nextFloat() < 0.33f) {
                    ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                    chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.8f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(chunkOfFlesh);
                } else {
                    GibFlesh gib = ModEntities.GIB_FLESH.get().create(slvl);
                    gib.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                    gib.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.5f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(gib);
                }
            }

        }
    }

    @Override
    protected int evolvesAtMetabolism() {
        return 100;
    }
}
