package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import mod.azure.azurelib.cache.object.GeoBone;
import mod.azure.azurelib.model.GeoModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.joml.Vector3f;

import java.util.*;

public class StaticSiliconiteMethods {
    public static Map<EntityType<?>, EntityType<? extends BaseTethered>> tetherHashMap =
            //Defines all tetherable mobs and their tether result
            //the key is the target mob and the value is the tether result
            new HashMap<>();

    public static Map<Block, Block> infectionConversionMap = new HashMap<>();

    public static Map<EntityType<? extends BaseVegetated>, Integer> vegetatedRadiusMap = new HashMap<>();

    public static final Map<EntityType<? extends LivingEntity>, SupportProfile> ASTROCYTE_SUPPORT_TARGETS =
            Map.of(
                    ModEntities.TETH_ZOMBIE.get(), new SupportProfile(),
                    ModEntities.TETH_SKELETON.get(), new SupportProfile(),
                    ModEntities.TETH_COW.get(), new SupportProfile()
            );

    public record SupportProfile() {}

    static {
        //Define all tether pairs here
        tetherHashMap.put(EntityType.ZOMBIE, ModEntities.TETH_ZOMBIE.get());
        tetherHashMap.put(EntityType.HUSK, ModEntities.TETH_ZOMBIE.get());
        tetherHashMap.put(EntityType.DROWNED, ModEntities.TETH_ZOMBIE.get());
        tetherHashMap.put(EntityType.COW, ModEntities.TETH_COW.get());
        tetherHashMap.put(EntityType.SKELETON, ModEntities.TETH_SKELETON.get());
        tetherHashMap.put(EntityType.STRAY, ModEntities.TETH_SKELETON.get());

        //Define all block infection pairs

        //mushy
        infectionConversionMap.put(Blocks.GRASS_BLOCK, ModBlocks.MUSHY_SILICON_BLOCK.get());
        infectionConversionMap.put(Blocks.DIRT, ModBlocks.MUSHY_SILICON_BLOCK.get());
        infectionConversionMap.put(Blocks.PODZOL, ModBlocks.MUSHY_SILICON_BLOCK.get());
        infectionConversionMap.put(Blocks.MYCELIUM, ModBlocks.MUSHY_SILICON_BLOCK.get());
        infectionConversionMap.put(Blocks.CLAY, ModBlocks.MUSHY_SILICON_BLOCK.get());

        //hard
        infectionConversionMap.put(Blocks.STONE, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.DEEPSLATE, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.DIORITE, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.GRANITE, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.ANDESITE, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.SANDSTONE, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.TUFF, ModBlocks.SILICATE_BLOCK.get());
        infectionConversionMap.put(Blocks.CALCITE, ModBlocks.SILICATE_BLOCK.get());

        //Define vegetated radiuses
        vegetatedRadiusMap.put(ModEntities.VEG_BULB.get(), 8);
    }

    public static void tetherMob(ServerLevel serverLevel, LivingEntity tetherTarget) {
        EntityType<? extends LivingEntity> tethered_result_type = tetherHashMap.get(tetherTarget.getType());
        if(tethered_result_type==null) {
            return;
        }
        LivingEntity tethered_result = tethered_result_type.create(serverLevel);
        //spawn the chosen entity
        if(tethered_result!=null) {
            tethered_result.moveTo(tetherTarget.getX(), tetherTarget.getY(), tetherTarget.getZ());
            serverLevel.sendParticles(
                    ParticleTypes.EXPLOSION,
                    tetherTarget.getX() + 0.5,
                    tetherTarget.getY() + 0.5,
                    tetherTarget.getZ() + 0.5,
                    1,          // Number of particles per spawn call
                    0,    // X offset for randomness
                    0,    // Y offset
                    0,    // Z offset
                    0.1         // Speed of the particle
            );
            serverLevel.playSound(
                    null,
                    tetherTarget.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            serverLevel.addFreshEntity(tethered_result);
        }
    }

    public static boolean isTetherable(LivingEntity entity) {
        return tetherHashMap.containsKey(entity.getType());
    }
    public static boolean isTetherable(EntityType<LivingEntity> entityType) {
        return tetherHashMap.containsKey(entityType);
    }

    public static boolean isMobFromChemosynthesisMod(LivingEntity entity) {
        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

    public static GeoBone[] scrambleBones(GeoBone[] array) {
        Random rng = new Random();
        GeoBone[] result = array.clone();

        for (int i = 0; i < array.length; i++) {
            int el_1 = rng.nextInt(array.length);
            int el_2 = rng.nextInt(array.length);

            GeoBone buffer = result[el_2];
            result[el_2] = result[el_1];
            result[el_1] = buffer;
        }

        return result;
    }

    public static void updateBulbVisuals(BaseSiliconite animatable, GeoModel<?> model) {
        GeoBone[] bulbs = animatable.getBulbsArray(model).clone();
        for (int i = 0; i < animatable.getBulbCount(); i++) {
            bulbs[i].setHidden(animatable.getBrokenOffBulbs() > i);
        }
    }

    public static void spawnBloodBurst(ServerLevel slvl, BlockPos blockPos) {
        DustParticleOptions blood = new DustParticleOptions(
                new Vector3f(0.8f, 0.0f, 0.0f),
                3.0f
        );

        slvl.sendParticles(
                blood,
                blockPos.getX(),
                blockPos.getY() + 1.0,
                blockPos.getZ(),
                30,
                0.3,
                0.5,
                0.3,
                0.1
        );
    }

    public static final Set<EntityType<? extends LivingEntity>> BLACKLISTED_MOBS = Set.of(
            EntityType.CREEPER,
            EntityType.BAT
    );
    public static boolean shouldAttackMob(LivingEntity entity) {
        if(entity instanceof BaseSiliconite) return false;
        if(isMobFromChemosynthesisMod(entity)) return false;
        if(BLACKLISTED_MOBS.contains(entity.getType())) return false;
        return true;
    }
}
