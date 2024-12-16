package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelSiliconTripod;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererSiliconTripod extends MobRenderer<EntitySiliconTripod, ModelSiliconTripod<EntitySiliconTripod>> {
    public RendererSiliconTripod(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelSiliconTripod<>(pContext.bakeLayer(GeneralModModelLayers.SILICON_TRIPOD_LAYER)), 0.6f);
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
