package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Core.ModRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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

public abstract class Mutation implements GeoAnimatable {

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

    //Condition for applying mutation. Usually for masking certain mobs
    public abstract boolean canMutateMob(Mob mob);

    // ===== Graphics ===== //

    public boolean hasRenderLayer() { return false; }
    public String getAttachBoneName() { return "body"; }
    public GeoRenderLayer<?> createRenderLayer(GeoRenderer<?> hostRenderer) {
        // Default – may be overridden by mutations that have custom rendering
        return new MutationBaseRenderLayer<>((GeoEntityRenderer<?>) hostRenderer, this);
    }

    /** Provide the GeoModel instance used for this mutation's addon geometry */
    public abstract GeoModel<? extends Mutation> getModel();

    /** Provide the GeoRenderer instance used for this mutation (lightweight, can be shared) */
    public abstract GeoRenderer<? extends Mutation> getRenderer();

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        throw new NotImplementedException();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object object) {
        return RenderUtils.getCurrentTick();
    }

    public ResourceLocation getTypeId() {
        return typeId;
    };

    // ===== Saving loading ===== //
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("typeId", getTypeId().toString());
        tag.putInt("mutationId", id);   // the integer ID passed in constructor
        return tag;
    }

    public static Mutation deserialize(CompoundTag tag) {
        String typeIdStr = tag.getString("typeId");
        ResourceLocation typeId = new ResourceLocation(typeIdStr);
        int mutationId = tag.getInt("mutationId");

        // Look up from DeferredRegister – always works after mod init
        RegistryObject<MutationType> ro = ModMutations.findMutationType(typeId);
        if (ro == null) {
            Chemosynthesis.LOGGER.error("Unknown mutation type: " + typeId);
            return null;
        }
        MutationType type = ro.get();
        return type.create(mutationId);
    }


}
