package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.Packets.MutationSyncPacket;
import com.mateussdev.chemosyntehsis.Util.Packets.TetheredSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ModNetworking {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Chemosynthesis.MODID, "tether_sync"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void register() {
        CHANNEL.registerMessage(
                0,
                TetheredSyncPacket.class,
                TetheredSyncPacket::encode,
                TetheredSyncPacket::new,
                TetheredSyncPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
        );
        CHANNEL.registerMessage(
                1,
                MutationSyncPacket.class,
                MutationSyncPacket::encode,
                MutationSyncPacket::new,
                MutationSyncPacket::handle,
                Optional.empty()
        );
    }
}
