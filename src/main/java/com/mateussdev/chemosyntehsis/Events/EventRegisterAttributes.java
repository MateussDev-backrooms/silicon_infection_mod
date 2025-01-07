package com.mateussdev.chemosyntehsis.Events;


import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventRegisterAttributes {
    @SubscribeEvent
    public static void registerMobAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SILICON_ROLLER.get(), SiliconRoller.createAttributes().build());
    }
}
