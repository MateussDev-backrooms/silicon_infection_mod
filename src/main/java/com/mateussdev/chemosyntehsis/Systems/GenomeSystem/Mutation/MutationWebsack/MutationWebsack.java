package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationWebsack;

import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.web_shot.WebShotEntity;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.FloatingSiliconiteRandomStrollGoal;
import com.mateussdev.chemosyntehsis.Entities.generic.AI.ImprovedFlyingMoveControl;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Mixin.MobAccessor;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.List;
import java.util.Random;

public class MutationWebsack extends Mutation {

    //=====Constantz=====//

    private static final float WEBSHOOT_MIN_DISTANCE = 4f; //If the mob is closer than this it cannot shoot webs
    private static final float WEBSHOOT_MAX_DISTANCE = 9f; //The mob must be at least this many blocks close to be shot at
    private static final int WEBSHOOT_COOLDOWN = 100;

    public MutationWebsack(ResourceLocation typeId, int mutation_id) {
        super(typeId, mutation_id);
    }

    private final GeoModel<MutationWebsack> model = new MutationWebsack_Model();
    private final GeoRenderer<MutationWebsack> renderer = new MutationWebsack_Renderer(model);

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "body"; }

    @Override
    public int tier() { return 3; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationWebsack_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    private float webshootCooldown = 0f;

    @Override
    public void onTick(Mob mob) {
        if(!(mob.level() instanceof ServerLevel slvl)) return;
        //I want the shooting of webs to run in parallel to attacking, so this is not in an AI task
        //Also I'm lazy :P

        LivingEntity target = mob.getTarget();
        if(target != null) {
            float _dst = mob.distanceTo(target);
            if(--webshootCooldown <= 0) {
                if(_dst <= WEBSHOOT_MAX_DISTANCE && _dst >= WEBSHOOT_MIN_DISTANCE) {
                    shootWebAtTarget(mob, target, _dst, slvl);
                    webshootCooldown = WEBSHOOT_COOLDOWN;
                }
            }
        }
    }

    public void shootWebAtTarget(Mob mob, LivingEntity target, float distance, ServerLevel slvl) {
        //Calculate shoot angle
        WebShotEntity shot = new WebShotEntity(slvl, mob);
        shot.setPos(mob.getEyePosition());
        Vec3 shootDirection = target.position().subtract(mob.position());
        shot.shoot(shootDirection.x, shootDirection.y, shootDirection.z, 2f, 0.2f);
        slvl.addFreshEntity(shot);
    }

    @Override
    public void onInit(Mob mob) {

    }

    @Override
    public void onRemove(Mob mob) {
        //Reset values
    }

    @Override
    public boolean canMutateMob(Mob mob) {

        //Generic mob mask
        List<EntityType<?>> disabledMobs = List.of(
                ModEntities.TETH_ENDERMAN.get()
        );

        for(EntityType<?> type : disabledMobs) {
            if(mob.getType() == type) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canPassThroughBlock(Mob mob, BlockState state) {
        return state.is(Blocks.COBWEB);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "mutation_flight", 5, event -> {
            return event.setAndContinue(RawAnimation.begin().thenLoop("active"));
        }));
    }

    @Override
    public Mutation copy(Random rng) {
        return new MutationWebsack(this.getTypeId(), rng.nextInt(Integer.MAX_VALUE));
    }
}
