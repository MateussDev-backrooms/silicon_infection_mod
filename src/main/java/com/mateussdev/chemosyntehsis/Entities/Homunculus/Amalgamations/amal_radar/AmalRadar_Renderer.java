package com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_radar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AmalRadar_Renderer extends GeoEntityRenderer<AmalRadar> {
    public AmalRadar_Renderer(EntityRendererProvider.Context context) {
        super(context, new AmalRadar_Model());
    }

    @Override
    public boolean shouldRender(AmalRadar pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void actuallyRender(PoseStack poseStack, AmalRadar animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        if(isReRender) return;


    }

    @Override
    public void render(AmalRadar entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
//        int i=0;
//        for(AmalRadar.RadarTracker tracker : entity.getClientPingData().values().stream().toList()) {
//            i++;
//            float score = tracker.score;
//
//            float normalizedScore = Mth.clamp(score/200f, 0.0F, 1.0F);
//
//            Vector4f color = StaticSiliconiteMethods.colorLerpHSV(
//                    new Vector4i(26, 0, 13, 100),
//                    new Vector4i(255, 128, 0, 150), normalizedScore);
//            BlockPos trackerPos = tracker.getPosition().subtract(entity.getOnPos());
//
//            VertexConsumer consumer = bufferSource.getBuffer(
//                    RenderType.debugQuads()
////                    RenderType.entityTranslucentCull(new ResourceLocation(Chemosynthesis.MODID, "textures/block/debug_cube.png"))
//            );
//            StaticRenderingMethods.renderDebugCube(consumer, poseStack, poseStack.last(),
//                    trackerPos.getX()-0.51f, trackerPos.getY()-1.01f, trackerPos.getZ()-0.51f,
//                    1.02f, 1.02f, 1.02f,
//                    color.x/255f, color.y/255f, color.z/255f, color.w/255f, 15728880);
//        }
    }
}