package com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredCapabilityProvider;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MutationSyncPacket {
    public final int entityId;
    public final String mutationClassName;

    public MutationSyncPacket(int entityId, String mutationClassName) {
        this.entityId = entityId;
        this.mutationClassName = mutationClassName;
    }

    public MutationSyncPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.mutationClassName = buf.readUtf(256);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(mutationClassName, 256);
    }

    // Client-side packet handler
    public static void handle(MutationSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(packet));
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(MutationSyncPacket packet) {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.level.getEntity(packet.entityId);

//        Chemosynthesis.LOGGER.debug("MutationSyncPacket sent");

        if (!(entity instanceof Mob mob)) return;

        EntityRenderer<?> rawRenderer = mc.getEntityRenderDispatcher().getRenderer(entity);
//        Chemosynthesis.LOGGER.debug("Entity: "+entity);



        if (!(rawRenderer instanceof GeoEntityRenderer geoRenderer)) return;
//        Chemosynthesis.LOGGER.debug("GeoRenderer: "+geoRenderer);

        // Resolve which layer to add based on mutation class name
        GeoRenderLayer layer = MutationLayerRegistry.createLayer(packet.mutationClassName, geoRenderer);
        if (layer != null) {
//            Chemosynthesis.LOGGER.debug("Successfully added the render layer");
//            StaticSiliconiteMethods.debugLog("successfully added the layer!");
            geoRenderer.addRenderLayer(layer);
        }
    }
}
