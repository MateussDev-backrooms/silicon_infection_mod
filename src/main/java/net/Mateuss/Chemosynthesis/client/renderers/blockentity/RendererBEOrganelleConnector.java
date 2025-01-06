package net.Mateuss.Chemosynthesis.client.renderers.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.block.blockentity.BEOrganelleConnector;
import net.Mateuss.Chemosynthesis.client.GeneralRenderingFunctions;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class RendererBEOrganelleConnector implements BlockEntityRenderer<BEOrganelleConnector> {

    public RendererBEOrganelleConnector(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public void render(BEOrganelleConnector pEntity, float v, PoseStack pPoseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        List<BEOrganelleConnector> nearbyConnectors = pEntity.getListNeighbours();

        VertexConsumer vc = multiBufferSource.getBuffer(RenderType.entityCutoutNoCull(new ResourceLocation(Chemosynthesis.MODID, "textures/block/blood_tissue.png")));
        PoseStack.Pose pose = pPoseStack.last();
        BlockPos currentPos = pEntity.getBlockPos();

        GeneralRenderingFunctions.renderParabolaTube(vc, pose,
                i, 0, 0, 0, 0, 10, 0, 1f, 1f, 1f, 0.5f, 8, 10, 0f
        );

//        for(BEOrganelleConnector connector : nearbyConnectors) {
//            pPoseStack.pushPose();
//
//            BlockPos connectedPos = connector.getBlockPos();
//
//                double relX = connectedPos.getX() - currentPos.getX() +0.5f;
//                double relY = connectedPos.getY() - currentPos.getY() +0.5f;
//                double relZ = connectedPos.getZ() - currentPos.getZ() +0.5f;
//
//                float dst = (float) Math.sqrt(connectedPos.distSqr(currentPos));
//
//                GeneralRenderingFunctions.renderParabolaTube(vc, pose, i, 0.5f, 0.5f, 0.5f, relX, relY, relZ,
//                        1f, 1f, 1f,
//                        0.4f, 8, dst, 0.5f);
//                pPoseStack.popPose();
//            if(currentPos.compareTo(connectedPos) < 0) {
//            }
//
//        }
//        pPoseStack.pushPose();
//        GeneralRenderingFunctions.renderParabolaTube(vc, pose, i, 0.5f, 0.5f, 0.5f, 0.5f, 5f, 0.5f,
//                1f, 1f, 1f,
//                0.4f, 8, 5f, 0.5f);
//        pPoseStack.popPose();

    }
}
