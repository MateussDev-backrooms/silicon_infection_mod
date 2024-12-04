package net.mateuss.chemosynthesis.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.EntityBrachaticStage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererBrachaticStage extends MobRenderer<EntityBrachaticStage, ModelBrachaticStage<EntityBrachaticStage>> {
    public RendererBrachaticStage(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelBrachaticStage<>(pContext.bakeLayer(ModModelLayers.BRACHATIC_STAGE)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBrachaticStage entityBrachaticStage) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/brachatic_stage.png");
    }

    @Override
    public void render(EntityBrachaticStage pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
