package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_converter.AmalConverter;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_radar.AmalRadar;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_spawner.AmalSpawner;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_turret.AmalTurret;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.genome.GenomeCarrier;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.homunculus_t1.HomunculusNucleusT1;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt2_perfocyte.HybridPerfocyte;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.basic_bulbs.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon.BulbHarpoonEntity;
import com.mateussdev.chemosyntehsis.Entities.Homunculus.Amalgamations.amal_zombie.AmalZombie;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.mutated_harpoon.MutatedHarpoonEntity;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_enderman.TethEnderman;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_pig.TethPig;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_sheep.TethSheep;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte.HybridAstrocyte;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte.HybridErythrocyte;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte.HybridThrombocyte;
import com.mateussdev.chemosyntehsis.Entities.Metabolized.met_cow.MetCow;
import com.mateussdev.chemosyntehsis.Entities.Metabolized.met_zombie.MetZombie;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton.TethSkeleton;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie.TethZombie;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow.TethCow;
import com.mateussdev.chemosyntehsis.Entities.Vegetated.vasc_roller.VascularRoller;
import com.mateussdev.chemosyntehsis.Entities.Vegetated.veg_bulb.VegetativeBulb;
import com.mateussdev.chemosyntehsis.Entities.Vegetated.veg_roller.VegetativeRoller;
import com.mateussdev.chemosyntehsis.Entities.util.VeinConnectorEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chemosynthesis.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Chemosynthesis.MODID);

    //===== DEFINE ENTITIES HERE =====//

    public static final RegistryObject<EntityType<SiliconRoller>> SILICON_ROLLER =
            ENTITIES.register("silicon_roller", () -> EntityType.Builder.of(SiliconRoller::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("silicon_roller"));

    public static final RegistryObject<EntityType<ChunkOfFlesh>> CHUNK_OF_FLESH =
            ENTITIES.register("chunk_of_flesh", () -> EntityType.Builder.of(ChunkOfFlesh::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("chunk_of_flesh"));
    public static final RegistryObject<EntityType<ClusterOfFlesh>> CLUSTER_OF_FLESH =
            ENTITIES.register("cluster_of_flesh", () -> EntityType.Builder.of(ClusterOfFlesh::new, MobCategory.MONSTER)
                    .sized(1.4f, 1.4f)
                    .build("cluster_of_flesh"));

    public static final RegistryObject<EntityType<GenomeCarrier>> GENOME_CARRIER =
            ENTITIES.register("genome_carrier", () -> EntityType.Builder.of(GenomeCarrier::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.6f)
                    .build("genome_carrier"));

    //Tethered mobs

    public static final RegistryObject<EntityType<TethZombie>> TETH_ZOMBIE =
            ENTITIES.register("teth_zombie", () -> EntityType.Builder.of(TethZombie::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .build("teth_zombie"));

    public static final RegistryObject<EntityType<TethSkeleton>> TETH_SKELETON =
            ENTITIES.register("teth_skeleton", () -> EntityType.Builder.of(TethSkeleton::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .build("teth_skeleton"));

    public static final RegistryObject<EntityType<TethCow>> TETH_COW =
            ENTITIES.register("teth_cow", () -> EntityType.Builder.of(TethCow::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.2f)
                    .build("teth_cow"));
    public static final RegistryObject<EntityType<TethSheep>> TETH_SHEEP =
            ENTITIES.register("teth_sheep", () -> EntityType.Builder.of(TethSheep::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.1f)
                    .build("teth_sheep"));
    public static final RegistryObject<EntityType<TethPig>> TETH_PIG =
            ENTITIES.register("teth_pig", () -> EntityType.Builder.of(TethPig::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.0f)
                    .build("teth_pig"));
    public static final RegistryObject<EntityType<TethEnderman>> TETH_ENDERMAN =
            ENTITIES.register("teth_enderman", () -> EntityType.Builder.of(TethEnderman::new, MobCategory.MONSTER)
                    .sized(0.7f, 3f)
                    .build("teth_enderman"));

    //Metabolized mobs
    public static final RegistryObject<EntityType<MetZombie>> MET_ZOMBIE =
            ENTITIES.register("met_zombie", () -> EntityType.Builder.of(MetZombie::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .build("met_zombie"));
    public static final RegistryObject<EntityType<MetCow>> MET_COW =
            ENTITIES.register("met_cow", () -> EntityType.Builder.of(MetCow::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.2f)
                    .build("met_cow"));

    //Vegetated mobs
    public static final RegistryObject<EntityType<VegetativeBulb>> VEG_BULB =
            ENTITIES.register("veg_bulb", () -> EntityType.Builder.of(VegetativeBulb::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("veg_bulb"));
    public static final RegistryObject<EntityType<VegetativeRoller>> VEG_ROLLER =
            ENTITIES.register("veg_roller", () -> EntityType.Builder.of(VegetativeRoller::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("veg_roller"));
    public static final RegistryObject<EntityType<VascularRoller>> VASC_ROLLER =
            ENTITIES.register("vasc_roller", () -> EntityType.Builder.of(VascularRoller::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .build("vasc_roller"));

    //Amalgamations [T1 homunculus]
    public static final RegistryObject<EntityType<HomunculusNucleusT1>> HOMUNCULUS_T1 =
            ENTITIES.register("homunculus_t1", () -> EntityType.Builder.of(HomunculusNucleusT1::new, MobCategory.MONSTER)
                    .sized(1.2f, 3f)
                    .build("homunculus_t1"));
    public static final RegistryObject<EntityType<AmalZombie>> AMAL_ZOMBIE =
            ENTITIES.register("amal_zombie", () -> EntityType.Builder.of(AmalZombie::new, MobCategory.MONSTER)
                    .sized(1.2f, 3f)
                    .build("amal_zombie"));
    public static final RegistryObject<EntityType<AmalSpawner>> AMAL_SPAWNER =
            ENTITIES.register("amal_spawner", () -> EntityType.Builder.of(AmalSpawner::new, MobCategory.MONSTER)
                    .sized(1.2f, 3f)
                    .build("amal_spawner"));
    public static final RegistryObject<EntityType<AmalTurret>> AMAL_TURRET =
            ENTITIES.register("amal_turret", () -> EntityType.Builder.of(AmalTurret::new, MobCategory.MONSTER)
                    .sized(1.2f, 3f)
                    .build("amal_turret"));
    public static final RegistryObject<EntityType<AmalRadar>> AMAL_RADAR =
            ENTITIES.register("amal_radar", () -> EntityType.Builder.of(AmalRadar::new, MobCategory.MONSTER)
                    .sized(1.2f, 4f)
                    .build("amal_radar"));
    public static final RegistryObject<EntityType<AmalConverter>> AMAL_CONVERTER =
            ENTITIES.register("amal_converter", () -> EntityType.Builder.of(AmalConverter::new, MobCategory.MONSTER)
                    .sized(1.2f, 3f)
                    .build("amal_converter"));

    //Hybrid mobs

    //Tier 1
    public static final RegistryObject<EntityType<HybridThrombocyte>> THROMBOCYTE =
            ENTITIES.register("hybt1_thrombocyte", () -> EntityType.Builder.of(HybridThrombocyte::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("hybt1_thrombocyte"));
    public static final RegistryObject<EntityType<HybridAstrocyte>> ASTROCYTE =
            ENTITIES.register("hybt1_astrocyte", () -> EntityType.Builder.of(HybridAstrocyte::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("hybt1_astrocyte"));
    public static final RegistryObject<EntityType<HybridErythrocyte>> ERYTHROCYTE =
            ENTITIES.register("hybt1_erythrocyte", () -> EntityType.Builder.of(HybridErythrocyte::new, MobCategory.MONSTER)
                    .sized(0.85f, 0.85f)
                    .build("hybt1_erythrocyte"));

    //Tier 2
    public static final RegistryObject<EntityType<HybridPerfocyte>> PERFOCYTE =
            ENTITIES.register("hybt1_perfocyte", () -> EntityType.Builder.of(HybridPerfocyte::new, MobCategory.MONSTER)
                    .sized(1.5f, 1.2f)
                    .build("hybt1_perfocyte"));

    //Tier 3


    //Projectile

    public static final RegistryObject<EntityType<BulbProjectileEntity>> BULB_PROJECTILE =
            ENTITIES.register("bulb_projectile", () -> EntityType.Builder.<BulbProjectileEntity>of(BulbProjectileEntity::new, MobCategory.MISC)
                    .sized(0.33f, 0.33f)
                    .build("bulb_projectile"));
    public static final RegistryObject<EntityType<BulbHarpoonEntity>> BULB_HARPOON_PROJECTILE =
            ENTITIES.register("bulb_harpoon_projectile", () -> EntityType.Builder.<BulbHarpoonEntity>of(BulbHarpoonEntity::new, MobCategory.MISC)
                    .sized(0.33f, 0.33f)
                    .build("bulb_harpoon_projectile"));
    public static final RegistryObject<EntityType<MutatedHarpoonEntity>> MUTATED_HARPOON_PROJECTILE =
            ENTITIES.register("mutated_harpoon_projectile", () -> EntityType.Builder.<MutatedHarpoonEntity>of(MutatedHarpoonEntity::new, MobCategory.MISC)
                    .sized(0.33f, 0.33f)
                    .build("mutated_harpoon_projectile"));

    //GibEntities
    public static final RegistryObject<EntityType<GibFlesh>> GIB_FLESH =
            ENTITIES.register("gib_flesh", () -> EntityType.Builder.of(GibFlesh::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f)
                    .build("gib_flesh"));

    //Block entities

    //Utility
    public static final RegistryObject<EntityType<VeinConnectorEntity>> VEIN_CONNECTOR =
            ENTITIES.register("util_vein_connector", () -> EntityType.Builder.of(VeinConnectorEntity::new, MobCategory.MISC)
                    .sized(0.1f, 0.1f)
                    .build("util_vein_connector"));


    //===== DEFINE ENTITIES HERE =====//

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
