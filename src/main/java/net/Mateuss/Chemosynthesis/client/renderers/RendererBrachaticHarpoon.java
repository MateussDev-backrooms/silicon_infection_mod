package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
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



            renderSegmentedLine(vc, pose, 0, 0, 0, parentX, parentY, parentZ, 0.5f, 0f, 0f, thickness, 8, dst);

            pPoseStack.popPose();
        }
    }

    private void renderSegmentedLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
                                     double endX, double endY, double endZ, float red, float green, float blue, float thickness, int segments, float distance) {
        double dx = (endX - startX)/segments;
        double dy = (endY - startY)/segments;
        double dz = (endZ - startZ)/segments;

        double prevX = startX;
        double prevY = startY;
        double prevZ = startZ;

        for(int i=1; i<=segments; i++) {
            double s_endX = startX + dx*i;
            double s_endY = startY + dy*i + simulateGravity((distance/segments)*i, distance);
            double s_endZ = startZ + dz*i;

            double midX = (prevX + s_endX)/2;
            double midY = (prevY + s_endY)/2;
            midY += simulateGravity((distance/segments)*(i - 0.5f), distance);
            double midZ = (prevZ + s_endZ)/2;

            renderLine(vertexConsumer, pose, prevX, prevY, prevZ, s_endX, s_endY, s_endZ, red, green, blue, thickness);

            prevX = s_endX;
            prevY = s_endY;
            prevZ = s_endZ;
        }
    }

    private double simulateGravity(float x, float distance) {
        double midpoint = distance/2;

        return ((double)x-midpoint)*((double)x-midpoint) * (1/(midpoint*midpoint)) - 1;
    }

    private void renderLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
                            double endX, double endY, double endZ, float red, float green, float blue, float thickness) {
        // Compute the direction vector
        float dx = (float) (endX - startX);
        float dy = (float) (endY - startY);
        float dz = (float) (endZ - startZ);

        // Normalize direction vector
        float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length == 0) return; // Avoid division by zero
        dx /= length;
        dy /= length;
        dz /= length;

        // Create perpendicular offset for thickness (using cross product)
        float offsetX = -dz * thickness / 2;
        float offsetZ = dx * thickness / 2;

        // Define the four corners of the thick line (rectangle)
        float startX1 = (float) startX + offsetX;
        float startZ1 = (float) startZ + offsetZ;
        float startX2 = (float) startX - offsetX;
        float startZ2 = (float) startZ - offsetZ;

        float endX1 = (float) endX + offsetX;
        float endZ1 = (float) endZ + offsetZ;
        float endX2 = (float) endX - offsetX;
        float endZ2 = (float) endZ - offsetZ;

        // Render the rectangle (two triangles forming a quad)
        renderQuad(vertexConsumer, pose, startX1, startY, startZ1, endX1, endY, endZ1, endX2, endY, endZ2, startX2, startY, startZ2, red, green, blue);
    }

    private void renderQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                            float x1, double y1, float z1, float x2, double y2, float z2,
                            float x3, double y3, float z3, float x4, double y4, float z4,
                            float red, float green, float blue) {
        // Define the quad vertices with all required attributes
        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue);
        vertex(vertexConsumer, pose, x2, y2, z2, red, green, blue);
        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue);

        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue);
        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue);
        vertex(vertexConsumer, pose, x4, y4, z4, red, green, blue);
    }

    private void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, double x, double y, double z, float red, float green, float blue) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, 1.0F) // Color
                .uv(0.0F, 0.0F)               // Texture UV (unused here)
                .overlayCoords(OverlayTexture.NO_OVERLAY) // Overlay texture
                .uv2(15728880)                // Lightmap texture (full brightness)
                .normal(pose.normal(), 0.0F, 1.0F, 0.0F) // Normal vector (arbitrary up)
                .endVertex();
    }

}
