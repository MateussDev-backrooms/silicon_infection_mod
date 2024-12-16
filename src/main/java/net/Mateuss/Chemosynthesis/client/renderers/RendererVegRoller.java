package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelVegRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.EntityVegRoller;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererVegRoller extends MobRenderer<EntityVegRoller, ModelVegRoller<EntityVegRoller>> {
    public RendererVegRoller(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelVegRoller<>(pContext.bakeLayer(GeneralModModelLayers.VEG_ROLLER_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityVegRoller EntityVegRoller) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/veg_roller.png");
    }

    @Override
    public void render(EntityVegRoller pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
