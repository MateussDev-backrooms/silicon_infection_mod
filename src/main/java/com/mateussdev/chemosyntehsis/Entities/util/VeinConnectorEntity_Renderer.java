package com.mateussdev.chemosyntehsis.Entities.util;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VeinConnectorEntity_Renderer extends EntityRenderer<VeinConnectorEntity> {
    private static final ResourceLocation BEAM_TEXTURE =
            new ResourceLocation("textures/entity/guardian_beam.png");

    public VeinConnectorEntity_Renderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(VeinConnectorEntity veinConnectorEntity) {
        return null;
    }

    @Override
    public void render(VeinConnectorEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        List<BlockPos> positions = pEntity.getClientConnectionPositions();

            VertexConsumer consumer = pBuffer.getBuffer(
//                    RenderType.debugQuads()
                    RenderType.entityTranslucentCull(new ResourceLocation(Chemosynthesis.MODID, "textures/block/amalgamated_flesh_block.png"))
            );
//        StaticRenderingMethods.renderDebugCube(consumer, pPoseStack, pPoseStack.last(), 0, 0, 0, 0.5f, 0.5f, 0.5f, 1f, 1f, 1f, 1f, 15728880);

        for(BlockPos pos : positions) {

            Vec3 sourcePos = pEntity.blockPosition().getCenter();
            Vec3 targetPos = pos.getCenter();

            Vec3 relPos = targetPos.subtract(sourcePos).scale(0.5f);

            float dst = (float) sourcePos.distanceTo(targetPos);

            StaticSiliconiteMethods.renderParabolaTube(
                    consumer, pPoseStack.last(), pPackedLight,
                    0, 0, 0,
                    relPos.x, relPos.y, relPos.z,
                    0.4f, 1f, 1f, 0.45f,
                    Mth.floor(dst / 2f), dst , 1f);
        }
    }

    @Override
    public boolean shouldRender(VeinConnectorEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
