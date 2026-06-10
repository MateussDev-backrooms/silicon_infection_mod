package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.lang.reflect.Field;

public class MutationFlight extends Mutation {

    public MutationFlight(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationFlight> model = new MutationFlight_Model();
    private final GeoRenderer<MutationFlight> renderer = new MutationFlight_Renderer(model);

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "body"; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationFlight_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            //Float upward when on ground
            float minFlyDist = mob.getTarget() == null ? 1f : mob.getTarget().getBbHeight();
            BlockHitResult raycastDown = slvl.clip(new ClipContext(mob.position(), mob.position().add(0, -minFlyDist, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
            if(raycastDown.getType() != HitResult.Type.MISS) {
                mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.05, 0));
            }

            //Float down when hitting ceiling
            BlockHitResult raycastUp = slvl.clip(new ClipContext(mob.position(), mob.position().add(0, 1, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
            if(raycastUp.getType() != HitResult.Type.MISS) {
                mob.setDeltaMovement(mob.getDeltaMovement().add(0, -0.05, 0));
            }
        }
    }

    @Override
    public void onInit(Mob mob) {
        mob.setNoGravity(true);
        //Add floating AI functionality
        try {
            Field navField = Mob.class.getDeclaredField("navigation");
            navField.setAccessible(true);
            navField.set(mob, new FlyingPathNavigation(mob, mob.level()));

            Field moveControlField = Mob.class.getDeclaredField("moveControl");
            moveControlField.setAccessible(true);
            moveControlField.set(mob, new ImprovedFlyingMoveControl((BaseSiliconite) mob, 1f, true));
        } catch (Exception e) {
            Chemosynthesis.LOGGER.error("Failed to set flying navigation", e);
        }

        mob.goalSelector.addGoal(1, new FloatingSiliconiteRandomStrollGoal((BaseSiliconite) mob, 7f, 4f));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mutation_flight", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("active"));
        }));
    }
}
