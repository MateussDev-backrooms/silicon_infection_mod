package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Mixin.MobAccessor;
import com.mateussdev.chemosyntehsis.Particles.SiliconiteParticles;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class MutationSwimming extends Mutation {

    private static final int WATER_DASH_COOLDOWN = 20;

    public MutationSwimming(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationSwimming> model = new MutationSwimming_Model();
    private final GeoRenderer<MutationSwimming> renderer = new MutationSwimming_Renderer(model);

    //AI revert vars//
    private MoveControl oldMoveControl;
    private LookControl oldLookControl;
    private PathNavigation oldNavigation;

    private static final AttributeModifier SWIMMING_BONUS_MODIFIER = new AttributeModifier(
            "mutation_swimming_bonus", 6.5d, AttributeModifier.Operation.ADDITION
    );



    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "head"; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationSwimming_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    private int waterDashT = 0;
    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            mob.setAirSupply(mob.getMaxAirSupply());

            if(mob.isInWater()) {
                mob.setSwimming(true);
                if(mob.getTarget() != null && --waterDashT <= 0) {
                    //Find vector and go towards mob
                    Vec3 dir = mob.getTarget().position().subtract(mob.position()).normalize();

                    addParticlesAroundSelf(slvl, mob.blockPosition());
                    slvl.playSound(null, mob.blockPosition(), SoundEvents.PLAYER_SWIM, SoundSource.HOSTILE);

                    mob.setDeltaMovement(dir.scale(1f));
                    waterDashT = WATER_DASH_COOLDOWN;
                }
            } else {
                mob.setSwimming(false);
            }
        }
    }

    @Override
    public void onInit(Mob mob) {
        oldMoveControl = ((MobAccessor) mob).getMoveControl();
        oldLookControl = ((MobAccessor) mob).getLookControl();
        oldNavigation = ((MobAccessor) mob).getNavigation();

        mob.getAttribute(Attributes.MOVEMENT_SPEED).addPermanentModifier(SWIMMING_BONUS_MODIFIER);

        ((MobAccessor) mob).setMoveControl(new SmoothSwimmingMoveControl(mob, 85, 10, 1.2F, 0.5F, true));
        ((MobAccessor) mob).setLookControl(new SmoothSwimmingLookControl(mob, 10));
        ((MobAccessor) mob).setNavigation(new WaterBoundPathNavigation(mob, mob.level()));

        mob.goalSelector.addGoal(-1, new TryFindWaterGoal((PathfinderMob) mob));
//        mob.goalSelector.addGoal(-1, new SweepingSwimAttackGoal(mob, 1.4f, true));
        mob.goalSelector.addGoal(1, new RandomSwimmingGoal((PathfinderMob) mob, 1.0D, 10));
        mob.goalSelector.removeGoal(new WaterAvoidingRandomStrollGoal((PathfinderMob) mob, 1.0f));

    }

    @Override
    public void onRemove(Mob mob) {
        ((MobAccessor) mob).setMoveControl(oldMoveControl);
        ((MobAccessor) mob).setLookControl(oldLookControl);
        ((MobAccessor) mob).setNavigation(oldNavigation);

        mob.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(SWIMMING_BONUS_MODIFIER);
    }

    private void addParticlesAroundSelf(ServerLevel slvl, BlockPos blockPos) {
        slvl.sendParticles(
                ParticleTypes.BUBBLE,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                15,
                0.3,
                0.5,
                0.3,
                0.1
        );

    }

    @Override
    public boolean canMutateMob(Mob mob) {

        //Generic mob mask
        List<EntityType<?>> disabledMobs = List.of(
        );

        for(EntityType<?> type : disabledMobs) {
            if(mob.getType() == type) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mutation_swimming", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("active"));
        }));
    }

    @Override
    public Mutation copy(Random rng) {
        return new MutationSwimming(this.getTypeId(), rng.nextInt(Integer.MAX_VALUE));
    }
}
