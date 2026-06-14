package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class MutationBurrowing_Renderer extends GeoObjectRenderer<MutationBurrowing> {
    private MultiBufferSource currentBuffer;

    public MutationBurrowing_Renderer(GeoModel<MutationBurrowing> model) {
        super(model);
    }

    @Override
    public GeoModel<MutationBurrowing> getGeoModel() {
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MutationBurrowing animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }
}
