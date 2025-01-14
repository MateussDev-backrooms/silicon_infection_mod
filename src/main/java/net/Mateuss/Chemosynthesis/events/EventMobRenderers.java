package net.Mateuss.Chemosynthesis.events;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.client.GeneralModModelLayers;
import net.Mateuss.Chemosynthesis.client.renderers.*;
import net.Mateuss.Chemosynthesis.client.renderers.blockentity.RendererBEOrganelleConnector;
import net.Mateuss.Chemosynthesis.core.ModBlockEntities;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventMobRenderers
{
    @SubscribeEvent
    public static void onClientSetup(final EntityRenderersEvent.RegisterRenderers event)
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
        EntityRenderers.register(ModEntities.HOMUNCULUS_MITOCHONDRIA.get(), RendererHomMitochondria::new);
        EntityRenderers.register(ModEntities.BRACHATIC_STAGE.get(), RendererBrachaticStage::new);
        EntityRenderers.register(ModEntities.ZIGOTE.get(), RendererZigote::new);

        EntityRenderers.register(ModEntities.VEG_ROLLER.get(), RendererVegRoller::new);
        EntityRenderers.register(ModEntities.BRACHATIC_HARPOON.get(), context -> new RendererBrachaticHarpoon(context, GeneralModModelLayers.BRACHATIC_HARPOON_LAYER));
        EntityRenderers.register(ModEntities.BULB_PROJECTILE.get(), context -> new RendererBulbProjectile(context, GeneralModModelLayers.BRACHATIC_HARPOON_LAYER));

        event.registerBlockEntityRenderer(ModBlockEntities.ORGANELLE_CONNECTOR.get(), RendererBEOrganelleConnector::new);
    }
}