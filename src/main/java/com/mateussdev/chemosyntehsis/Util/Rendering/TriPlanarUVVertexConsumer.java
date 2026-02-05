package com.mateussdev.chemosyntehsis.Util.Rendering;

import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TriPlanarUVVertexConsumer implements VertexConsumer {
    private final VertexConsumer wrapped;
    private final double entityX, entityY, entityZ;
    private double lastX, lastY, lastZ;
    private float lastNX, lastNY, lastNZ;
    private float lastU, lastV;
    private final float textureScale = 1.0f * 16.0f;
    private final boolean displayUVs;

    public TriPlanarUVVertexConsumer(VertexConsumer wrapped, double entityX, double entityY, double entityZ, boolean displayUV) {
        this.wrapped = wrapped;
        this.entityX = entityX;
        this.entityY = entityY;
        this.entityZ = entityZ;
        this.displayUVs = displayUV;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if(camera.isInitialized()) {
            this.lastX = x + (camera.getPosition().x - (entityX));
            this.lastY = y + (camera.getPosition().y - (entityY));
            this.lastZ = z + (camera.getPosition().z - (entityZ));
        }
//        StaticSiliconiteMethods.debugLog("x: "+x+" y: "+y+" z: "+z);
        return wrapped.vertex(x, y, z);
    }

    @Override
    public VertexConsumer normal(float nx, float ny, float nz) {
        this.lastNX = nx;
        this.lastNY = ny;
        this.lastNZ = nz;
        return wrapped.normal(nx, ny, nz);
    }

    @Override
    public VertexConsumer uv(float u, float v) {

        //Divide positions
//        float div = 1024f;
//
//        float v1X = (float) ((lastX-entityX) / div);
//        float v1Y = (float) ((lastY-entityY) / div);
//        float v1Z = (float) ((lastZ-entityZ) / div);
//
//        Vector3f localTangentAbs = new Vector3f(lastNX, lastNY, lastNZ).mul(1f).absolute();
//        double exp = 160;
//
//        float v2X = (float) Math.pow(localTangentAbs.x, exp);
//        float v2Y = (float) Math.pow(localTangentAbs.y, exp);
//        float v2Z = (float) Math.pow(localTangentAbs.z, exp);
//
//        float sum = v2X + v2Y + v2Z;
//
//        float v3X = v2X / sum;
//        float v3Y = v2Y / sum;
//        float v3Z = v2Z / sum;
//
//        Vector3f contrV = StaticRenderingMethods.cheapContrast(v3X, v3Y, v3Z, 512);
//
//        Vector2f proj1 = new Vector2f(v1X, v1Y).mul(contrV.z);
//        Vector2f proj2 = new Vector2f(v1Y, v1Z).mul(contrV.x);
//        Vector2f proj3 = new Vector2f(v1X, v1Z).mul(contrV.y);
//
//        Vector2f finalUV = proj1.add(proj2).add(proj3);


        float finalU = (float) (u);
        float finalV = (float) (v);



        lastU = finalU;
        lastV = finalV;
        return wrapped.uv(finalU, finalV);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        if(displayUVs){
            StaticSiliconiteMethods.debugLog("u: "+lastU+" v:"+lastV);
            return wrapped.color(lastU, lastV, blue, 0.5f);
        }
        return wrapped.color(red, green, blue, alpha);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return wrapped.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        return wrapped.uv2(u, v);
    }

    @Override
    public void endVertex() {
        wrapped.endVertex();
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        wrapped.defaultColor(r, g, b, a);
    }

    @Override
    public void unsetDefaultColor() {
        wrapped.unsetDefaultColor();
    }
}
