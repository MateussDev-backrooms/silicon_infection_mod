package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.models.ModelBulbProjectile;
import net.Mateuss.Chemosynthesis.entity.projectile.ProjectileBulb;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RendererBulbProjectile extends EntityRenderer<ProjectileBulb> {

    private final ModelBulbProjectile model;


    public RendererBulbProjectile(EntityRendererProvider.Context pContext, ModelLayerLocation layer) {
        super(pContext);
        this.model = new ModelBulbProjectile(pContext.bakeLayer(layer));
    }

    @Override
    public ResourceLocation getTextureLocation(ProjectileBulb ProjectileBulb) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/singular_bulb.png");
    }

    @Override
    public void render(ProjectileBulb pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot())+ 180f));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.translate(0f, -1.33f, 0f);
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, false);
        this.model.renderToBuffer(pPoseStack, $$6, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

}
