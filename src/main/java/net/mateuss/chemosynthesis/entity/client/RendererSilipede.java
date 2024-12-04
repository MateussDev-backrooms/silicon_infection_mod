package net.mateuss.chemosynthesis.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.EntitySilipede;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererSilipede extends MobRenderer<EntitySilipede, ModelSilipede<EntitySilipede>> {
    public RendererSilipede(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelSilipede<>(pContext.bakeLayer(ModModelLayers.SILIPEDE_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySilipede entitySilipede) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/silipede.png");
    }

    @Override
    public void render(EntitySilipede pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
