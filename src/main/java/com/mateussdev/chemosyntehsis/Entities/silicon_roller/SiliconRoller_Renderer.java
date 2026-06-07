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
}
