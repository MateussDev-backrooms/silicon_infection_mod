package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationHarpoon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class MutationHarpoon_Renderer extends GeoObjectRenderer<MutationHarpoon> {
    private MultiBufferSource currentBuffer;

    public MutationHarpoon_Renderer(GeoModel<MutationHarpoon> model) {
        super(model);
    }

    @Override
    public GeoModel<MutationHarpoon> getGeoModel() {
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MutationHarpoon animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }
}
