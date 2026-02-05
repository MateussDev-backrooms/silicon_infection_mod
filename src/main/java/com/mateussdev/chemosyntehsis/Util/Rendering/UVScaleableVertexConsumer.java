package com.mateussdev.chemosyntehsis.Util.Rendering;

import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;

public class UVScaleableVertexConsumer implements VertexConsumer {
    private final VertexConsumer wrapped;
    private float uScale, vScale;

    public UVScaleableVertexConsumer(VertexConsumer wrapped, float uScale, float vScale) {
        this.wrapped = wrapped;
        this.uScale = uScale;
        this.vScale = vScale;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return wrapped.vertex(x, y, z);
    }

    @Override
    public VertexConsumer normal(float nx, float ny, float nz) {
        return wrapped.normal(nx, ny, nz);
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return wrapped.uv(u*uScale, v*vScale);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
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
