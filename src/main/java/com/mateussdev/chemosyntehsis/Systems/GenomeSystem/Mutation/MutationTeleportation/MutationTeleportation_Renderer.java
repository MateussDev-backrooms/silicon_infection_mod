package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationTeleportation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class MutationTeleportation_Renderer extends GeoObjectRenderer<MutationTeleportation> {
    private MultiBufferSource currentBuffer;

    public MutationTeleportation_Renderer(GeoModel<MutationTeleportation> model) {
        super(model);
    }

    @Override
    public GeoModel<MutationTeleportation> getGeoModel() {
        return model;
    }

    @Override
    public void render(PoseStack poseStack, MutationTeleportation animatable, @Nullable MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, int packedLight) {
        super.render(poseStack, animatable, bufferSource, renderType, buffer, packedLight);
    }
}
