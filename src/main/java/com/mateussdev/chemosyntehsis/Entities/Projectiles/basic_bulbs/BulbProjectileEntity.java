package com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModSounds;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseGib;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.Vegetated.veg_bulb.VegetativeBulb;
import com.mateussdev.chemosyntehsis.Util.GlobalMobCap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.util.List;

import static com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods.spawnBloodBurst;

public class BulbProjectileEntity extends AbstractArrow implements GeoAnimatable {
    public BulbProjectileEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.pickup = Pickup.CREATIVE_ONLY;
    }

    public BulbProjectileEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BULB_PROJECTILE.get(), shooter, level);
        this.pickup = Pickup.CREATIVE_ONLY;
    }

    public static final EntityDataAccessor<Integer> TRANSFORM_TIMER = SynchedEntityData.defineId(BulbProjectileEntity.class, EntityDataSerializers.INT);
    public final int TRANSFORM_IN_SECONDS = 30;

    public boolean mustMerge = false;
    public int evolution_t = 0;

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.inGround && this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }

        if (this.level() instanceof ServerLevel slvl) {
            if (this.inGround) {
                entityData.set(TRANSFORM_TIMER, entityData.get(TRANSFORM_TIMER) + 1);

                if (entityData.get(TRANSFORM_TIMER) >= 20 * TRANSFORM_IN_SECONDS) {
                    this.vegetate(slvl);
                }

                if (mustMerge && evolution_t++ > 20) {
                    mergeIntoBulb();
                }

                if (tickCount % 60 == 0) {
                    if (!this.mustMerge) {
                        List<BulbProjectileEntity> nearby = level().getEntitiesOfClass(
                                BulbProjectileEntity.class,
                                this.getBoundingBox().inflate(1.2D),
                                c -> c != this && !c.mustMerge
                        );

                        if (nearby.size() + 1 >= 5) {
                            initiateMerge(nearby);
                        }
                    }
                }
            }
        }
    }

    private void mergeIntoBulb() {
        if (!(level() instanceof ServerLevel slvl)) return;

        List<BulbProjectileEntity> all = slvl.getEntitiesOfClass(
                BulbProjectileEntity.class,
                this.getBoundingBox().inflate(1.2D),
                c -> c.mustMerge
        );

        if (all.stream().anyMatch(c -> c.getId() < this.getId())) return;

        // Effects
        spawnBloodBurst(slvl, this.blockPosition());

        // Spawn Cluster
        VegetativeBulb vegetativeBulb = ModEntities.VEG_BULB.get().create(slvl);

        vegetativeBulb.moveTo(this.getX(), this.getY(), this.getZ());

        slvl.addFreshEntity(vegetativeBulb);

        for (BulbProjectileEntity c : all) {
            c.discard();
        }
    }

    private void initiateMerge(List<BulbProjectileEntity> others) {
        this.mustMerge = true;

        for (BulbProjectileEntity c : others) {
            c.mustMerge = true;
        }

        if (level() instanceof ServerLevel slvl) {
            spawnBloodBurst(slvl, blockPosition());

            slvl.scheduleTick(this.blockPosition(), Blocks.AIR, 20);
        }
    }

    public void vegetate(ServerLevel slvl) {
        if(GlobalMobCap.canSpawnUnique(slvl, ModEntities.VEG_BULB.get(), blockPosition(), GlobalMobCap.BULB_CAP, 128)) {
            VegetativeBulb bulb = ModEntities.VEG_BULB.get().create(slvl);
            bulb.moveTo(this.position());
            slvl.sendParticles(
                    ParticleTypes.EXPLOSION,
                    this.getX() + 0.5,
                    this.getY() + 0.5,
                    this.getZ() + 0.5,
                    1,
                    0,
                    0,
                    0,
                    0.1
            );
            slvl.addFreshEntity(bulb);
            this.discard();
        } else {
            slvl.playSound(null, blockPosition(), ModSounds.BULB_SHATTER.get(), SoundSource.AMBIENT, 0.3f, 1f);
            StaticSiliconiteMethods.spawnBloodHit(slvl, this.position());
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().invulnerableTime = 3;
        if (result.getEntity() instanceof LivingEntity livingEntity) {
            if (livingEntity.getHealth() / livingEntity.getMaxHealth() < 0.33f) {
                //Only tether if the mob has under 1/3 HP
                if (level() instanceof ServerLevel slvl) {
                    if (StaticSiliconiteMethods.isTetherable(livingEntity)) {
                        StaticSiliconiteMethods.tetherMob(slvl, livingEntity);
                        livingEntity.discard();
                    }
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        return p_36743_ != getOwner() && !(p_36743_ instanceof AbstractArrow) && !(p_36743_ instanceof BaseOrganelle) && !(p_36743_ instanceof BaseGib);
    }

    private final AnimatableInstanceCache anim_cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return anim_cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("transform_timer", entityData.get(TRANSFORM_TIMER));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        entityData.set(TRANSFORM_TIMER, pCompound.getInt("transform_timer"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TRANSFORM_TIMER, 0);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);

        if (level() instanceof ServerLevel slvl) {
            if (level().random.nextFloat() < 0.6f || !GlobalMobCap.canSpawnUnique(slvl, ModEntities.BULB_PROJECTILE.get(), blockPosition(), 128, 128)) {
                slvl.playSound(null, blockPosition(), ModSounds.BULB_SHATTER.get(), SoundSource.AMBIENT, 0.3f, 1f);
                StaticSiliconiteMethods.spawnBloodHit(slvl, this.position());
                this.discard();
            }
        }
    }
}
