package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class BEVeinBlock_Renderer implements BlockEntityRenderer<BEVeinBlock> {
    public BEVeinBlock_Renderer(BlockEntityRendererProvider.Context ctx) {}

    //TEXTURE PATH
    private static final ResourceLocation texture = new ResourceLocation(Chemosynthesis.MODID,
            "textures/block/vein_block.png");

    @Override
    public void render(
            BEVeinBlock be,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light,
            int overlay
    ) {
        Vec3 start = Vec3.atCenterOf(be.getBlockPos());

        float time = (be.getLevel().getGameTime() + partialTick) * 0.05f;
        float pulse = 0.1f + Mth.sin(time) * 0.05f;

        for (BlockPos targetPos : be.getConnections()) {
            Vec3 end = Vec3.atCenterOf(targetPos);
            Vec3 origin = Vec3.atCenterOf(targetPos.subtract(be.getBlockPos()));

            renderVein(start, end, pulse, poseStack, buffer, light);
        }
    }

    private void renderVein(
            Vec3 start,
            Vec3 end,
            float thickness,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int light
    ) {
        VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucent(texture));

        Vec3 mid = start.add(end).scale(0.5);
        Vec3 offset = new Vec3(0, 0.5, 0); // sag / curve
        Vec3 control = mid.add(offset);

        int segments = 16;

        for (int i = 0; i < segments; i++) {
            float t1 = i / (float) segments;
            float t2 = (i + 1) / (float) segments;

            Vec3 p1 = bezier(start, control, end, t1);
            Vec3 p2 = bezier(start, control, end, t2);

            drawTubeSegment(vc, poseStack, p1, p2, thickness, light);
        }
    }

    private Vec3 bezier(Vec3 a, Vec3 b, Vec3 c, float t) {
        float u = 1 - t;
        return a.scale(u * u)
                .add(b.scale(2 * u * t))
                .add(c.scale(t * t));
    }

    private void drawTubeSegment(
            VertexConsumer vc,
            PoseStack poseStack,
            Vec3 p1,
            Vec3 p2,
            float radius,
            int light
    ) {
        PoseStack.Pose pose = poseStack.last();

        // Direction of the segment
        Vec3 dir = p2.subtract(p1).normalize();

        // Camera-facing perpendicular
        Vector3f _temp = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
        Vec3 cameraDir = new Vec3(_temp);
        Vec3 right = dir.cross(cameraDir).normalize().scale(radius);

        // Four corners of the quad
        Vec3 v1 = p1.add(right);
        Vec3 v2 = p1.subtract(right);
        Vec3 v3 = p2.subtract(right);
        Vec3 v4 = p2.add(right);

        float u1 = 0f;
        float u2 = 1f;
        float vStart = 0f;
        float vEnd = 1f;

        // Quad (double-sided)
        addVertex(vc, pose, v1, u1, vStart, light);
        addVertex(vc, pose, v2, u2, vStart, light);
        addVertex(vc, pose, v3, u2, vEnd, light);
        addVertex(vc, pose, v4, u1, vEnd, light);

        addVertex(vc, pose, v4, u1, vEnd, light);
        addVertex(vc, pose, v3, u2, vEnd, light);
        addVertex(vc, pose, v2, u2, vStart, light);
        addVertex(vc, pose, v1, u1, vStart, light);
    }

    private void addVertex(
            VertexConsumer vc,
            PoseStack.Pose pose,
            Vec3 pos,
            float u,
            float v,
            int light
    ) {
        vc.vertex(pose.pose(),
                        (float) pos.x,
                        (float) pos.y,
                        (float) pos.z)
                .color(180, 30, 30, 200) // blood red, semi-transparent
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(pose.normal(), 0, 1, 0)
                .endVertex();
    }


}
