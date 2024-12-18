package net.Mateuss.Chemosynthesis.events;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.renderers.*;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventMobRenderers
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        // Some client setup code
        EntityRenderers.register(ModEntities.SILICON_ROLLER.get(), RendererSiliconRoller::new);
        EntityRenderers.register(ModEntities.SILICON_TRIPOD.get(), RendererSiliconTripod::new);
        EntityRenderers.register(ModEntities.SILIPEDE.get(), RendererSilipede::new);
        EntityRenderers.register(ModEntities.TETH_ZOMBIE.get(), RendererTethZombie::new);
        EntityRenderers.register(ModEntities.TETH_COW.get(), RendererTethCow::new);
        EntityRenderers.register(ModEntities.TETH_SHEEP.get(), RendererTethSheep::new);
        EntityRenderers.register(ModEntities.TETH_PIG.get(), RendererTethPig::new);
        EntityRenderers.register(ModEntities.HOMUNCULUS_HEART.get(), RendererHomunculus::new);
        EntityRenderers.register(ModEntities.HOMUNCULUS_VAUCOLE.get(), RendererHomVaucole::new);
        EntityRenderers.register(ModEntities.BRACHATIC_STAGE.get(), RendererBrachaticStage::new);

        EntityRenderers.register(ModEntities.VEG_ROLLER.get(), RendererVegRoller::new);
        EntityRenderers.register(ModEntities.BRACHATIC_HARPOON.get(), context -> new RendererBrachaticHarpoon(context, GeneralModModelLayers.BRACHATIC_HARPOON_LAYER));
    }
}