package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationWebsack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class MutationWebsack_Renderer extends GeoObjectRenderer<MutationWebsack> {
    private MultiBufferSource currentBuffer;

    public MutationWebsack_Renderer(GeoModel<MutationWebsack> model) {
        super(model);
    }

    @Override
    public GeoModel<MutationWebsack> getGeoModel() {
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MutationWebsack animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }
}
