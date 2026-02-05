package com.mateussdev.chemosyntehsis.Entities.silicon_roller;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.Rendering.TriPlanarUVVertexConsumer;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SiliconRoller_Renderer extends GeoEntityRenderer<SiliconRoller> {
    public SiliconRoller_Renderer(EntityRendererProvider.Context context) {
        super(context, new SiliconRoller_Model());
    }

    private static final ResourceLocation TENDRIL_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tendril_overlay.png");

    @Override
    public void render(SiliconRoller entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        VertexConsumer testConsumer = new TriPlanarUVVertexConsumer(bufferSource.getBuffer(RenderType.debugQuads()),
                Mth.lerp(partialTick, entity.xOld, entity.xo),
                Mth.lerp(partialTick, entity.yOld, entity.yo),
                Mth.lerp(partialTick, entity.zOld, entity.zo), true);
        StaticRenderingMethods.renderDebugCube(testConsumer, poseStack, poseStack.last(), 0, 2, 0, 1, 1, 1, 1, 1, 1, 1, LightTexture.FULL_BRIGHT);
    }
}
