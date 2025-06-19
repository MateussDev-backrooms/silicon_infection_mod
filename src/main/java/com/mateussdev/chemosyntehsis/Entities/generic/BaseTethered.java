package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.GlobalWarming.GlobalWarmingCommand;
import com.mateussdev.chemosyntehsis.GlobalWarming.GlobalWarmingData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BaseTethered extends BaseSiliconite {
    protected BaseTethered(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected int globalWarmingRate = 64;
    protected int chunk_count = 3;

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
        ++this.deathTime;

        // This is the magical moment of expiration (20 ticks = 1 second)
        if (this.deathTime == 20 && !this.level().isClientSide) {
            this.splitIntoChunks(5);
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
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

            for (int i = 0; i < count; i++) {
                ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                chunkOfFlesh.moveTo(blockPosition().getX(), blockPosition().getY(), blockPosition().getZ());
                chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.2f, 0.5f, (slvl.random.nextDouble()*2f - 1f)*0.2f));
                slvl.addFreshEntity(chunkOfFlesh);
            }
        }
    }
}
