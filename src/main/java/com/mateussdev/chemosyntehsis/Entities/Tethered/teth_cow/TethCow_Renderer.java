package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow;

import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton.TethSkeleton;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing.MutationBurrowing;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethCow_Renderer extends GeoEntityRenderer<TethCow> {
    public TethCow_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethCow_Model());
    }
    @Override
    public void render(TethCow entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Apply burrow offset if the entity has the mutation
        if (entity.hasMutationType(MutationBurrowing.TYPE_ID)) {
            float offset = MutationBurrowing.getBurrowRenderOffset(entity, MutationBurrowing.TYPE_ID);
            poseStack.translate(0, offset / 16f, 0) ;
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
