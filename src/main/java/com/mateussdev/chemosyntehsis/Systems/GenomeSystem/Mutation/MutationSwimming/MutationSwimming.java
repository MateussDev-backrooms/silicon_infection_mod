package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Mixin.MobAccessor;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Dolphin;
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

    public MutationSwimming(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationSwimming> model = new MutationSwimming_Model();
    private final GeoRenderer<MutationSwimming> renderer = new MutationSwimming_Renderer(model);

    //AI revert vars//
    private MoveControl oldMoveControl;
    private LookControl oldLookControl;
    private PathNavigation oldNavigation;

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "head"; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationSwimming_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            mob.setAirSupply(mob.getMaxAirSupply());
        }
    }

    @Override
    public void onInit(Mob mob) {
        oldMoveControl = ((MobAccessor) mob).getMoveControl();
        oldLookControl = ((MobAccessor) mob).getLookControl();
        oldNavigation = ((MobAccessor) mob).getNavigation();

        ((MobAccessor) mob).setMoveControl(new SmoothSwimmingMoveControl(mob, 85, 10, 1.2F, 0.1F, true));
        ((MobAccessor) mob).setLookControl(new SmoothSwimmingLookControl(mob, 10));
        ((MobAccessor) mob).setNavigation(new WaterBoundPathNavigation(mob, mob.level()));

        mob.goalSelector.addGoal(2, new RandomSwimmingGoal((PathfinderMob) mob, 1.0D, 10));
        mob.goalSelector.removeGoal(new WaterAvoidingRandomStrollGoal((PathfinderMob) mob, 1.0f));

    }

    @Override
    public void onRemove(Mob mob) {
        ((MobAccessor) mob).setMoveControl(oldMoveControl);
        ((MobAccessor) mob).setLookControl(oldLookControl);
        ((MobAccessor) mob).setNavigation(oldNavigation);
    }

    private void addParticlesAroundSelf(ParticleOptions pParticleOption, Mob mob) {
        for(int i = 0; i < 7; ++i) {
            double d0 = mob.getRandom().nextGaussian() * 0.01D;
            double d1 = mob.getRandom().nextGaussian() * 0.01D;
            double d2 = mob.getRandom().nextGaussian() * 0.01D;
            mob.level().addParticle(pParticleOption, mob.getRandomX(1.0D), mob.getRandomY() + 0.2D, mob.getRandomZ(1.0D), d0, d1, d2);
        }

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
