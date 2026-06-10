package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.lang.reflect.Field;
import java.util.List;

public class MutationSwimming extends Mutation {

    public MutationSwimming(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationSwimming> model = new MutationSwimming_Model();
    private final GeoRenderer<MutationSwimming> renderer = new MutationSwimming_Renderer(model);

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

        }
    }

    @Override
    public void onInit(Mob mob) {
        //TODO Make swimming mutation create fast swimmers
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
}
