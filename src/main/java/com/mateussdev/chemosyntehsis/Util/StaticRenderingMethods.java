package com.mateussdev.chemosyntehsis.Util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class StaticRenderingMethods {

    // Full-featured cube rendering
    public static void renderDebugCube(VertexConsumer vertexConsumer, PoseStack poseStack, PoseStack.Pose pose,
                                       float x, float y, float z,
                                       float width, float height, float depth,
                                       float red, float green, float blue, float alpha,
                                       int packedLight) {
        poseStack.pushPose();

        renderQuad(vertexConsumer, pose,
                x, y, z,
                x + width, y, z,
                x + width, y + height, z,
                x, y + height, z,
                red, green, blue, packedLight);

        renderQuad(vertexConsumer, pose,
                x, y, z + depth,
                x, y, z,
                x, y + height, z,
                x, y + height, z + depth,
                red, green, blue, packedLight);

        renderQuad(vertexConsumer, pose,
                x + width, y, z + depth,
                x, y, z + depth,
                x, y + height, z + depth,
                x + width, y + height, z + depth,
                red, green, blue, packedLight);

        renderQuad(vertexConsumer, pose,
                x, y + height, z,
                x + width, y + height, z,
                x + width, y + height, z + depth,
                x, y + height, z + depth,
                red, green, blue, packedLight);

        renderQuad(vertexConsumer, pose,
                x + width, y, z,
                x + width, y, z + depth,
                x + width, y + height, z + depth,
                x + width, y + height, z,
                red, green, blue, packedLight);

        renderQuad(vertexConsumer, pose,
                x, y, z + depth,
                x + width, y, z + depth,
                x + width, y, z,
                x, y, z,
                red, green, blue, packedLight);

        poseStack.popPose();
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


    // ===== RENDERING ===== //

    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, double x, double y, double z, double nx, double ny, double nz, float red, float green, float blue, float alpha, float u, float v, int light) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, alpha)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(pose.normal(), (float) nx, (float) ny, (float) nz)
                .endVertex();
    }


    public static void renderQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                  float x1, float y1, float z1, float x2, float y2, float z2,
                                  float x3, float y3, float z3, float x4, float y4, float z4,
                                  float red, float green, float blue, int light) {
        float[] normal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);

        vertex(vertexConsumer, pose, x1, y1, z1, normal[0], normal[1], normal[2], red, green, blue, 1f, 0f, 0f, light);
        vertex(vertexConsumer, pose, x2, y2, z2, normal[0], normal[1], normal[2], red, green, blue, 1f, 1f, 0f, light);
        vertex(vertexConsumer, pose, x3, y3, z3, normal[0], normal[1], normal[2], red, green, blue, 1f, 1f, 1f, light);
        vertex(vertexConsumer, pose, x4, y4, z4, normal[0], normal[1], normal[2], red, green, blue, 1f, 0f, 1f, light);

    }

    public static void renderLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
                                  double endX, double endY, double endZ, float red, float green, float blue, float thickness) {
        float dx = (float) (endX - startX);
        float dy = (float) (endY - startY);
        float dz = (float) (endZ - startZ);

        float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length == 0) return;
        dx /= length;
        dy /= length;
        dz /= length;

        float offsetX = -dz * thickness / 2;
        float offsetZ = dx * thickness / 2;

        float startX1 = (float) startX + offsetX;
        float startZ1 = (float) startZ + offsetZ;
        float startX2 = (float) startX - offsetX;
        float startZ2 = (float) startZ - offsetZ;

        float endX1 = (float) endX + offsetX;
        float endZ1 = (float) endZ + offsetZ;
        float endX2 = (float) endX - offsetX;
        float endZ2 = (float) endZ - offsetZ;

        renderQuad(vertexConsumer, pose, startX1, (float) startY, startZ1, endX1, (float) endY, endZ1, endX2, (float) endY, endZ2, startX2, (float) startY, startZ2, red, green, blue, 15728880);
    }

    private static void renderQuadSegment(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                          Vec3[] startCorners, Vec3[] endCorners,
                                          float r, float g, float b, int packedLight,
                                          float uStart, float uEnd) {
        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;

            Vec3 v1 = startCorners[i];
            Vec3 v2 = startCorners[next];
            Vec3 v3 = endCorners[next];
            Vec3 v4 = endCorners[i];

            // Calculate normal
            Vec3 edge1 = v2.subtract(v1);
            Vec3 edge2 = v3.subtract(v1);
            Vec3 normal = edge1.cross(edge2).normalize();

            // Render the quad with proper UVs to avoid stretching
            vertex(vertexConsumer, pose, v1.x, v1.y, v1.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uStart, 0, packedLight);
            vertex(vertexConsumer, pose, v2.x, v2.y, v2.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uStart, 1, packedLight);
            vertex(vertexConsumer, pose, v3.x, v3.y, v3.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uEnd, 1, packedLight);
            vertex(vertexConsumer, pose, v4.x, v4.y, v4.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uEnd, 0, packedLight);
        }
    }

    private static double simulateGravity(float x, float distance) {
        double midpoint = distance;

        return ((double)x-midpoint)*((double)x-midpoint) * (1/(midpoint*midpoint)) - 1;
    }

    public static void renderParabolaTube(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                          Vec3 start, Vec3 end, float r, float g, float b,
                                          float thickness, float distance, int segments, int packedLight, float gravityMul) {
        // Direction vector
        Vec3 dir = end.subtract(start);
        double length = dir.length();
        dir = dir.normalize();

        // Calculate perpendicular vectors for tube cross-section
        Vec3 up = Math.abs(dir.y) > 0.9 ? new Vec3(0, 0, 1) : new Vec3(0, 1, 0);
        Vec3 right = dir.cross(up).normalize().scale(thickness / 2);
        up = right.cross(dir).normalize().scale(thickness / 2);

        // Generate points along the beam
        Vec3[] points = new Vec3[segments + 1];
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            // Add slight curve for visual appeal
            double curve = simulateGravity((distance/segments)*i, distance)*gravityMul;
            points[i] = start.add(dir.scale(t * length))
                    .add(up.scale(curve));
        }

        // Render tube segments with overlapping ends to prevent gaps
        for (int i = 0; i < segments; i++) {
            Vec3 current = points[i];
            Vec3 next = points[i + 1];

            // Get perpendicular vectors at current point
            Vec3 segDir = next.subtract(current).normalize();
            Vec3 segRight = segDir.cross(up).normalize().scale(thickness / 2);
            Vec3 segUp = segRight.cross(segDir).normalize().scale(thickness / 2);

            // Calculate corner positions
            Vec3[] startCorners = {
                    current.add(segRight).add(segUp),
                    current.add(segRight).subtract(segUp),
                    current.subtract(segRight).subtract(segUp),
                    current.subtract(segRight).add(segUp)
            };

            // Get perpendicular vectors at next point
            Vec3 nextSegDir = (i + 2 <= segments) ?
                    points[i + 2].subtract(next).normalize() : segDir;
            Vec3 nextRight = nextSegDir.cross(up).normalize().scale(thickness / 2);
            Vec3 nextUp = nextRight.cross(nextSegDir).normalize().scale(thickness / 2);

            Vec3[] endCorners = {
                    next.add(nextRight).add(nextUp),
                    next.add(nextRight).subtract(nextUp),
                    next.subtract(nextRight).subtract(nextUp),
                    next.subtract(nextRight).add(nextUp)
            };

            // Render the quad segment
            renderQuadSegment(vertexConsumer, pose,
                    startCorners, endCorners,
                    r, g, b, packedLight,
                    (float) i / (segments*0.25f), (float) (i + 1) / (segments*0.25f));
        }
    }

    public static void renderText(PoseStack pPoseStack, MultiBufferSource pBuffer, String pText, double pX, double pY, double pZ, int pColor, float pScale, boolean p_270731_, float p_270825_, boolean pTransparent) {
        Minecraft $$11 = Minecraft.getInstance();
        Camera $$12 = $$11.gameRenderer.getMainCamera();
        if ($$12.isInitialized() && $$11.getEntityRenderDispatcher().options != null) {
            Font $$13 = $$11.font;
            double $$14 = $$12.getPosition().x;
            double $$15 = $$12.getPosition().y;
            double $$16 = $$12.getPosition().z;
            pPoseStack.pushPose();
            pPoseStack.translate((float)(pX - $$14), (float)(pY - $$15) + 0.07F, (float)(pZ - $$16));
            pPoseStack.mulPoseMatrix((new Matrix4f()).rotation($$12.rotation()));
            pPoseStack.scale(-pScale, -pScale, pScale);
            float $$17 = p_270731_ ? (float)(-$$13.width(pText)) / 2.0F : 0.0F;
            $$17 -= p_270825_ / pScale;
            $$13.drawInBatch(pText, $$17, 0.0F, pColor, false, pPoseStack.last().pose(), pBuffer, pTransparent ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, 15728880);
            pPoseStack.popPose();
        }
    }

    // ===== Rendering Maths ===== //

    private static float[] calculateNormal(float x1, float y1, float z1,
                                           float x2, float y2, float z2,
                                           float x3, float y3, float z3) {
        float[] edge1 = {x2 - x1, y2 - y1, z2 - z1};
        float[] edge2 = {x3 - x1, y3 - y1, z3 - z1};

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

    private static void scale(float[] vector, float scalar) {
        vector[0] *= scalar;
        vector[1] *= scalar;
        vector[2] *= scalar;
    }

    public static Vector3f transformTangentToLocal(Vector3f tangentSpaceVec, Vector3f normal) {
        Vector3f tangent = new Vector3f(0, 1, 0); // Default up vector

        // If normal is nearly vertical, use a different basis
        if (Math.abs(normal.dot(tangent)) > 0.999f) {
            tangent = new Vector3f(1, 0, 0); // Use forward instead
        }

        // Make tangent orthogonal to normal
        tangent.sub(new Vector3f(normal).mul(normal.dot(tangent)));
        tangent.normalize();

        Vector3f bitangent = new Vector3f(normal).cross(tangent);

        Vector3f localVec = new Vector3f(tangent).mul(tangentSpaceVec.x);
        localVec.add(new Vector3f(bitangent).mul(tangentSpaceVec.y));
        localVec.add(new Vector3f(normal).mul(tangentSpaceVec.z));

        return localVec;
    }

    public static Vector3f vectorLerp(float aX, float aY, float aZ, float bX, float bY, float bZ, float alphaX, float alphaY, float alphaZ) {
        return new Vector3f(
                aX * (1 - alphaX) + bX * alphaX,
                aY * (1 - alphaY) + bY * alphaY,
                aZ * (1 - alphaZ) + bZ * alphaZ
        );
    }

    public static Vector3f cheapContrast(float inX, float inY, float inZ, float contrast) {
        float v1 = 0-contrast;
        float v2 = contrast+1;

        return vectorLerp(v1, v1, v1, v2, v2, v2, inX, inY, inZ);
    }
}
