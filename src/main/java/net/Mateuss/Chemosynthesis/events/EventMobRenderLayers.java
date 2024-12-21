package net.Mateuss.Chemosynthesis.events;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.models.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventMobRenderLayers {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GeneralModModelLayers.SILICON_ROLLER_LAYER, ModelSiliconRoller::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.SILIPEDE_LAYER, ModelSilipede::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.SILICON_TRIPOD_LAYER, ModelSiliconTripod::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.TETH_ZOMBIE_LAYER, ModelTethZombie::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.TETH_COW_LAYER, ModelTethCow::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.TETH_SHEEP_LAYER, ModelTethSheep::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.TETH_PIG_LAYER, ModelTethPig::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.HOMUNCULUS_LAYER, ModelHomunculus::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.HOMUNCULUS_VAUCOLE_LAYER, ModelHomVaucole::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.BRACHATIC_STAGE_LAYER, ModelBrachaticStage::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.BRACHATIC_HARPOON_LAYER, ModelBrachaticHarpoonBulb::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.VEG_ROLLER_LAYER, ModelVegRoller::createBodyLayer);
        event.registerLayerDefinition(GeneralModModelLayers.ZIGOTE_LAYER, ModelZigote::createBodyLayer);
    }
}
