package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.DSPSystem.DirectiveSignalingProteinData;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventServer {
    private static int particleTick = 0;
    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if(event.phase == TickEvent.LevelTickEvent.Phase.END) {
            if(event.level instanceof ServerLevel slvl) {
                DirectiveSignalingProteinData data = DirectiveSignalingProteinData.get(slvl);
                data.tick(slvl);

                if (++particleTick >= 10) {
                    particleTick = 0;
                    data.spawnDSPParticles(slvl);
                }
            }


        }
    }
}
