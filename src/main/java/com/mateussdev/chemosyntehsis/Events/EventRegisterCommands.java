package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.GenomeCommand;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.HomunculusCommand;
import com.mateussdev.chemosyntehsis.Systems.GlobalWarming.GlobalWarmingCommand;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.TetherCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventRegisterCommands {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        GlobalWarmingCommand.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
        TetherCommand.register(event.getDispatcher());
        GenomeCommand.register(event);
        HomunculusCommand.register(event);
    }
}
