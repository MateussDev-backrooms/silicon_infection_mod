package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.GeneralRenderingFunctions;
import net.Mateuss.Chemosynthesis.client.models.ModelHomVaucole;
import net.Mateuss.Chemosynthesis.client.models.ModelHomunculus;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomVaucole;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class RendererHomVaucole extends MobRenderer<EntityHomVaucole, ModelHomVaucole<EntityHomVaucole>> {
    public RendererHomVaucole(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelHomVaucole<>(pContext.bakeLayer(GeneralModModelLayers.HOMUNCULUS_VAUCOLE_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHomVaucole entityHomVaucole) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/homunculus_vaucole.png");
    }

    @Override
    public void render(EntityHomVaucole pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);

        Entity parent = pEntity.findNearestHomunculus();
        VertexConsumer vc = pBuffer.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(Chemosynthesis.MODID, "textures/block/blood_tissue.png")));
        PoseStack.Pose pose = pPoseStack.last();

        if(parent != null) {
            pPoseStack.pushPose();

            double relX = parent.getX() - pEntity.getX();
            double relY = parent.getEyeY() - pEntity.getY();
            double relZ = parent.getZ() - pEntity.getZ();

            float dst = pEntity.distanceTo(parent);

            GeneralRenderingFunctions.renderParabolaTube(vc, pose, pPackedLight, 0, 0, 0, relX, relY+pEntity.yOffset, relZ,
                    1f-pEntity.randomBrightness, 1f-pEntity.randomBrightness, 1f-pEntity.randomBrightness,
                    pEntity.veinThickness, 8, dst, 0.5f);
            pPoseStack.popPose();
        }
//        GeneralRenderingFunctions.renderParabolaLine(vc, pose, 0, 0, 0, 0, 0, 10, 0f, 1f, 0f, 0.3f, 8, 10f);
    }
}
