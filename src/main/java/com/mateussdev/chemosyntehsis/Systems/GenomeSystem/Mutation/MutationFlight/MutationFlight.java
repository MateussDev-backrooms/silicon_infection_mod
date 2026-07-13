package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Mixin.MobAccessor;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.Tags;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class MutationFlight extends Mutation {

    public MutationFlight(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationFlight> model = new MutationFlight_Model();
    private final GeoRenderer<MutationFlight> renderer = new MutationFlight_Renderer(model);

    //AI revert vars//
    private MoveControl oldMoveControl;
    private PathNavigation oldNavigation ;
    private boolean oldGravity;

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "body"; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationFlight_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public int tier() { return 1; }

    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            //Float upward when on ground
//            float minFlyDist = mob.getTarget() == null ? 1f : mob.getTarget().getBbHeight();
//            BlockHitResult raycastDown = slvl.clip(new ClipContext(mob.position(), mob.position().add(0, -minFlyDist, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
//            if(raycastDown.getType() != HitResult.Type.MISS) {
//                mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.05, 0));
//            }
//
//            //Float down when hitting ceiling
//            BlockHitResult raycastUp = slvl.clip(new ClipContext(mob.position(), mob.position().add(0, 1, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, mob));
//            if(raycastUp.getType() != HitResult.Type.MISS) {
//                mob.setDeltaMovement(mob.getDeltaMovement().add(0, -0.05, 0));
//            }
        }
    }

    @Override
    public void onInit(Mob mob) {
        //Save the to-be changed values
        oldGravity = mob.isNoGravity();
        oldMoveControl = ((MobAccessor)mob).getMoveControl();
        oldNavigation = ((MobAccessor)mob).getNavigation();

        //Add floating AI functionality
        mob.setNoGravity(true);
        ((MobAccessor)mob).setNavigation(new FlyingPathNavigation(mob, mob.level()));
        ((MobAccessor)mob).setMoveControl(new ImprovedFlyingMoveControl((BaseSiliconite) mob, 1f, true));

        //Add floating goal
        mob.goalSelector.addGoal(1, new FloatingSiliconiteRandomStrollGoal((BaseSiliconite) mob, 7f, 4f));
    }

    @Override
    public void onRemove(Mob mob) {
        //Reset values
        mob.setNoGravity(oldGravity);
        ((MobAccessor)mob).setNavigation(oldNavigation);
        ((MobAccessor)mob).setMoveControl(oldMoveControl);
    }

    @Override
    public boolean canMutateMob(Mob mob) {

        //Generic mob mask
        List<EntityType<?>> disabledMobs = List.of(
                ModEntities.TETH_ENDERMAN.get(),
                ModEntities.TETH_VILLAGER.get()
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
        controllers.add(new AnimationController<>(this, "mutation_flight", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("active"));
        }));
    }

    @Override
    public Mutation copy(Random rng) {
        return new MutationFlight(this.getTypeId(), rng.nextInt(Integer.MAX_VALUE));
    }
}
