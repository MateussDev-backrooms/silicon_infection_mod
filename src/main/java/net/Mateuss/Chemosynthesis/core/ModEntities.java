package net.Mateuss.Chemosynthesis.core;

import net.Mateuss.Chemosynthesis.Chemosynthesis;
import net.Mateuss.Chemosynthesis.entity.living_entities.conjugonal.EntityBrachaticStage;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomunculus;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomVaucole;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySilipede;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethCow;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethPig;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethSheep;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethZombie;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.EntityVegRoller;
import net.Mateuss.Chemosynthesis.entity.projectile.ProjectileBrachaticHarpoon;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Chemosynthesis.MODID);

    //PURE

    public static final RegistryObject<EntityType<EntitySiliconTripod>> SILICON_TRIPOD =
            ENTITY_TYPES.register("silicon_tripod", () -> EntityType.Builder.of(EntitySiliconTripod::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f).build("silicon_tripod"));
    public static final RegistryObject<EntityType<EntitySiliconRoller>> SILICON_ROLLER =
            ENTITY_TYPES.register("silicon_roller", () -> EntityType.Builder.of(EntitySiliconRoller::new, MobCategory.MONSTER)
                    .sized(0.7f, 0.7f).build("silicon_roller"));
    public static final RegistryObject<EntityType<EntitySilipede>> SILIPEDE =
            ENTITY_TYPES.register("silipede", () -> EntityType.Builder.of(EntitySilipede::new, MobCategory.MONSTER)
                    .sized(0.7f, 2.3f).build("silipede"));

    //VEGETATIVE
    public static final RegistryObject<EntityType<EntityVegRoller>> VEG_ROLLER =
            ENTITY_TYPES.register("veg_roller", () -> EntityType.Builder.of(EntityVegRoller::new, MobCategory.MISC)
                    .sized(0.7f, 0.7f).build("veg_roller"));

    //TETHERED

    public static final RegistryObject<EntityType<EntityTethZombie>> TETH_ZOMBIE =
            ENTITY_TYPES.register("teth_zombie", () -> EntityType.Builder.of(EntityTethZombie::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_zombie"));

    public static final RegistryObject<EntityType<EntityTethCow>> TETH_COW =
            ENTITY_TYPES.register("teth_cow", () -> EntityType.Builder.of(EntityTethCow::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_cow"));

    public static final RegistryObject<EntityType<EntityTethSheep>> TETH_SHEEP =
            ENTITY_TYPES.register("teth_sheep", () -> EntityType.Builder.of(EntityTethSheep::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_sheep"));
    public static final RegistryObject<EntityType<EntityTethPig>> TETH_PIG =
            ENTITY_TYPES.register("teth_pig", () -> EntityType.Builder.of(EntityTethPig::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("teth_pig"));

    //CONJUGONAL
    public static final RegistryObject<EntityType<EntityBrachaticStage>> BRACHATIC_STAGE =
            ENTITY_TYPES.register("brachatic_stage", () -> EntityType.Builder.of(EntityBrachaticStage::new, MobCategory.MONSTER)
                    .sized(0.7f, 2.3f).build("brachatic_stage"));


    //HOMUNCULOIDS
    public static final RegistryObject<EntityType<EntityHomunculus>> HOMUNCULUS_HEART =
            ENTITY_TYPES.register("homunculus_heart", () -> EntityType.Builder.of(EntityHomunculus::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("homunculus_heart"));
    public static final RegistryObject<EntityType<EntityHomVaucole>> HOMUNCULUS_VAUCOLE =
            ENTITY_TYPES.register("homunculus_vaucole", () -> EntityType.Builder.of(EntityHomVaucole::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.8f).build("homunculus_vaucole"));

    //PROJECTILES
    public static final RegistryObject<EntityType<ProjectileBrachaticHarpoon>> BRACHATIC_HARPOON =
            ENTITY_TYPES.register("brachatic_harpoon", () -> EntityType.Builder.of(ProjectileBrachaticHarpoon::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("brachatic_harpoon"));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
