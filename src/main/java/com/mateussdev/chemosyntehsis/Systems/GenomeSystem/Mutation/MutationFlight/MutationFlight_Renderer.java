package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoObjectRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MutationFlight_Renderer extends GeoObjectRenderer<MutationFlight> {
    private MultiBufferSource currentBuffer;

    public MutationFlight_Renderer(GeoModel<MutationFlight> model) {
        super(model);
    }

    @Override
    public GeoModel<MutationFlight> getGeoModel() {
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MutationFlight animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }
}
