package com.mateussdev.chemosyntehsis.Core;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.Projectiles.BulbProjectileEntity;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.silicon_roller.SiliconRoller;
import com.mateussdev.chemosyntehsis.Entities.teth_skeleton.TethSkeleton;
import com.mateussdev.chemosyntehsis.Entities.teth_zombie.TethZombie;
import com.mateussdev.chemosyntehsis.Entities.teth_cow.TethCow;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chemosynthesis.MODID);

    //===== DEFINE ENTITIES HERE =====//

    public static final RegistryObject<EntityType<SiliconRoller>> SILICON_ROLLER =
            ENTITIES.register("silicon_roller", () -> EntityType.Builder.of(SiliconRoller::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("silicon_roller"));

    public static final RegistryObject<EntityType<ChunkOfFlesh>> CHUNK_OF_FLESH =
            ENTITIES.register("chunk_of_flesh", () -> EntityType.Builder.of(ChunkOfFlesh::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f)
                    .build("chunk_of_flesh"));

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



    //Projectile

    public static final RegistryObject<EntityType<BulbProjectileEntity>> BULB_PROJECTILE =
            ENTITIES.register("bulb_projectile", () -> EntityType.Builder.<BulbProjectileEntity>of(BulbProjectileEntity::new, MobCategory.MISC)
                    .sized(0.33f, 0.33f)
                    .build("bulb_projectile"));

    //===== DEFINE ENTITIES HERE =====//

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
