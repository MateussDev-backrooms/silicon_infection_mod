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
    public final Mutation mutationReference;
    public MutationBaseRenderLayer(GeoEntityRenderer<?> entityRendererIn, Mutation mutation) {
        super((GeoRenderer<T>) entityRendererIn);
        this.mutationReference = mutation;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof IGenomeModifiable genmod && !(genmod.hasMutationType(mutationReference.getTypeId()))) return;

        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
