package com.mateussdev.chemosyntehsis.Util.Rendering;

import com.google.common.cache.Cache;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class TextureUtilities {

    private static final Map<ResourceLocation, Integer> TEXTURE_WIDTH_CACHE = new HashMap<>();
    private static final Map<ResourceLocation, Integer> TEXTURE_HEIGHT_CACHE = new HashMap<>();

    public static float getUvWidthScaleForTexture(ResourceLocation textureLocation, float baseUvScale, int baseTextureWidth) {
        int textureWidth = getTextureWidth(textureLocation, baseTextureWidth);
        return calculateUvScale(textureWidth, baseUvScale, baseTextureWidth);
    }
    public static float getUvHeightScaleForTexture(ResourceLocation textureLocation, float baseUvScale, int baseTextureHeight) {
        int textureHeight = getTextureHeight(textureLocation, baseTextureHeight);
        return calculateUvScale(textureHeight, baseUvScale, baseTextureHeight);
    }
    private static int getTextureWidth(ResourceLocation textureLocation, int defaultWidth) {
        return TEXTURE_WIDTH_CACHE.computeIfAbsent(textureLocation, loc -> {
            try {
                Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(loc);
                try (InputStream inputStream = resource.open();
                     NativeImage image = NativeImage.read(inputStream)) {
                    return image.getWidth();
                }
            } catch (IOException e) {
                return defaultWidth;
            }
        });
    }
    private static int getTextureHeight(ResourceLocation textureLocation, int defaultHeight) {
        return TEXTURE_HEIGHT_CACHE.computeIfAbsent(textureLocation, loc -> {
            try {
                Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(loc);
                try (InputStream inputStream = resource.open();
                     NativeImage image = NativeImage.read(inputStream)) {
                    return image.getHeight();
                }
            } catch (IOException e) {
                return defaultHeight;
            }
        });
    }
    public static float calculateUvScale(int textureWidth, float baseUvScale, int baseTextureWidth) {
        if (baseTextureWidth <= 0) {
            return baseUvScale;
        }
        return baseUvScale * ((float) textureWidth / baseTextureWidth);
    }
}
