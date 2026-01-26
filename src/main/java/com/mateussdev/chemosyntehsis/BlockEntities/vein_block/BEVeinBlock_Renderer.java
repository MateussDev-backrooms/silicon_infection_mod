package com.mateussdev.chemosyntehsis.BlockEntities.vein_block;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BEVeinBlock_Renderer implements BlockEntityRenderer<BEVeinBlock> {
    public BEVeinBlock_Renderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BEVeinBlock blockEntity, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {

        for (BlockPos connectedPos : blockEntity.getConnectedBlocks()) {
            renderConnection(blockEntity, connectedPos, poseStack, bufferSource);
        }
    }

    private void renderConnection(BEVeinBlock source, BlockPos target,
                                  PoseStack poseStack, MultiBufferSource bufferSource) {
        Vec3 sourcePos = Vec3.atCenterOf(source.getBlockPos());
        Vec3 targetPos = Vec3.atCenterOf(target);

        Vec3 relPos = targetPos.subtract(sourcePos);

        VertexConsumer consumer = bufferSource.getBuffer(
                    RenderType.debugQuads()
//                    RenderType.entityTranslucentCull(new ResourceLocation(Chemosynthesis.MODID, "textures/block/debug_cube.png"))
            );

        StaticSiliconiteMethods.renderLine(consumer, poseStack.last(), 0, 0, 0, relPos.x, relPos.y, relPos.z, 1f, 1f, 1f, 1f);
    }


}
