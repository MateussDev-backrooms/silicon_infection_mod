package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.BlockEntities.vein_block.BEVeinBlock;
import com.mateussdev.chemosyntehsis.BlockEntities.vein_block.BEVeinBlock_Renderer;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlockEntities;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller_Renderer;
import com.mateussdev.chemosyntehsis.Entities.teth_zombie.TethZombie_Renderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EventRegisterRenderer {
    @SubscribeEvent
    public static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(ModEntities.SILICON_ROLLER.get(), SiliconRoller_Renderer::new);
        EntityRenderers.register(ModEntities.TETH_ZOMBIE.get(), TethZombie_Renderer::new);

        event.registerBlockEntityRenderer(ModBlockEntities.VEIN_BLOCK.get(), BEVeinBlock_Renderer::new);
    }
}
