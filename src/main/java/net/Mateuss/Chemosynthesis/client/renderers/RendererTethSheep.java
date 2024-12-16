package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelTethSheep;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethSheep;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTethSheep extends MobRenderer<EntityTethSheep, ModelTethSheep<EntityTethSheep>> {
    public RendererTethSheep(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelTethSheep<>(pContext.bakeLayer(GeneralModModelLayers.TETH_SHEEP_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTethSheep entityTethSheep) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/teth_sheep.png");
    }

    @Override
    public void render(EntityTethSheep pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
