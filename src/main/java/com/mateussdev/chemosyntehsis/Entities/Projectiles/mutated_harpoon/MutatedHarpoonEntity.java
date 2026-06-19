package com.mateussdev.chemosyntehsis.Entities.Projectiles.mutated_harpoon;

import com.mateussdev.chemosyntehsis.Blocks.FleshPileBlock;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Interfaces.IBiomassContainer;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.AbstractHarpoonProjectile;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.AbstractProperHarpoonProjectile;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MutatedHarpoonEntity extends AbstractProperHarpoonProjectile implements GeoAnimatable {


    public MutatedHarpoonEntity(Level level, LivingEntity shooter) {
        super(ModEntities.MUTATED_HARPOON_PROJECTILE.get(), shooter, level);
        this.pickup = Pickup.CREATIVE_ONLY;
        this.setOwner(shooter);
    }

    public MutatedHarpoonEntity(EntityType<MutatedHarpoonEntity> mutatedHarpoonEntityEntityType, Level level) {
        super(mutatedHarpoonEntityEntityType, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    int damageTick;
    @Override
    public void tick() {
        super.tick();

    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (result.getEntity() instanceof Mob mob) {
            mob.hurt(damageSources().arrow(this, this.getOwner()), 4.0F);

//            if (mob.getHealth() / mob.getMaxHealth() < 0.33f) {
//                //Only tether if the mob has under 1/3 HP
//                if (level() instanceof ServerLevel slvl) {
//                    UniversalTethering.tryTetherMob(mob, slvl);
//                }
//            }
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
    protected boolean canHitEntity(Entity p_36743_) {
        return super.canHitEntity(p_36743_) && !(p_36743_ instanceof ChunkOfFlesh) && !(p_36743_ instanceof BaseOrganelle);
    }
}
