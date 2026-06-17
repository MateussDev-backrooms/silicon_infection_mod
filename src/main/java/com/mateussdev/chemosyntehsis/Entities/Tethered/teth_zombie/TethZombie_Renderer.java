package com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie;

import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBurrowing.MutationBurrowing;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationFlight.MutationFlight_Layer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TethZombie_Renderer extends GeoEntityRenderer<TethZombie> {
    public TethZombie_Renderer(EntityRendererProvider.Context context) {
        super(context, new TethZombie_Model());
    }

    //grrr I gotta add this so that it works per instance

    @Override
    public void render(TethZombie entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        // Apply burrow offset if the entity has the mutation
        if (entity.hasMutationType(MutationBurrowing.TYPE_ID)) {
            float offset = MutationBurrowing.getBurrowRenderOffset(entity, MutationBurrowing.TYPE_ID);
            poseStack.translate(0, offset / 16f, 0); // 1 block = 16 model units (adjust if needed)
        }
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
