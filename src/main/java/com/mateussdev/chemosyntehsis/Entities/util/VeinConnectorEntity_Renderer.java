package com.mateussdev.chemosyntehsis.Entities.util;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class VeinConnectorEntity_Renderer extends EntityRenderer<VeinConnectorEntity> {
    private static final ResourceLocation BEAM_TEXTURE =
            new ResourceLocation("textures/entity/guardian_beam.png");

    public VeinConnectorEntity_Renderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(VeinConnectorEntity veinConnectorEntity) {
        return null;
    }

    public class VeinDefinition {
        public Vec3 offset;
        public Vec3 color;
        public float thickness;
        public float gravMul;
        public VeinDefinition(Vec3 offset, Vec3 color, float thickness, float gravMul) {
            this.offset = offset;
            this.color = color;
            this.thickness = thickness;
            this.gravMul = gravMul;
        }
    }

    private List<VeinDefinition> veinConfig = List.of(
            new VeinDefinition(new Vec3(0, 0, 0), new Vec3(1f, 1f, 1f), 0.5f, 2f),
            new VeinDefinition(new Vec3(0.1, 0.3, 0), new Vec3(0.66f, 1f, 1f), 0.3f, 2f),
            new VeinDefinition(new Vec3(0, -0.33, 0.3), new Vec3(1f, 1f, 0.66f), 0.25f, 4f)
    );

    @Override
    public void render(VeinConnectorEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        VertexConsumer consumer = pBuffer.getBuffer(
                RenderType.entityTranslucentCull(new ResourceLocation(Chemosynthesis.MODID, "textures/block/amalgamated_flesh_block.png"))
                
        );

        for(BlockPos pos : pEntity.getClientConnectionPositions()) {

            Vec3 sourcePos = pEntity.blockPosition().getCenter();
            Vec3 targetPos = pos.getCenter();

            Vec3 relPos = targetPos.subtract(sourcePos).scale(0.5f);

            float dst = (float) sourcePos.distanceTo(targetPos);

            for(VeinDefinition vein : veinConfig) {

                float heartbeat = (1+heartbeatFunction(pEntity.level().getGameTime() + pPartialTick)/50);

                StaticRenderingMethods.renderParabolaTube(consumer, pPoseStack.last(),
                        vein.offset, relPos.add(vein.offset),
                        (float) vein.color.x, (float) vein.color.y, (float) vein.color.z,
                        vein.thickness*heartbeat, dst, Mth.floor(dst*1.2f),
                        pEntity.getCorrectPackedLight(), vein.gravMul*heartbeat);
            }
        }
    }

    private float heartbeatFunction(float t) {
        return (float) (Math.pow(Mth.sin(t/7), 80)*Mth.sin(t + 1.5f)*8);
    }

    @Override
    public boolean shouldRender(VeinConnectorEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }
}
