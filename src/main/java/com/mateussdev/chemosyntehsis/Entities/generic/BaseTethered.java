package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModParticles;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.GlobalWarming.GlobalWarmingCommand;
import com.mateussdev.chemosyntehsis.GlobalWarming.GlobalWarmingData;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class BaseTethered extends BaseSiliconite {
    protected BaseTethered(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected int globalWarmingRate = 64;
    protected int chunk_count = 3;

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

    private int deathTime = 0;
    @Override
    public void tickDeath() {
        if(explodeOnDeath()) {
            ++this.deathTime;


            if (this.deathTime == 40 && !this.level().isClientSide) {
                this.splitIntoChunks(3);
                this.level().broadcastEntityEvent(this, (byte)60);
                this.remove(RemovalReason.KILLED);
            }
        } else {
            super.tickDeath();
        }
    }

    public void turnIntoBiomush() {

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
            spawnBloodBurst();

            slvl.setBlock(blockPosition(), ModBlocks.BIOMUSH.get().defaultBlockState(), 2);

            //Spawn chunks
            for (int i = 0; i < count; i++) {
                ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.5f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                slvl.addFreshEntity(chunkOfFlesh);
            }

        }
    }

    private void spawnBloodBurst() {
        if (!(this.level() instanceof ServerLevel serverLevel)) return;

        DustParticleOptions blood = new DustParticleOptions(
                new Vector3f(0.8f, 0.0f, 0.0f),
                3.0f
        );

        serverLevel.sendParticles(
                blood,
                this.getX(),
                this.getY() + 1.0,
                this.getZ(),
                30,
                0.3,
                0.5,
                0.3,
                0.1
        );
    }
}
