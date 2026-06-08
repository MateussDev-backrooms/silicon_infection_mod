package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModNetworking;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationSyncPacket;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredCapabilityProvider;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredSyncPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID)
public class EventMutationHandling {
    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof Mob mob)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        //Readd the layers on reload
        if(mob instanceof IGenomeModifiable genmod) {
            if(genmod.getGene() != null && !genmod.getGene().mutations.isEmpty()) {
                // Delay by one tick — connection must be fully established
                mob.getServer().execute(() -> {

                    // Also guard here in case the player disconnected in the meantime
                    if (!player.isAlive() || player.hasDisconnected()) return;

                    for (Mutation mutation : genmod.getGene().mutations) {
                        if (mutation.getMutationRenderLayer(null) != null) {
                            ModNetworking.CHANNEL.send(
                                    PacketDistributor.PLAYER.with(() -> player),
                                    new MutationSyncPacket(mob.getId(), mutation.getClass().getSimpleName())
                            );
                        }
                    }
                });
            }
        }
    }
}
