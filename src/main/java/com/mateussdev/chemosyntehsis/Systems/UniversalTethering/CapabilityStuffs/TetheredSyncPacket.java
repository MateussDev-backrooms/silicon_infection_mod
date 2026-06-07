package com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TetheredSyncPacket {

    private final int entityId;
    private final boolean tethered;

    public TetheredSyncPacket(int entityId, boolean tethered) {
        this.entityId = entityId;
        this.tethered = tethered;
    }

    public TetheredSyncPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.tethered = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(tethered);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // This runs on the client
            var level = net.minecraft.client.Minecraft.getInstance().level;
            if (level == null) return;

            var entity = level.getEntity(entityId);
            if (!(entity instanceof Mob mob)) return;

            mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP)
                    .ifPresent(cap -> cap.setTethered(tethered));
        });
        ctx.get().setPacketHandled(true);
    }
}