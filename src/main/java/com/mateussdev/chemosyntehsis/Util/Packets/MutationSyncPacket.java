package com.mateussdev.chemosyntehsis.Util.Packets;

import com.mateussdev.chemosyntehsis.Core.ModMutations;
import com.mateussdev.chemosyntehsis.Core.ModRegistries;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.Mutation;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationBaseRenderLayer;
import com.mateussdev.chemosyntehsis.Systems.GenomeSystem.Mutation.MutationType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.function.Supplier;

public class MutationSyncPacket {
    private final int entityId;
    private final ResourceLocation mutationTypeId;

    public MutationSyncPacket(int entityId, ResourceLocation mutationTypeId) {
        this.entityId = entityId;
        this.mutationTypeId = mutationTypeId;
    }

    public MutationSyncPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.mutationTypeId = buf.readResourceLocation();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(mutationTypeId);
    }

    public static void handle(MutationSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                ClientHandler.handleClient(packet);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientHandler {
        public static void handleClient(MutationSyncPacket packet) {
            try {
                Minecraft mc = Minecraft.getInstance();
                Entity entity = mc.level.getEntity(packet.entityId);
                if (!(entity instanceof Mob mob)) return;

                EntityRenderer<?> renderer = mc.getEntityRenderDispatcher().getRenderer(mob);
                if (!(renderer instanceof GeoEntityRenderer geoRenderer)) {
                    return;
                }

                // ----- METHOD 1: Use static registry field from ModRegistries -----
                IForgeRegistry<MutationType> registry = ModRegistries.MUTATION_TYPE_REGISTRY;
                MutationType type = null;
                if (registry != null) {
                    type = registry.getValue(packet.mutationTypeId);
                }

                // ----- METHOD 2: Fallback to DeferredRegister (always works) -----
                if (type == null) {
                    RegistryObject<MutationType> ro = ModMutations.findMutationType(packet.mutationTypeId);
                    if (ro != null) {
                        type = ro.get();
                    } else {
                        return;
                    }
                }

                Mutation clientMutation = type.createClientSide();
                GeoRenderLayer<?> layer = clientMutation.createRenderLayer(geoRenderer);
                boolean alreadyExists = geoRenderer.getRenderLayers().stream()
                        .anyMatch(l -> {
                            if (l instanceof MutationBaseRenderLayer<?> baseLayer) {
                                return baseLayer.mutationReference.getTypeId().equals(packet.mutationTypeId);
                            }
                            return false;
                        });

                if (!alreadyExists) {
                    geoRenderer.addRenderLayer(layer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
