package com.mateussdev.chemosyntehsis.Util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;

public class StaticRenderingMethods {
    // Full-featured cube rendering
    public static void renderDebugCube(VertexConsumer vertexConsumer, PoseStack poseStack, PoseStack.Pose pose,
                                       float x, float y, float z,
                                       float width, float height, float depth,
                                       float red, float green, float blue, float alpha,
                                       int packedLight) {


        poseStack.pushPose();
        // Front face (facing -Z direction, outward normal is (0,0,-1))
        addQuad(vertexConsumer, pose,
                x, y, z,
                x + width, y, z,
                x + width, y + height, z,
                x, y + height, z,
                0, 0, -1,  // Normal pointing outward (-Z)
                red, green, blue, alpha, packedLight
        );

        // Left face (facing -X direction, outward normal is (-1,0,0))
        addQuad(vertexConsumer, pose,
                x, y, z + depth,
                x, y, z,
                x, y + height, z,
                x, y + height, z + depth,
                -1, 0, 0,  // Normal pointing outward (-X)
                red, green, blue, alpha, packedLight
        );

        // Back face (facing +Z direction, outward normal is (0,0,1))
        addQuad(vertexConsumer, pose,
                x + width, y, z + depth,
                x, y, z + depth,
                x, y + height, z + depth,
                x + width, y + height, z + depth,
                0, 0, 1,  // Normal pointing outward (+Z)
                red, green, blue, alpha, packedLight
        );

        // Top face (facing +Y direction, outward normal is (0,1,0))
        addQuad(vertexConsumer, pose,
                x, y + height, z,
                x + width, y + height, z,
                x + width, y + height, z + depth,
                x, y + height, z + depth,
                0, 1, 0,  // Normal pointing outward (+Y)
                red, green, blue, alpha, packedLight
        );

        // Right face (facing +X direction, outward normal is (1,0,0))
        addQuad(vertexConsumer, pose,
                x + width, y, z,
                x + width, y, z + depth,
                x + width, y + height, z + depth,
                x + width, y + height, z,
                1, 0, 0,  // Normal pointing outward (+X)
                red, green, blue, alpha, packedLight
        );


        // Bottom face (facing -Y direction, outward normal is (0,-1,0))
        addQuad(vertexConsumer, pose,
                x, y, z + depth,
                x + width, y, z + depth,
                x + width, y, z,
                x, y, z,
                0, -1, 0,  // Normal pointing outward (-Y)
                red, green, blue, alpha, packedLight
        );

        poseStack.popPose();


    }

    // Add a quad (two triangles) to form a face
    private static void addQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                float x1, float y1, float z1,
                                float x2, float y2, float z2,
                                float x3, float y3, float z3,
                                float x4, float y4, float z4,
                                float nx, float ny, float nz,
                                float red, float green, float blue, float alpha,
                                int light) {

        // Calculate normal from vertices (or use precomputed)
        float[] calculatedNormal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        normalize(calculatedNormal);

        // Use the precomputed normal (should match calculated one if vertices are in correct order)
        float[] normalToUse = calculatedNormal;
        // OR force use of precomputed normal:
        // normalToUse = new float[]{nx, ny, nz};

        // Triangle 1: v1 -> v2 -> v3 (counter-clockwise)
        vertex(vertexConsumer, pose, x1, y1, z1, normalToUse[0], normalToUse[1], normalToUse[2],
                red, green, blue, alpha, 0f, 0f, light);
        vertex(vertexConsumer, pose, x2, y2, z2, normalToUse[0], normalToUse[1], normalToUse[2],
                red, green, blue, alpha, 1f, 0f, light);
        vertex(vertexConsumer, pose, x3, y3, z3, normalToUse[0], normalToUse[1], normalToUse[2],
                red, green, blue, alpha, 1f, 1f, light);
        vertex(vertexConsumer, pose, x4, y4, z4, normalToUse[0], normalToUse[1], normalToUse[2],
                red, green, blue, alpha, 0f, 1f, light);
    }

    // Add a single vertex
    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                              double x, double y, double z,
                              float nx, float ny, float nz,  // Use these normals!
                              float red, float green, float blue, float alpha,
                              float u, float v, int light) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, alpha)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(pose.normal(), nx, ny, nz)
                .endVertex();
    }

    private static float[] calculateNormal(float x1, float y1, float z1,
                                           float x2, float y2, float z2,
                                           float x3, float y3, float z3) {
        // Edge vectors: v2 - v1 and v3 - v1
        float[] edge1 = {x2 - x1, y2 - y1, z2 - z1};
        float[] edge2 = {x3 - x1, y3 - y1, z3 - z1};

        // Cross product: edge1 × edge2
        float[] normalRaw = cross(edge1, edge2);
        normalize(normalRaw);
        return normalRaw;
    }

    private static void normalize(float[] vector) {
        float length = (float) Math.sqrt(
                vector[0] * vector[0] +
                        vector[1] * vector[1] +
                        vector[2] * vector[2]
        );

        if (length > 1.0E-6f) { // Avoid division by zero
            float invLength = 1.0f / length;
            vector[0] *= invLength;
            vector[1] *= invLength;
            vector[2] *= invLength;
        }
    }

    private static float[] cross(float[] a, float[] b) {
        return new float[]{
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }
}
