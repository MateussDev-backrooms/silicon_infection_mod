package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationHarpoon;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBaseRenderLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Mob;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MutationHarpoon_Layer<T extends Mob & GeoAnimatable> extends MutationBaseRenderLayer<T> {

    private final GeoModel<MutationHarpoon> mutationModel;
    private final GeoRenderer<MutationHarpoon> mutationRenderer;

    public MutationHarpoon_Layer(GeoRenderer<?> entityRendererIn, MutationHarpoon mutation) {
        super((GeoEntityRenderer<?>) entityRendererIn, mutation);
        this.mutationModel = new MutationHarpoon_Model();
        this.mutationRenderer = new MutationHarpoon_Renderer(mutationModel);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        if (animatable instanceof IGenomeModifiable genmod && !(genmod.hasMutationType(mutationReference.getTypeId()))) return;

        //Cast to the actual mutation here instead of using the generic one:
        MutationHarpoon castedMutationRef = (MutationHarpoon) mutationReference;

        //Get socket bone
        GeoModel<T> hostModel = getRenderer().getGeoModel();
        CoreGeoBone socketBone = hostModel
                .getBone(mutationReference.getAttachBoneName())
                .orElse(null);
        if (socketBone == null) return;

        poseStack.pushPose();
        Vector3d vec = ((GeoBone) socketBone).getWorldPosition().mul(-1).div(4);
        Matrix4f boneMatrix = new Matrix4f(((GeoBone) socketBone).getModelSpaceMatrix());
//        boneMatrix.scale(1/16f);

        poseStack.mulPose(new Quaternionf().rotationY(
                (float) Math.toRadians(-animatable.yBodyRot)
        ));
        poseStack.mulPose(new Quaternionf().rotationY(
                (float) Math.toRadians(180)
        ));
        poseStack.mulPoseMatrix(boneMatrix);
//        poseStack.translate(vec.x, vec.y, vec.z);
//        poseStack.translate(vec.x, vec.y, vec.z);


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
