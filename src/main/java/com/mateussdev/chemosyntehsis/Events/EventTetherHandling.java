package com.mateussdev.chemosyntehsis.Events;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModNetworking;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.*;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredCapabilityProvider;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.CapabilityStuffs.TetheredSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering.*;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID)
public class EventTetherHandling {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof Mob)) return;
        if (event.getObject() instanceof Player) return;

        TetheredCapabilityProvider provider = new TetheredCapabilityProvider();
        event.addCapability(
                new ResourceLocation(Chemosynthesis.MODID, "tethered"),
                provider
        );
        event.addListener(provider::invalidate);
    }



    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // Was the attacker a tethered mob?
        if (!(event.getSource().getEntity() instanceof Mob attacker)) return;
        if (!isTethered(attacker)) return;

        LivingEntity victim = event.getEntity();
        if (!(victim instanceof Mob targetMob)) return;

        // Only tether if victim HP (after damage) will be under 20%
        float hpAfterHit = victim.getHealth() - event.getAmount();
        float threshold = victim.getMaxHealth() * 0.20f;

        if (hpAfterHit <= threshold && hpAfterHit > 0) {
            if (targetMob.level() instanceof ServerLevel serverLevel) {
                UniversalTethering.tryTetherMob(targetMob, serverLevel);
                attacker.getCapability(TetheredCapabilityProvider.TETHERED_CAP)
                        .ifPresent(cap -> cap.getHooks().forEach(h -> h.onSpreadInfection(attacker, targetMob)));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        // Check capability — if it was tethered before the chunk unloaded, re-inject goals
        mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP).ifPresent(cap -> {
            if (cap.isTethered()) {

                //Re-add the siliconite functionality
                StandardTetheredBehavior behavior = new StandardTetheredBehavior();
                cap.addHook(behavior);
                behavior.onTether(mob);

                //Inject the new AI
                UniversalTethering.injectTetheredGoals(mob);

                //Re-sync to client on world load
                ModNetworking.CHANNEL.send(
                        PacketDistributor.TRACKING_ENTITY.with(() -> mob),
                        new TetheredSyncPacket(mob.getId(), true)
                );
            }
        });
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!(event.getEntity() instanceof Mob mob)) return;

        mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP).ifPresent(cap -> {
            if (cap.isTethered()) {
                cap.getHooks().forEach(h -> h.onTick(mob));
            }
        });
    }

    @SubscribeEvent
    public static void onLivingHurtHook(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof Mob attacker)) return;
        if (!isTethered(attacker)) return;

        LivingEntity victim = event.getEntity();
        if (!(victim instanceof Mob targetMob)) return;

        float hpAfterHit = victim.getHealth() - event.getAmount();
        float threshold = victim.getMaxHealth() * 0.20f;

        if (hpAfterHit <= threshold && hpAfterHit > 0) {
            if (targetMob.level() instanceof ServerLevel serverLevel) {
                UniversalTethering.tryTetherMob(targetMob, serverLevel);

                // Attacker should stop targeting its newly converted ally
                if (attacker.getTarget() == targetMob) {
                    attacker.setTarget(null);
                }

                attacker.getCapability(TetheredCapabilityProvider.TETHERED_CAP)
                        .ifPresent(cap -> cap.getHooks().forEach(h -> h.onSpreadInfection(attacker, targetMob)));
            }
        }
    }

    @SubscribeEvent
    public static void onTetheredHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Mob mob)) return;
        if (!UniversalTethering.isTethered(mob)) return;

        var source = event.getSource();
        if (source.is(DamageTypeTags.IS_FIRE)
                || source.is(DamageTypeTags.BURNS_ARMOR_STANDS)
                || source.is(DamageTypeTags.IGNITES_ARMOR_STANDS)) {
            event.setAmount(event.getAmount() * 0.1f);
        }

        //Handle onHurt hook inside of the ITetheredHook
        mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP)
                .ifPresent(cap -> cap.getHooks().forEach(h -> h.onHurt(mob, event.getEntity(), event.getAmount())));
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof Mob mob)) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        mob.getCapability(TetheredCapabilityProvider.TETHERED_CAP).ifPresent(cap -> {
            if (cap.isTethered()) {
                ModNetworking.CHANNEL.send(
                        net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
                        new TetheredSyncPacket(mob.getId(), true)
                );
            }
        });
    }
}
