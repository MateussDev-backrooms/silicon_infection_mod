package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralRenderingFunctions;
import net.Mateuss.Chemosynthesis.client.models.ModelBrachaticHarpoonBulb;
import net.Mateuss.Chemosynthesis.entity.projectile.ProjectileBrachaticHarpoon;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RendererBrachaticHarpoon extends ArrowRenderer<ProjectileBrachaticHarpoon> {

    private final ModelBrachaticHarpoonBulb model;


    public RendererBrachaticHarpoon(EntityRendererProvider.Context pContext, ModelLayerLocation layer) {
        super(pContext);
        this.model = new ModelBrachaticHarpoonBulb(pContext.bakeLayer(layer));
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileBrachaticHarpoon ProjectileBrachaticHarpoon) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/singular_bulb.png");
    }

    @Override
    public void render(ProjectileBrachaticHarpoon pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        Entity parent = pEntity.getOwner();
        if(parent != null) {
            pPoseStack.pushPose();

            double parentX = parent.getX() - pEntity.getX();
            double parentY = parent.getEyeY() - pEntity.getY();
            double parentZ = parent.getZ() - pEntity.getZ();

            VertexConsumer vc = pBuffer.getBuffer(RenderType.leash());
            PoseStack.Pose pose = pPoseStack.last();

            float dst = pEntity.distanceTo(parent);
            float thickness = 0.033f+(1f-Math.min(dst/20f, 1f))*0.4f;



            GeneralRenderingFunctions.renderParabolaLine(vc, pose, 0, 0, 0, parentX, parentY, parentZ, 0.5f, 0f, 0f, thickness, 8, dst);

            pPoseStack.popPose();
        }
    }

}
