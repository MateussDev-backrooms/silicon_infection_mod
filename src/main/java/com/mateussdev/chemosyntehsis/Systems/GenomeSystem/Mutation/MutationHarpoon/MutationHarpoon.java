package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationHarpoon;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.AbstractHarpoonProjectile;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.mutated_harpoon.MutatedHarpoonEntity;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Random;

public class MutationHarpoon extends Mutation {

    public MutationHarpoon(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationHarpoon> model = new MutationHarpoon_Model();
    private final GeoRenderer<MutationHarpoon> renderer = new MutationHarpoon_Renderer(model);

    public MutatedHarpoonEntity harpoonEntity;

    private static final int HARPOON_COOLDOWN = 50;

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "head"; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationHarpoon_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public void onInit(Mob mob) {
        mob.goalSelector.addGoal(-1, new RangedHarpoonAIGoal(mob, 1.5f, HARPOON_COOLDOWN, 8, this));
    }

    @Override
    public float onHurt(Mob mob, DamageSource source, float amount) {
//        if(harpoonEntity != null) {
//            harpoonEntity.setAttached(false);
//            harpoonEntity.setTarget(null);
//            harpoonEntity.setCurrentAttachType(AbstractHarpoonProjectile.AttachTypes.Reeling);
//            harpoonEntity = null;
//        }
        return super.onHurt(mob, source, amount);
    }

    @Override
    public boolean canMutateMob(Mob mob) {

        //Generic mob mask
        List<EntityType<?>> disabledMobs = List.of(
                ModEntities.TETH_SKELETON.get()
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
        controllers.add(new AnimationController<>(this, "mutation_harpoon", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public Mutation copy(Random rng) {
        return new MutationHarpoon(this.getTypeId(), rng.nextInt(Integer.MAX_VALUE));
    }
}
