package com.mateussdev.chemosyntehsis.Entities.veg_bulb;

import com.mateussdev.chemosyntehsis.Entities.generic.BaseVegetated;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class VegetativeBulb_Renderer extends GeoEntityRenderer<VegetativeBulb> {
    public VegetativeBulb_Renderer(EntityRendererProvider.Context context) {
        super(context, new VegetativeBulb_Model());
    }
}