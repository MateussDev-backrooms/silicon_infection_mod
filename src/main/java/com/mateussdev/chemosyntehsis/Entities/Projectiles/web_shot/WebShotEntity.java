package com.mateussdev.chemosyntehsis.Entities.Projectiles.web_shot;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Core.ModSounds;
import com.mateussdev.chemosyntehsis.Entities.Vegetated.veg_bulb.VegetativeBulb;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseGib;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.MobCapSystem.GlobalMobCap;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class WebShotEntity extends AbstractArrow implements GeoAnimatable {

    public WebShotEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public WebShotEntity(Level level, LivingEntity shooter) {
        super(ModEntities.WEB_SHOT.get(), level);
        this.setOwner(shooter);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.inGround && this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if(!(this.level() instanceof ServerLevel slvl)) return;
        //Place coweb
        if(slvl.isEmptyBlock(result.getEntity().blockPosition())) {
            slvl.setBlock(result.getEntity().blockPosition(), Blocks.COBWEB.defaultBlockState(), 3);
            slvl.playSound(null, result.getEntity().blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.HOSTILE);
        }
        this.discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        if(p_36743_ instanceof Mob mob) {
            if(UniversalTethering.isTethered(mob)) return false;
        }
        return p_36743_ != getOwner() && !(p_36743_ instanceof AbstractArrow) && !(p_36743_ instanceof BaseOrganelle) && !(p_36743_ instanceof BaseGib);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    public boolean isNoGravity() {
        return true;
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
    protected void onHitBlock(BlockHitResult pResult) {
        if(!(this.level() instanceof ServerLevel slvl)) return;
        //Place coweb

        if(slvl.isEmptyBlock(pResult.getBlockPos())) {
            slvl.setBlock(pResult.getBlockPos().relative(pResult.getDirection()), Blocks.COBWEB.defaultBlockState(), 3);
            slvl.playSound(null, pResult.getBlockPos(), SoundEvents.WOOL_PLACE, SoundSource.HOSTILE);
        }
        this.discard();
    }
}
