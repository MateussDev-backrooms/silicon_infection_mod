package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Random;

public class MutationBurrowing extends Mutation {

    public MutationBurrowing(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationBurrowing> model = new MutationBurrowing_Model();
    private final GeoRenderer<MutationBurrowing> renderer = new MutationBurrowing_Renderer(model);

    private boolean isMoving = false;

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "body"; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationBurrowing_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            isMoving = mob.getDeltaMovement().length() > 0 && mob.onGround();
        }
    }

    @Override
    public void onInit(Mob mob) {
        //TODO Make burrow mutation have a two-state mob that burrows and unburrows
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
        controllers.add(new AnimationController<>(this, "mutation_burrowing", 5, event -> {
            return isMoving ? event.setAndContinue(RawAnimation.begin().thenLoop("walk")) : event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public Mutation copy(Random rng) {
        return new MutationBurrowing(this.getTypeId(), rng.nextInt(Integer.MAX_VALUE));
    }
}
