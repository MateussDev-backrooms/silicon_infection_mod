package net.mateuss.chemosynthesis.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.EntityTethCow;
import net.mateuss.chemosynthesis.entity.custom.EntityTethZombie;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTethCow extends MobRenderer<EntityTethCow, ModelTethCow<EntityTethCow>> {
    public RendererTethCow(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelTethCow<>(pContext.bakeLayer(ModModelLayers.TETH_COW_LAYER)), 0.6f);
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
