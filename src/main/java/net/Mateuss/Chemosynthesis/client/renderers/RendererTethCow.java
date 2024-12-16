package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelTethCow;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethCow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTethCow extends MobRenderer<EntityTethCow, ModelTethCow<EntityTethCow>> {
    public RendererTethCow(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelTethCow<>(pContext.bakeLayer(GeneralModModelLayers.TETH_COW_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTethCow entityTethCow) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/teth_cow.png");
    }

    @Override
    public void render(EntityTethCow pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
