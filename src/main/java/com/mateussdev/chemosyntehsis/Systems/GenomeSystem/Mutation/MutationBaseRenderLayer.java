package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

//Empty CUZ I want creative control :P
public class MutationBaseRenderLayer<T extends Mob & GeoAnimatable> extends GeoRenderLayer<T> {
    protected final Mutation mutationReference;
    public MutationBaseRenderLayer(GeoEntityRenderer<?> entityRendererIn, Mutation mutation) {
        super((GeoRenderer<T>) entityRendererIn);
        this.mutationReference = mutation;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(!hasMutation(animatable)) return;

        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

    private boolean hasMutation(T animatable) {
        if (animatable instanceof IGenomeModifiable genmod && genmod.getGene() != null) {
            // Compare by mutation type ID (ResourceLocation)
            return genmod.getGene().mutations.stream()
                    .anyMatch(m -> m.getTypeId().equals(mutationReference.getTypeId()));
        }
        return false;
    }
}
