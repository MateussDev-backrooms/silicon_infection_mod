package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
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

    public enum BurrowState {
        SURFACE,
        BURROWING,
        UNDERGROUND,
        UNBURROWING
    }

    public BurrowState burrowState = BurrowState.SURFACE;

    private int transitionTimer = 0;
    private int burrowCooldown = 0;
    private int particleTimer = 0;


    public static final int TRANSITION_DURATION = 25;
    public static final int BURROW_COOLDOWN_DURATION = 200;

    // How far underground the mob sinks (in blocks, negative = down)
    public static final float MAX_BURROW_DEPTH = -48f;

    public static final ResourceLocation TYPE_ID = new ResourceLocation(Chemosynthesis.MODID, "burrowing");

    private boolean isMoving = false;

    @Override public boolean hasRenderLayer() { return true; }
    @Override public String getAttachBoneName() { return "body"; }

    @Override
    public int tier() { return 2; }

    @Override
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationBurrowing_Layer<>(hostRenderer, this);
    }

    @Override public GeoModel<? extends Mutation> getModel() { return model; }
    @Override public GeoRenderer<? extends Mutation> getRenderer() { return renderer; }

    @Override
    public void onInit(Mob mob) {
        mob.goalSelector.addGoal(-1, new BurrowGoal(mob, this));
    }

    @Override
    public void onTick(Mob mob) {
        if(mob.level() instanceof ServerLevel slvl) {
            isMoving = mob.getDeltaMovement().length() > 0 && mob.onGround();

            if (burrowCooldown > 0) burrowCooldown--;

            if(mob instanceof IGenomeModifiable genmod) {
                //Update state in entity data
                CompoundTag state = new CompoundTag();
                state.putInt("burrowState", burrowState.ordinal());
                state.putInt("transitionTimer", transitionTimer);
                state.putInt("burrowCooldown", burrowCooldown);

                genmod.setMutationState(mob, getTypeId(), genmod.getMutationStateAccessor(), state);
            }

            switch (burrowState) {
                case BURROWING -> tickBurrowing(mob, slvl);
                case UNDERGROUND -> tickUnderground(mob, slvl);
                case UNBURROWING -> tickUnburrowing(mob, slvl);
                case SURFACE -> {}
            }
        }
    }

    private void tickBurrowing(Mob mob, ServerLevel slvl) {
        transitionTimer++;

        //Freeze the mob in place during transition
        mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);
        mob.getNavigation().stop();

        //Spawn block-breaking particles as it digs in
        spawnDigParticles(mob, slvl);

        if (transitionTimer >= TRANSITION_DURATION) {
            transitionTimer = 0;
            burrowState = BurrowState.UNDERGROUND;
            //Prevent the mob from unburrowing when burrowed
            burrowCooldown = BURROW_COOLDOWN_DURATION;
        }
    }

    private void tickUnderground(Mob mob, ServerLevel slvl) {
        // Keep mob frozen and pinned to ground level
        mob.setDeltaMovement(
                mob.getDeltaMovement().x * 0.3,
                0,
                mob.getDeltaMovement().z * 0.3
        );

        // Block-breaking particles periodically while crawling underground
        particleTimer++;
        if (particleTimer >= 8) {
            spawnDigParticles(mob, slvl);
            particleTimer = 0;
        }

        // Prevent jumping and pushes by zeroing vertical momentum
        if (!mob.onGround()) {
            mob.setDeltaMovement(mob.getDeltaMovement().x, -0.1, mob.getDeltaMovement().z);
        }
    }

    private void tickUnburrowing(Mob mob, ServerLevel slvl) {
        transitionTimer++;

        // Keep frozen during transition
        mob.setDeltaMovement(0, mob.getDeltaMovement().y, 0);
        mob.getNavigation().stop();

        spawnDigParticles(mob, slvl);

        if (transitionTimer >= TRANSITION_DURATION) {
            transitionTimer = 0;
            burrowState = BurrowState.SURFACE;
            burrowCooldown = BURROW_COOLDOWN_DURATION;
        }
    }



    @Override
    public float onHurt(Mob mob, DamageSource source, float amount) {
        //Prevent invulnerability from /kill
        if(amount > 1000f) return 1f;
        //Do not receive damage when underground
        return burrowState == BurrowState.UNDERGROUND ? 0.0f : 1.0f;
    }

    @Override
    public void onRemove(Mob mob) {
        if(mob instanceof IGenomeModifiable genmod) {
            CompoundTag cleanState = new CompoundTag();
            cleanState.putInt("burrowState", BurrowState.SURFACE.ordinal());
            cleanState.putInt("transitionTimer", TRANSITION_DURATION);
            genmod.setMutationState(mob, getTypeId(), genmod.getMutationStateAccessor(), cleanState);
        }
    }

    @Override
    public boolean canDealDamage(Mob mob, Entity target) {
        return burrowState == BurrowState.SURFACE;
    }

    @Override
    public boolean canBeSeen(Mob mob) {
        return burrowState == BurrowState.SURFACE || burrowState == BurrowState.UNBURROWING;
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

    // ===== Actions ===== //

    public void startBurrowing(Mob mob) {
        if (burrowState != BurrowState.SURFACE || burrowCooldown > 0) return;
        burrowState = BurrowState.BURROWING;
        transitionTimer = 0;
    }

    public void startUnburrowing(Mob mob) {
        if (burrowState != BurrowState.UNDERGROUND || burrowCooldown > 0) return;
        burrowState = BurrowState.UNBURROWING;
        transitionTimer = 0;
    }

    public boolean isFullyBurrowed() {
        return burrowState == BurrowState.UNDERGROUND;
    }

    public boolean isFullySurfaced() {
        return burrowState == BurrowState.SURFACE;
    }

    public boolean isBurrowCoolingDown() {
        return burrowCooldown > 0;
    }

    public static float getBurrowRenderOffset(Mob mob, ResourceLocation typeId) {
        if (!(mob instanceof IGenomeModifiable genmod)) return 0f;
        CompoundTag state = genmod.getMutationState(mob, typeId, genmod.getMutationStateAccessor());
        int stateOrdinal = state.getInt("burrowState");
        BurrowState burrowState = BurrowState.values()[stateOrdinal];
        int transitionTimer = state.getInt("transitionTimer");
        return switch (burrowState) {
            case SURFACE -> 0f;
            case UNDERGROUND -> MAX_BURROW_DEPTH;
            case BURROWING -> Mth.lerp((float) transitionTimer / TRANSITION_DURATION, 0f, MAX_BURROW_DEPTH);
            case UNBURROWING -> Mth.lerp((float) transitionTimer / TRANSITION_DURATION, MAX_BURROW_DEPTH, 0f);
        };
    }

    private void spawnDigParticles(Mob mob, ServerLevel slvl) {
        BlockPos below = mob.blockPosition().below();
        BlockState groundState = slvl.getBlockState(below);

        if (!groundState.isAir()) {
            slvl.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, groundState),
                    mob.getX(), mob.getY() + 0.1, mob.getZ(),
                    6,
                    0.3, 0.1, 0.3,
                    0.05
            );
        }
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
