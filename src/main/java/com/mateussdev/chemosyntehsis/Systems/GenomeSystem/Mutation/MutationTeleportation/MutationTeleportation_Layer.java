package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationTeleportation;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBaseRenderLayer;
import com.mateussdev.chemosyntehsis.Util.Rendering.TextureUtilities;
import com.mateussdev.chemosyntehsis.Util.Rendering.UVScaleableVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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

public class MutationTeleportation_Layer<T extends Mob & GeoAnimatable> extends MutationBaseRenderLayer<T> {

    private final GeoModel<MutationTeleportation> mutationModel;
    private final GeoRenderer<MutationTeleportation> mutationRenderer;

    private static final ResourceLocation TINT_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/ender_overlay.png");
    private static final RenderType TINT_RENDER_TYPE = RenderType.entityTranslucent(TINT_TEXTURE);

    public MutationTeleportation_Layer(GeoRenderer<?> entityRendererIn, MutationTeleportation mutation) {
        super((GeoEntityRenderer<?>) entityRendererIn, mutation);
        this.mutationModel = new MutationTeleportation_Model();
        this.mutationRenderer = new MutationTeleportation_Renderer(mutationModel);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        if (animatable instanceof IGenomeModifiable genmod && !(genmod.hasMutationType(mutationReference.getTypeId()))) return;

        //Render overlay
        ResourceLocation texLoc = getRenderer().getTextureLocation(animatable);
        float uvWidth = TextureUtilities.getUvWidthScaleForTexture(texLoc, 4f, 64);
        float uvHeight = TextureUtilities.getUvHeightScaleForTexture(texLoc, 4f, 64);

        VertexConsumer scaledConsumer = new UVScaleableVertexConsumer(bufferSource.getBuffer(TINT_RENDER_TYPE), uvWidth, uvHeight);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType, scaledConsumer, partialTick, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 0.33f);

        //Cast to the actual mutation here instead of using the generic one:
        MutationTeleportation castedMutationRef = (MutationTeleportation) mutationReference;

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
