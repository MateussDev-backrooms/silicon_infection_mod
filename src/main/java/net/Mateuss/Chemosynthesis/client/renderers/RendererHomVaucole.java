package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelHomVaucole;
import net.Mateuss.Chemosynthesis.client.models.ModelHomunculus;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomVaucole;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererHomVaucole extends MobRenderer<EntityHomVaucole, ModelHomVaucole<EntityHomVaucole>> {
    public RendererHomVaucole(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelHomVaucole<>(pContext.bakeLayer(GeneralModModelLayers.HOMUNCULUS_VAUCOLE_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHomVaucole entityHomVaucole) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/homunculus_vaucole.png");
    }

    @Override
    public void render(EntityHomVaucole pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
