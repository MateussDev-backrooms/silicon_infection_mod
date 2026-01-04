package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.BlockEntities.vein_block.BEVeinBlock;
import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.amal_zombie.AmalZombie;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.cluster_of_flesh.ClusterOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_astrocyte.HybridAstrocyte;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_erythrocyte.HybridErythrocyte;
import com.mateussdev.chemosyntehsis.Entities.Hybrids.hybt1_thrombocyte.HybridThrombocyte;
import com.mateussdev.chemosyntehsis.Entities.met_zombie.MetZombie;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_skeleton.TethSkeleton;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_zombie.TethZombie;
import com.mateussdev.chemosyntehsis.Entities.Tethered.teth_cow.TethCow;
import com.mateussdev.chemosyntehsis.Entities.vasc_roller.VascularRoller;
import com.mateussdev.chemosyntehsis.Entities.veg_bulb.VegetativeBulb;
import com.mateussdev.chemosyntehsis.Entities.veg_roller.VegetativeRoller;
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
                    .sized(0.7f, 1.8f)
                    .build("teth_cow"));

    //Metabolized mobs
    public static final RegistryObject<EntityType<MetZombie>> MET_ZOMBIE =
            ENTITIES.register("met_zombie", () -> EntityType.Builder.of(MetZombie::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f)
                    .build("met_zombie"));

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

    //Amalgamations
    public static final RegistryObject<EntityType<AmalZombie>> AMAL_ZOMBIE =
            ENTITIES.register("amal_zombie", () -> EntityType.Builder.of(AmalZombie::new, MobCategory.MONSTER)
                    .sized(1.2f, 2.3f)
                    .build("amal_zombie"));

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

    //Tier 3


    //Projectile

    public static final RegistryObject<EntityType<BulbProjectileEntity>> BULB_PROJECTILE =
            ENTITIES.register("bulb_projectile", () -> EntityType.Builder.<BulbProjectileEntity>of(BulbProjectileEntity::new, MobCategory.MISC)
                    .sized(0.33f, 0.33f)
                    .build("bulb_projectile"));

    //GibEntities
    public static final RegistryObject<EntityType<GibFlesh>> GIB_FLESH =
            ENTITIES.register("gib_flesh", () -> EntityType.Builder.of(GibFlesh::new, MobCategory.MISC)
                    .sized(0.4f, 0.4f)
                    .build("gib_flesh"));

    //Block entities

    public static final RegistryObject<BlockEntityType<BEVeinBlock>> BE_VEIN_BLOCK =
            BLOCK_ENTITIES.register("be_vein_block", () -> BlockEntityType.Builder.of(BEVeinBlock::new, ModBlocks.VEIN_BLOCK.get())
                    .build(null));


    //===== DEFINE ENTITIES HERE =====//

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}
