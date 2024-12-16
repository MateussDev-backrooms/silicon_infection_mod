package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelHomunculus;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomunculus;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererHomunculus extends MobRenderer<EntityHomunculus, ModelHomunculus<EntityHomunculus>> {
    public RendererHomunculus(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelHomunculus<>(pContext.bakeLayer(GeneralModModelLayers.HOMUNCULUS_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHomunculus entityHomunculus) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/homunculus.png");
    }

    @Override
    public void render(EntityHomunculus pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
