package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class Mutation<T extends Mob & GeoAnimatable> {

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

    public void onAiRegisterGoals(Mob mob) {
        //Triggers once when the mutation is applied, allowing for AI modification
        //Useful for adding and removing goals
    }

    public GeoRenderLayer<T> getMutationRenderLayer(GeoRenderer<T> renderer) {
        //The render layer that will be applied to the recepient when they get the mutation
        return null;
    }

    public String getAttachBoneName() {
        return "body";
    }
}
