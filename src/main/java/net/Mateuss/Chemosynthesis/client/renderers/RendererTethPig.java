package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelTethPig;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethPig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTethPig extends MobRenderer<EntityTethPig, ModelTethPig<EntityTethPig>> {
    public RendererTethPig(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelTethPig<>(pContext.bakeLayer(GeneralModModelLayers.TETH_PIG_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTethPig entityTethPig) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/teth_pig.png");
    }

    @Override
    public void render(EntityTethPig pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
