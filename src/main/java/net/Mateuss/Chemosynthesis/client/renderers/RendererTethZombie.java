package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.ModelTethZombie;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethZombie;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTethZombie extends MobRenderer<EntityTethZombie, ModelTethZombie<EntityTethZombie>> {
    public RendererTethZombie(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelTethZombie<>(pContext.bakeLayer(GeneralModModelLayers.TETH_ZOMBIE_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTethZombie entityTethZombie) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/teth_zombie.png");
    }

    @Override
    public void render(EntityTethZombie pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {


        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
