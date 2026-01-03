package com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ChunkOfFlesh_Model extends GeoModel<ChunkOfFlesh> {
    //MODEL PATH
    private static final ResourceLocation model = new ResourceLocation(Chemosynthesis.MODID,
            "geo/entity/chunk_of_flesh.geo.json");
    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/entity/chunk_of_flesh.png");
    //ANIMATIONS PATH
    private static final ResourceLocation animation = new ResourceLocation(Chemosynthesis.MODID,
            "animations/entity/chunk_of_flesh.animation.json");

    @Override
    public ResourceLocation getModelResource(ChunkOfFlesh object) { return model; }

    @Override
    public ResourceLocation getTextureResource(ChunkOfFlesh object) { return texture; }

    @Override
    public ResourceLocation getAnimationResource(ChunkOfFlesh object) { return animation; }

    private int t;
    @Override
    public void setCustomAnimations(ChunkOfFlesh animatable, long instanceId, AnimationState<ChunkOfFlesh> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        var root = this.getBone("root");

        // Evolve anim
        if(root != null && animatable.mustEvolve) {
            ++t;
            float scale = Mth.sin(t*8f)*0.15f;
            root.get().setScaleX(1f + scale);
            root.get().setScaleY(1f + scale);
            root.get().setScaleZ(1f + scale);
        }
    }
}
