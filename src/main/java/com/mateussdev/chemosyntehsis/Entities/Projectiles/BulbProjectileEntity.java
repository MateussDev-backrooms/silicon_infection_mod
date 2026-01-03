package com.mateussdev.chemosyntehsis.Entities.Projectiles;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

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

        if(this.level() instanceof ServerLevel slvl) {
            if(this.inGround) {
                entityData.set(TRANSFORM_TIMER, entityData.get(TRANSFORM_TIMER)+1);

                if(entityData.get(TRANSFORM_TIMER) >= 20*TRANSFORM_IN_SECONDS) {
                    this.vegetate(slvl);
                }
            }
        }
    }

    public void vegetate(ServerLevel slvl) {
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
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof LivingEntity target) {
            target.hurt(damageSources().arrow(this, this.getOwner()), 4.0F);

            if(target.getHealth() / target.getMaxHealth() < 0.33f) {
                //Only tether if the mob has under 1/3 HP
                if(level() instanceof ServerLevel slvl) {
                    if(StaticSiliconiteMethods.isTetherable(target)) {
                        StaticSiliconiteMethods.tetherMob(slvl, target);
                        target.discard();
                    }
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

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

        if(level().random.nextFloat() < 0.6f) {
            if(level() instanceof ServerLevel slvl) {
                slvl.playSound(null, blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.AMBIENT);
                slvl.sendParticles(
                        ParticleTypes.POOF,
                        this.getX() + 0.5,
                        this.getY() + 0.5,
                        this.getZ() + 0.5,
                        1,
                        0,
                        0,
                        0,
                        0.1
                );
                this.discard();
            }
        }
    }
}
