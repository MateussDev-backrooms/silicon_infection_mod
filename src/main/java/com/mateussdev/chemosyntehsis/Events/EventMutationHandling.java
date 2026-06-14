package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModNetworking;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.IGenomeModifiable;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Util.Packets.MutationSyncPacket;
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
        if (mob instanceof IGenomeModifiable genmod) {
            if (genmod.getGene() != null && !genmod.getGene().mutations.isEmpty()) {
                mob.getServer().execute(() -> {
                    if (!player.isAlive() || player.hasDisconnected()) return;
                    for (Mutation mutation : genmod.getGene().mutations) {
                        if (mutation.hasRenderLayer()) {
                            ModNetworking.CHANNEL.send(
                                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> mob),
                                    new MutationSyncPacket(mob.getId(), mutation.getTypeId())
                            );
                        }
                    }
                });
            }
        }
    }
}
