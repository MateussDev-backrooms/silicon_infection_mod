package net.mateuss.chemosynthesis.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.custom.EntitySiliconTripod;
import net.mateuss.chemosynthesis.entity.custom.EntityTethZombie;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererTethZombie extends MobRenderer<EntityTethZombie, ModelTethZombie<EntityTethZombie>> {
    public RendererTethZombie(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelTethZombie<>(pContext.bakeLayer(ModModelLayers.TETH_ZOMBIE_LAYER)), 0.6f);
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
