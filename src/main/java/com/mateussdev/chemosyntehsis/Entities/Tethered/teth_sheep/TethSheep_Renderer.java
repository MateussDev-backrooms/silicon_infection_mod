package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_sheep;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing.MutationBurrowing;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TethSheep_Renderer extends GeoEntityRenderer<TethSheep> {
    public TethSheep_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethSheep_Model());
    }
    @Override
    public void render(TethSheep entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Apply burrow offset if the entity has the mutation
        if (entity.hasMutationType(MutationBurrowing.TYPE_ID)) {
            float offset = MutationBurrowing.getBurrowRenderOffset(entity, MutationBurrowing.TYPE_ID);
            poseStack.translate(0, offset / 16f, 0) ;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
