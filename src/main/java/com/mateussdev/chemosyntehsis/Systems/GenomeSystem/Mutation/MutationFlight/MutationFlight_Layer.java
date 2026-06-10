package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBaseRenderLayer;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MutationFlight_Layer<T extends Mob & GeoAnimatable> extends MutationBaseRenderLayer<T> {

    private final GeoModel<MutationFlight> mutationModel;
    private final GeoRenderer<MutationFlight> mutationRenderer;

    public MutationFlight_Layer(GeoRenderer<?> entityRendererIn, MutationFlight mutation) {
        super((GeoEntityRenderer<?>) entityRendererIn, mutation);
        this.mutationModel = new MutationFlight_Model();
        this.mutationRenderer = new MutationFlight_Renderer(mutationModel);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
//        if(animatable instanceof IGenomeModifiable genmod && genmod.getGene() != null) {
//            //Check if entity has the mutation in its genome
//            if(!genmod.getGene().mutations.stream().anyMatch((mutation -> mutation instanceof MutationFlight))) {
//                return;
//            }
//        }

        StaticSiliconiteMethods.debugLog("renderinggggggg");

        //Cast to the actual mutation here instead of using the generic one:
        MutationFlight castedMutationRef = (MutationFlight) mutationReference;

        // Get the host entity's GeckoLib model
        GeoModel<T> hostModel = getRenderer().getGeoModel();

        // Find the socket bone on the HOST model
        CoreGeoBone socketBone = hostModel
                .getBone("body")
                .orElse(null);

        if (socketBone == null) return;

        poseStack.pushPose();

        //TODO: Translate and transform the poseStack to match the socketBone exactly
        //Rotation math suuuxxxxxx

        BakedGeoModel bakedMutationModel = mutationModel.getBakedModel(mutationModel.getModelResource(castedMutationRef));
        RenderType mutationRenderType = RenderType.entityCutoutNoCull(
                mutationModel.getTextureResource(castedMutationRef));

        mutationRenderer.actuallyRender(
                poseStack, castedMutationRef, bakedMutationModel, mutationRenderType, bufferSource, bufferSource.getBuffer(mutationRenderType), false, partialTick, packedLight, packedOverlay,
                1, 1, 1, 1
        );

        poseStack.popPose();
    }
}
