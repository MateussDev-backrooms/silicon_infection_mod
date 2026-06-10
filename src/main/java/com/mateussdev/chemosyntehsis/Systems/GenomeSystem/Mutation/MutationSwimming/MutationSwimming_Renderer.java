package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSwimming;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class MutationSwimming_Renderer extends GeoObjectRenderer<MutationSwimming> {
    private MultiBufferSource currentBuffer;

    public MutationSwimming_Renderer(GeoModel<MutationSwimming> model) {
        super(model);
    }

    @Override
    public GeoModel<MutationSwimming> getGeoModel() {
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MutationSwimming animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }
}
