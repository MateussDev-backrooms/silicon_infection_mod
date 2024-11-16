package net.mateuss.chemosynthesis.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.EntitySiliconTripod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererSiliconTripod extends MobRenderer<EntitySiliconTripod, ModelSiliconTripod<EntitySiliconTripod>> {
    public RendererSiliconTripod(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelSiliconTripod<>(pContext.bakeLayer(ModModelLayers.SILICON_TRIPOD_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySiliconTripod entitySiliconTripod) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/silicon_roller.png");
    }

    @Override
    public void render(EntitySiliconTripod pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
