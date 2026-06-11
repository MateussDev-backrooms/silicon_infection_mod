package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Core.ModRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.NotImplementedException;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.util.Random;

public abstract class Mutation implements GeoAnimatable, Cloneable {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);
    private final ResourceLocation typeId;
    private final int id;

    public Mutation(ResourceLocation typeId, int mutation_id) {
        this.typeId = typeId;
        this.id = mutation_id;
    }

    public int getCost() {
        //Handles how expensive the mutation is to replicate
        return 1;
    }

    public int tier() {
        //Handles conflicting mutations. A Gene cannot have more than 1 mutation of the same tier
        //Fixes cases of mobs having both wings and being able to burrow underground (for balancing reasons)
        return 1;
    }

    // ===== Functionality ===== //
    public void onTick(Mob mob) {
        //This here is injected into the tick of the mutated creature
        //Made for adding new mechanics that are not AI-related
    }

    public void onInit(Mob mob) {
        //Triggers once when the mutation is applied, allowing for AI modification
        //Useful for adding and removing goals
    }

    public void onTickDeath(Mob mob) {
        //Triggers when the mob is in the dying phase
        //Useful for post-mortum functionality
    }

    public float onHurt(Mob mob, DamageSource source, float amount) {
        //Triggers when the mob is hurt
        //return is the multiplier for the damage received
        return 1.0f;
    }

    //Condition for applying mutation. Usually for masking certain mobs, or requiring certain prerequisites for the mob's mutation
    public abstract boolean canMutateMob(Mob mob);

    // ===== Graphics ===== //

    //Boolean that enables or disables any visuals for the mutation. Could be used to mask mutations for certain mobs while applying their benefits
    public boolean hasRenderLayer() { return false; }

    //Returns the name of the bone to which the mutation should be attached to
    public String getAttachBoneName() { return "body"; }

    //This function is ran by the MutationSyncPacket to instantiate and add a render layer to the model of a mob
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        return new MutationBaseRenderLayer<>((GeoEntityRenderer<?>) hostRenderer, this);
    }

    //Returns the GeoModel of the mutation
    public abstract GeoModel<? extends Mutation> getModel();

    //Returns the GeoRenderer of the mutation
    public abstract GeoRenderer<? extends Mutation> getRenderer();

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Allows for full-blown animation functionality for the mutation, like state machines, conditional animations and etc.
        throw new NotImplementedException();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //Ticks the animation. Needed backend for the animations to work
    @Override
    public double getTick(Object object) {
        return RenderUtils.getCurrentTick();
    }

    //Gets the MutationType ID
    public ResourceLocation getTypeId() {
        return typeId;
    };

    //Gets the int ID of the mutation instance
    public int getId() {
        return id;
    };

    // ===== Saving loading ===== //

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("typeId", getTypeId().toString());
        tag.putInt("mutationId", id);
        return tag;
    }

    public static Mutation deserialize(CompoundTag tag) {
        String typeIdStr = tag.getString("typeId");
        ResourceLocation typeId = new ResourceLocation(typeIdStr);
        int mutationId = tag.getInt("mutationId");

        RegistryObject<MutationType> ro = ModMutations.findMutationType(typeId);
        if (ro == null) {
            Chemosynthesis.LOGGER.error("Unknown mutation type: " + typeId);
            return null;
        }
        MutationType type = ro.get();
        return type.create(mutationId);
    }

    // ===== Cloning ===== //


    public abstract Mutation copy(Random rng);
}
