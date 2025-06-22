package com.mateussdev.chemosyntehsis.Entities.Projectiles;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import mod.azure.azurelib.core.animatable.GeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.math.functions.classic.Pi;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

    private final AnimatableInstanceCache anim_cache = AzureLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return anim_cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
