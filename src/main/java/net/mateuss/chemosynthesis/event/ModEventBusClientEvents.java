package net.mateuss.chemosynthesis.event;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.client.ModModelLayers;
import net.mateuss.chemosynthesis.entity.client.ModelSiliconRoller;
import net.mateuss.chemosynthesis.entity.client.ModelSiliconTripod;
import net.mateuss.chemosynthesis.entity.client.ModelTethZombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SILICON_ROLLER_LAYER, ModelSiliconRoller::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SILICON_TRIPOD_LAYER, ModelSiliconTripod::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TETH_ZOMBIE_LAYER, ModelTethZombie::createBodyLayer);
    }
}
