package net.Mateuss.Chemosynthesis.events;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.core.ModEntities;
import net.Mateuss.Chemosynthesis.entity.living_entities.conjugonal.EntityBrachaticStage;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomunculus;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySilipede;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethCow;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethPig;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethSheep;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethZombie;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.EntityVegRoller;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventMobAttributes {
    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SILICON_ROLLER.get(), EntitySiliconRoller.createAttributes().build());
        event.put(ModEntities.SILIPEDE.get(), EntitySilipede.createAttributes().build());
        event.put(ModEntities.SILICON_TRIPOD.get(), EntitySiliconTripod.createAttributes().build());
        event.put(ModEntities.TETH_ZOMBIE.get(), EntityTethZombie.createAttributes().build());
        event.put(ModEntities.TETH_COW.get(), EntityTethCow.createAttributes().build());
        event.put(ModEntities.TETH_SHEEP.get(), EntityTethSheep.createAttributes().build());
        event.put(ModEntities.TETH_PIG.get(), EntityTethPig.createAttributes().build());
        event.put(ModEntities.HOMUNCULUS_HEART.get(), EntityHomunculus.createAttributes().build());
        event.put(ModEntities.BRACHATIC_STAGE.get(), EntityBrachaticStage.createAttributes().build());

        event.put(ModEntities.VEG_ROLLER.get(), EntityVegRoller.createAttributes().build());
    }
}
