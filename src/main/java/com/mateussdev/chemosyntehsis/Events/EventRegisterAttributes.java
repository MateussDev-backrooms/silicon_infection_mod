package com.mateussdev.chemosyntehsis.Events;


import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.amal_zombie.AmalZombie;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte.HybridAstrocyte;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte.HybridErythrocyte;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte.HybridThrombocyte;
import com.mateussdev.chemosyntehsis.Entities.met_zombie.MetZombie;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow.TethCow;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton.TethSkeleton;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie.TethZombie;
import com.mateussdev.chemosyntehsis.Entities.vasc_roller.VascularRoller;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Chemosynthesis.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventRegisterAttributes {
    @SubscribeEvent
    public static void registerMobAttributes(EntityAttributeCreationEvent event) {
        //Pure
        event.put(ModEntities.SILICON_ROLLER.get(), SiliconRoller.createAttributes().build());
        event.put(ModEntities.CHUNK_OF_FLESH.get(), ChunkOfFlesh.createAttributes().build());
        event.put(ModEntities.CLUSTER_OF_FLESH.get(), ClusterOfFlesh.createAttributes().build());


        //Tethered mobs
        event.put(ModEntities.TETH_ZOMBIE.get(), TethZombie.createAttributes().build());
        event.put(ModEntities.TETH_SKELETON.get(), TethSkeleton.createAttributes().build());
        event.put(ModEntities.TETH_COW.get(), TethCow.createAttributes().build());

        //Metabolized mobs
        event.put(ModEntities.MET_ZOMBIE.get(), MetZombie.createAttributes().build());

        //Vegatated mobs
        event.put(ModEntities.VEG_BULB.get(), VegetativeBulb.createAttributes().build());
        event.put(ModEntities.VEG_ROLLER.get(), VegetativeRoller.createAttributes().build());
        event.put(ModEntities.VASC_ROLLER.get(), VascularRoller.createAttributes().build());

        //Amalgamations
        event.put(ModEntities.AMAL_ZOMBIE.get(), AmalZombie.createAttributes().build());


        //Hybrids
        event.put(ModEntities.THROMBOCYTE.get(), HybridThrombocyte.createAttributes().build());
        event.put(ModEntities.ERYTHROCYTE.get(), HybridErythrocyte.createAttributes().build());
        event.put(ModEntities.ASTROCYTE.get(), HybridAstrocyte.createAttributes().build());

    }
}
