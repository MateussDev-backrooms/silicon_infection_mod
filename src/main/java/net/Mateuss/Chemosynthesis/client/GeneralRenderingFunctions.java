package net.Mateuss.Chemosynthesis.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class GeneralRenderingFunctions {
    public static void renderLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
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

    public static void renderQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
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

    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, double x, double y, double z, float red, float green, float blue) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, 1.0F) // Color
                .uv(0.0F, 0.0F)               // Texture UV (unused here)
                .overlayCoords(OverlayTexture.NO_OVERLAY) // Overlay texture
                .uv2(15728880)                // Lightmap texture (full brightness)
                .normal(pose.normal(), 0.0F, 1.0F, 0.0F) // Normal vector (arbitrary up)
                .endVertex();
    }

    public static void renderParabolaLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
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

    private static double simulateGravity(float x, float distance) {
        double midpoint = distance/2;

        return ((double)x-midpoint)*((double)x-midpoint) * (1/(midpoint*midpoint)) - 1;
    }
}
