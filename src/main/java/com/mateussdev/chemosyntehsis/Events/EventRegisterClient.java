package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventRegisterClient {

    @SubscribeEvent
    public static void onEntityRenderPre(RenderLivingEvent.Pre event) {
        Entity entity = event.getEntity();

        // Check if infected
        if (isInfected(entity)) { // You need a helper to check NBT or Capability
            PoseStack poseStack = event.getPoseStack();

            // 1. Push a new matrix layer
            poseStack.pushPose();

            // 2. Tint Yellow (Red=1.0, Green=1.0, Blue=0.0)
            // You can adjust alpha here if you want transparency
            poseStack.

            // DO NOT call event.setCanceled(true).
            // We just modified the stack for the vanilla renderer to use.
        }
    }
}
