package com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon;

import com.mateussdev.chemosyntehsis.Blocks.FleshPileBlock;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.AbstractHarpoonProjectile;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.Interfaces.IBiomassContainer;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BulbHarpoonEntity extends AbstractHarpoonProjectile implements GeoAnimatable {


    public BulbHarpoonEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel, false, false, false);
        this.pickup = Pickup.DISALLOWED;
    }
    public BulbHarpoonEntity(Level level, LivingEntity shooter) {
        super(ModEntities.BULB_HARPOON_PROJECTILE.get(), level, true, true, false);
        this.pickup = Pickup.CREATIVE_ONLY;
        this.setOwner(shooter);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    int damageTick;
    @Override
    public void tick() {
        super.tick();

        //Biomass collection
        if(this.level() != null && this.level() instanceof ServerLevel slvl) {
            if(getTarget() instanceof LivingEntity LE && LE.isAlive()) {
                if(getOwner() instanceof IBiomassContainer biomassContainer) {
                    if (++damageTick % 15 == 0) {
                        LE.hurt(damageSources().starve(), 1f);
                        biomassContainer.addBiomass(1);
                    }
                }
            }
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
    protected void attachedToBlockTick() {
        if(tickCount % 10 == 0) {
            if(attachedBlockPos == null) {
                super.attachedToBlockTick();
                return;
            }

            BlockState state = level().getBlockState(attachedBlockPos);
            if(state.getBlock() instanceof FleshPileBlock flesh) {
                level().setBlockAndUpdate(attachedBlockPos, state.setValue(FleshPileBlock.BIOMUSHIFICATION, Mth.clamp(state.getValue(FleshPileBlock.BIOMUSHIFICATION) + 1, 0, 10)));
                if(getOwner() instanceof IBiomassContainer container) {
                    container.addBiomass(1);
                }
            } else {
                super.attachedToBlockTick();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_) && !(p_36743_ instanceof ChunkOfFlesh) && !(p_36743_ instanceof BaseOrganelle);
    }
}
