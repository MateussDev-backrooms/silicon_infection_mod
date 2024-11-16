package net.mateuss.chemosynthesis.event;

import net.mateuss.chemosynthesis.Chemosynthesis;
import net.mateuss.chemosynthesis.entity.ModEntities;
import net.mateuss.chemosynthesis.entity.client.ModModelLayers;
import net.mateuss.chemosynthesis.entity.custom.EntitySiliconRoller;
import net.mateuss.chemosynthesis.entity.custom.EntitySiliconTripod;
import net.mateuss.chemosynthesis.entity.custom.EntityTethZombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SILICON_ROLLER.get(), EntitySiliconRoller.createAttributes().build());
        event.put(ModEntities.SILICON_TRIPOD.get(), EntitySiliconTripod.createAttributes().build());
        event.put(ModEntities.TETH_ZOMBIE.get(), EntityTethZombie.createAttributes().build());
    }
}
