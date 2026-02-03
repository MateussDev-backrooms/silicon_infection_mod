package com.mateussdev.chemosyntehsis.Util.Packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

public class SyncTetheringPacket extends ClientboundCustomPayloadPacket {
    public SyncTetheringPacket(ResourceLocation pIdentifier, FriendlyByteBuf pData) {
        super(pIdentifier, pData);
    }
}
