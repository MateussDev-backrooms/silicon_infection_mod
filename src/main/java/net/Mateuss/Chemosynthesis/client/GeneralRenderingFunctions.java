package net.Mateuss.Chemosynthesis.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.joml.Vector3f;

import java.awt.*;
import java.util.Optional;

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

        renderQuad(vertexConsumer, pose, startX1, (float) startY, startZ1, endX1, (float) endY, endZ1, endX2, (float) endY, endZ2, startX2, (float) startY, startZ2, red, green, blue, 15728880);
    }

    public static void renderQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                            float x1, float y1, float z1, float x2, float y2, float z2,
                            float x3, float y3, float z3, float x4, float y4, float z4,
                            float red, float green, float blue, int light) {
        float[] normal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        // Define the quad vertices with all required attributes
        vertex(vertexConsumer, pose, x1, y1, z1, normal, red, green, blue, 0f, 0f, light);
        vertex(vertexConsumer, pose, x2, y2, z2, normal, red, green, blue, 1f, 0f, light);
        vertex(vertexConsumer, pose, x3, y3, z3, normal, red, green, blue, 1f, 1f, light);
        vertex(vertexConsumer, pose, x4, y4, z4, normal, red, green, blue, 0f, 1f, light);

//        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue, 0f, 0f, light);
//        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue, 1f, 1f, light);
    }
    public static void renderShadedQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose, Level level,
                            float x1, float y1, float z1, float x2, float y2, float z2,
                            float x3, float y3, float z3, float x4, float y4, float z4,
                            float red, float green, float blue) {
        float[] normal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);

        // Define the quad vertices with all required attributes

        vertex(vertexConsumer, pose, x1, y1, z1, normal, red, green, blue, 0f, 0f, getPackedLight(level, floatsToBlockPos(x1, y1, z1)));
        vertex(vertexConsumer, pose, x2, y2, z2, normal, red, green, blue, 1f, 0f, getPackedLight(level, floatsToBlockPos(x2, y2, z2)));
        vertex(vertexConsumer, pose, x3, y3, z3, normal, red, green, blue, 1f, 1f, getPackedLight(level, floatsToBlockPos(x3, y3, z3)));
        vertex(vertexConsumer, pose, x4, y4, z4, normal, red, green, blue, 0f, 1f, getPackedLight(level, floatsToBlockPos(x4, y4, z4)));

