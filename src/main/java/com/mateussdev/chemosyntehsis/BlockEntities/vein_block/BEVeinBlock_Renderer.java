package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.azure.azurelib.renderer.GeoBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BEVeinBlock_Renderer extends GeoBlockRenderer<BEVeinBlock> {
    public BEVeinBlock_Renderer(BlockEntityRendererProvider.Context context) {
        super(new BEVeinBlock_Model());
    }

    @Override
    public void render(BEVeinBlock animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
