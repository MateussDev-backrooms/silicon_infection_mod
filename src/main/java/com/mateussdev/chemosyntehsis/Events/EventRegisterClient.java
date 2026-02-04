package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.ITethered;
import com.mateussdev.chemosyntehsis.Entities.generic.UniversalTethering.TetheredOverlayLayer;
import com.mateussdev.chemosyntehsis.Util.Models.BulbSingular;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EventRegisterClient {

    @SubscribeEvent
    public static void onEntityRenderPre(RenderLivingEvent.Pre event) {
        Entity entity = event.getEntity();

        if(entity instanceof ITethered tethered) {
            if(tethered.isTethered()) {

                PoseStack poseStack = event.getPoseStack();
                MultiBufferSource bufferSource = event.getMultiBufferSource();

                // Example: Scale up tethered entities slightly
            }
        }
    }


}