//        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue, 0f, 0f, light);
//        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue, 1f, 1f, light);
    }

    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, double x, double y, double z, float[] normal, float red, float green, float blue, float u, float v, int light) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, 1.0F) // Color
                .uv(u, v)               // Texture UV
                .overlayCoords(OverlayTexture.NO_OVERLAY) // Overlay texture
                .uv2(light)                // Lightmap texture (full brightness)
                .normal(pose.normal(), 0.0F, 1.0F, 0.0F) // Normal vector (arbitrary up)
                .endVertex();
    }

    public static void renderParabolaLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
                                     double endX, double endY, double endZ, float red, float green, float blue, float thickness, int segments, float distance, float gravityMul) {
        double dx = (endX - startX)/segments;
        double dy = (endY - startY)/segments;
        double dz = (endZ - startZ)/segments;

        double prevX = startX;
        double prevY = startY;
        double prevZ = startZ;

        for(int i=1; i<=segments; i++) {
            double s_endX = startX + dx*i;
            double s_endY = startY + dy*i + simulateGravity((distance/segments)*i, distance)*gravityMul;
            double s_endZ = startZ + dz*i;

            double midX = (prevX + s_endX)/2;
            double midY = (prevY + s_endY)/2;
            midY += simulateGravity((distance/segments)*(i - 0.5f), distance)*gravityMul;
            double midZ = (prevZ + s_endZ)/2;

            renderLine(vertexConsumer, pose, prevX, prevY, prevZ, s_endX, s_endY, s_endZ, red, green, blue, thickness);

            prevX = s_endX;
            prevY = s_endY;
            prevZ = s_endZ;
        }
    }

    private static float[] calculateNormal(float x1, float y1, float z1,
                                           float x2, float y2, float z2,
                                           float x3, float y3, float z3) {
        // Calculate two vectors on the plane of the quad
        float[] vector1 = new float[]{x2 - x1, y2 - y1, z2 - z1};
        float[] vector2 = new float[]{x3 - x1, y3 - y1, z3 - z1};

        // Compute cross product
        float[] normal = cross(vector1, vector2);
        normalize(normal); // Normalize to unit length
        return normal;
    }

    private static double simulateGravity(float x, float distance) {
        double midpoint = distance/2;

        return ((double)x-midpoint)*((double)x-midpoint) * (1/(midpoint*midpoint)) - 1;
    }

    public static void renderParabolaTube(VertexConsumer vertexConsumer, PoseStack.Pose pose, int light, double startX, double startY, double startZ,
                                          double endX, double endY, double endZ, float red, float green, float blue, float thickness,
                                          int segments, float distance, float gravityMul) {
        double dx = (endX - startX) / segments;
        double dy = (endY - startY) / segments;
        double dz = (endZ - startZ) / segments;

        double prevX = startX;
        double prevY = startY;
        double prevZ = startZ;

        // Calculate initial perpendicular vectors
        float[] normal = {0, 1, 0}; // Arbitrary up vector for initial perpendicular calculation

        for (int i = 1; i <= segments; i++) {
            double s_endX = startX + dx * i;
            double s_endY = startY + dy * i + simulateGravity((distance / segments) * i, distance) * gravityMul;
            double s_endZ = startZ + dz * i;

            double midX = (prevX + s_endX) / 2;
            double midY = (prevY + s_endY) / 2;
            midY += simulateGravity((distance / segments) * (i - 0.5f), distance) * gravityMul;
            double midZ = (prevZ + s_endZ) / 2;

            // Calculate direction of segment
            float[] direction = {(float) (s_endX - prevX), (float) (s_endY - prevY), (float) (s_endZ - prevZ)};
            normalize(direction);

            // Compute perpendicular vectors
            float[] perp1 = cross(normal, direction);
            float[] perp2 = cross(direction, perp1);
            normalize(perp1);
            normalize(perp2);

            // Scale by thickness
            scale(perp1, thickness / 2);
            scale(perp2, thickness / 2);

            // Define corners of the square tube at start and end of the segment
            float[][] startCorners = new float[4][3];
            float[][] endCorners = new float[4][3];

            // Calculate corner positions
            calculateCorners(prevX, prevY, prevZ, perp1, perp2, startCorners);
            calculateCorners(s_endX, s_endY, s_endZ, perp1, perp2, endCorners);

            // Render quads between the corners
            for (int j = 0; j < 4; j++) {
                int next = (j + 1) % 4;

                renderQuad(vertexConsumer, pose,
                        startCorners[j][0], startCorners[j][1], startCorners[j][2],
                        startCorners[next][0], startCorners[next][1], startCorners[next][2],
                        endCorners[next][0], endCorners[next][1], endCorners[next][2],
                        endCorners[j][0], endCorners[j][1], endCorners[j][2],
                        red, green, blue, light);
            }

            // Update previous segment endpoint
            prevX = s_endX;
            prevY = s_endY;
            prevZ = s_endZ;
        }
    }

    // Helper functions
    private static void normalize(float[] vector) {
        float length = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        if (length != 0) {
            vector[0] /= length;
            vector[1] /= length;
            vector[2] /= length;
        }
    }

    private static float[] cross(float[] a, float[] b) {
        return new float[]{
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }

    private static void scale(float[] vector, float scalar) {
        vector[0] *= scalar;
        vector[1] *= scalar;
        vector[2] *= scalar;
    }

    private static void calculateCorners(double x, double y, double z, float[] perp1, float[] perp2, float[][] corners) {
        corners[0] = new float[]{(float) (x + perp1[0] + perp2[0]), (float) (y + perp1[1] + perp2[1]), (float) (z + perp1[2] + perp2[2])};
        corners[1] = new float[]{(float) (x - perp1[0] + perp2[0]), (float) (y - perp1[1] + perp2[1]), (float) (z - perp1[2] + perp2[2])};
        corners[2] = new float[]{(float) (x - perp1[0] - perp2[0]), (float) (y - perp1[1] - perp2[1]), (float) (z - perp1[2] - perp2[2])};
        corners[3] = new float[]{(float) (x + perp1[0] - perp2[0]), (float) (y + perp1[1] - perp2[1]), (float) (z + perp1[2] - perp2[2])};
    }

    private static int getPackedLight(Level world, BlockPos pos) {
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos); // Block light level
        int skyLight = world.getBrightness(LightLayer.SKY, pos);    // Sky light level
        return LightTexture.pack(blockLight, skyLight);
    }

    private static BlockPos floatsToBlockPos(float x, float y, float z) {
        int bx = Mth.floor(x);
        int by = Mth.floor(y);
        int bz = Mth.floor(z);

        return new BlockPos(bx, by, bz);
    }

    public static float[] RGBtoHSB(float r, float g, float b) {
        int ir = Mth.floor(r*255);
        int ig = Mth.floor(g*255);
        int ib = Mth.floor(b*255);
        return Color.RGBtoHSB(ir, ig, ib, null);
    }

    public static float[] HSBtoRGB(float h, float s, float b) {
        int rgb = Color.HSBtoRGB(h, s, b);

        int red = (rgb>>16)&0xFF;

        int green = (rgb>>8)&0xFF;

        int blue = rgb&0xFF;

        float _r = (float) red / 255f;
        float _g = (float) green / 255f;
        float _b = (float) blue / 255f;

        return new float[]{_r, _g, _b};
    }

    public static void shake(PoseStack pPoseStack, int shakeTimer, float freq, float power) {
        float shakeyX = (float) Math.sin(shakeTimer * freq) * power;
        float shakeyY = (float) Math.cos(shakeTimer * freq) * power;
        pPoseStack.translate(shakeyX, 0, shakeyY);
    }
}
