package net.Mateuss.Chemosynthesis.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.GeneralRenderingFunctions;
import net.Mateuss.Chemosynthesis.client.models.ModelSiliconRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconRoller;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RendererSiliconRoller extends MobRenderer<EntitySiliconRoller, ModelSiliconRoller<EntitySiliconRoller>> {
    public RendererSiliconRoller(EntityRendererProvider.Context pContext) {
        super(pContext, new ModelSiliconRoller<>(pContext.bakeLayer(GeneralModModelLayers.SILICON_ROLLER_LAYER)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySiliconRoller entitySiliconRoller) {
        return new ResourceLocation(Chemosynthesis.MODID, "textures/entity/silicon_roller.png");
    }

    @Override
    public void render(EntitySiliconRoller pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {

        if(pEntity.isOnFire()) {
            float shakeyX = (float) Math.sin(pEntity.shakeTimer * 10) * 0.1f;
            float shakeyY = (float) Math.cos(pEntity.shakeTimer * 10) * 0.1f;
            pPoseStack.translate(shakeyX, 0, shakeyY);
        }

        //small effect for when a bulb jerks
        if(pEntity.bulb_jerk_timer > 0) {
            GeneralRenderingFunctions.shake(pPoseStack, pEntity.bulb_jerk_timer, 3, 0.5f);
        }
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
