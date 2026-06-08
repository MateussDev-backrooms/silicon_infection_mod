package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Mob;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class MutationFlight_Layer<T extends Mob & GeoAnimatable> extends GeoRenderLayer<T> {

    private final MutationFlight_Model model = new MutationFlight_Model();
    private final MutationFlight mutationData = new MutationFlight();

    public MutationFlight_Layer(GeoRenderer<?> entityRendererIn) {
        super((GeoRenderer<T>) entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        if(animatable instanceof IGenomeModifiable genmod && genmod.getGene() != null) {
            //Check if entity has the mutation in its genome
            if(!genmod.getGene().mutations.stream().anyMatch((mutation -> mutation instanceof MutationFlight))) {
                return;
            }
        }
        GeoBone targetBone = getRenderer().getGeoModel().getBone(mutationData.getAttachBoneName()).orElse(null);
        if (targetBone == null) return;

        poseStack.pushPose();

        RenderUtils.translateToPivotPoint(poseStack, targetBone);
        RenderUtils.translateMatrixToBone(poseStack, targetBone);
//        RenderUtils.rotateMatrixAroundBone(poseStack, targetBone);
//        RenderUtils.scaleMatrixForBone(poseStack, targetBone);
//        RenderUtils.translateAwayFromPivotPoint(poseStack, targetBone);
//
//        // Counteract the root 180° X flip that reRender will apply
//        poseStack.mulPose(new Quaternionf().rotationX((float) Math.PI));

        RenderType layerRenderType = RenderType.entityCutoutNoCull(model.getTextureResource(animatable));
        getRenderer().reRender(
                model.getBakedModel(model.getModelResource(animatable)),
                poseStack, bufferSource, animatable,
                layerRenderType, bufferSource.getBuffer(layerRenderType),
                partialTick, packedLight, packedOverlay,
                1f, 1f, 1f, 1f
        );

        poseStack.popPose();
    }
}
