package com.mateussdev.chemosyntehsis.Util;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseOrganelle;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseSiliconite;
import com.mateussdev.chemosyntehsis.Entities.generic.BaseTethered;
import com.mateussdev.chemosyntehsis.Systems.UniversalTethering.UniversalTethering;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.joml.Vector4i;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.*;

public class StaticSiliconiteMethods {

    // ===== Static Vars ===== //



    public static final Map<Block, Block> infectionConversionMap = Map.ofEntries(
            //Surface
            Map.entry(Blocks.GRASS_BLOCK, ModBlocks.SILICATED_GRASS_L4.get()),
            Map.entry(Blocks.PODZOL, ModBlocks.SILICATED_GRASS_L4.get()),
            Map.entry(Blocks.MYCELIUM, ModBlocks.SILICATED_GRASS_L4.get()),
            Map.entry(Blocks.DIRT, ModBlocks.SILICATED_DIRT_L4.get()),

            //Stones
            Map.entry(Blocks.STONE, ModBlocks.SILICATED_STONE_L4.get()),
            Map.entry(Blocks.ANDESITE, ModBlocks.SILICATED_STONE_L4.get()),
            Map.entry(Blocks.DIORITE, ModBlocks.SILICATED_STONE_L4.get()),
            Map.entry(Blocks.GRANITE, ModBlocks.SILICATED_STONE_L4.get()),
            Map.entry(Blocks.DEEPSLATE, ModBlocks.SILICATED_DEEPSLATE_L4.get()),
            Map.entry(Blocks.TUFF, ModBlocks.SILICATED_DEEPSLATE_L4.get()),

            //Wood
            Map.entry(Blocks.ACACIA_LOG, ModBlocks.SILICATED_LOG_L4.get()),
            Map.entry(Blocks.BIRCH_LOG, ModBlocks.SILICATED_LOG_L4.get()),
            Map.entry(Blocks.CHERRY_LOG, ModBlocks.SILICATED_LOG_L4.get()),
            Map.entry(Blocks.JUNGLE_LOG, ModBlocks.SILICATED_LOG_L4.get()),
            Map.entry(Blocks.DARK_OAK_LOG, ModBlocks.SILICATED_LOG_L4.get()),
            Map.entry(Blocks.MANGROVE_LOG, ModBlocks.SILICATED_LOG_L4.get()),
            Map.entry(Blocks.OAK_LOG, ModBlocks.SILICATED_LOG_L4.get()),

            //Other
            Map.entry(Blocks.ACACIA_LEAVES, Blocks.AIR),
            Map.entry(Blocks.BIRCH_LEAVES, Blocks.AIR),
            Map.entry(Blocks.CHERRY_LEAVES, Blocks.AIR),
            Map.entry(Blocks.JUNGLE_LEAVES, Blocks.AIR),
            Map.entry(Blocks.DARK_OAK_LEAVES, Blocks.AIR),
            Map.entry(Blocks.MANGROVE_LEAVES, Blocks.AIR),
            Map.entry(Blocks.OAK_LEAVES, Blocks.AIR)
    );

    public static List<Block> blockConversionBlacklist = List.of(
            ModBlocks.AMALGAMATED_FLESH_BLOCK.get(),
            ModBlocks.VEIN_BLOCK.get(),
            ModBlocks.BIOMUSH.get(),
            ModBlocks.FLESH_PILE.get(),
            ModBlocks.TENDRILS.get(),
            ModBlocks.SULFURED_TENDRILS.get(),
            ModBlocks.CORPSE_PIG.get(),
            ModBlocks.CORPSE_COW.get(),
            Blocks.OBSIDIAN,
            Blocks.CRYING_OBSIDIAN
    );

    public static Map<EntityType<? extends BaseOrganelle>, Integer> vegetatedRadiusMap = Map.of(
            ModEntities.VEG_BULB.get(), 3,
            ModEntities.VEG_ROLLER.get(), 5,
            ModEntities.VASC_ROLLER.get(), 8
    );

    public static final Map<EntityType<? extends LivingEntity>, SupportProfile> ASTROCYTE_SUPPORT_TARGETS =
            Map.of(
                    ModEntities.TETH_ZOMBIE.get(), new SupportProfile(),
                    ModEntities.TETH_SKELETON.get(), new SupportProfile(),
                    ModEntities.TETH_COW.get(), new SupportProfile()
            );

    public record SupportProfile() {}

    // ===== BLACKLISTS ===== //

    public static final Set<EntityType<? extends LivingEntity>> BLACKLISTED_MOBS = Set.of(
            EntityType.CREEPER,
            EntityType.BAT,
            EntityType.COD,
            EntityType.SALMON,
            EntityType.TROPICAL_FISH,
            EntityType.SQUID,
            EntityType.GLOW_SQUID
    );
    public static boolean shouldAttackMob(LivingEntity entity) {
        //Do not target siliconites
        if(entity instanceof BaseSiliconite) return false;
        if(isMobFromChemosynthesisMod(entity)) return false;

        //Do not attack universally tethered mobs
        if(entity instanceof Mob mob && UniversalTethering.isTethered(mob)) return false;


        return !BLACKLISTED_MOBS.contains(entity.getType());
    }

    // ===== UTIL ===== //

    public static void debugLog(String text) {
        if(Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(text));
        }
    }

    // ===== Gameplay ===== //

    @Deprecated
    public static void tetherMob(ServerLevel serverLevel, LivingEntity tetherTarget) {
        EntityType<? extends LivingEntity> tethered_result_type = UniversalTethering.handmadeTetheredMobs.get(tetherTarget.getType());
        if(tethered_result_type==null) {
            //Split into chunks depending on the bounding box size
            if(boundingBoxVolume(tetherTarget.getBoundingBox()) < 0f) { //TEMP. TODO: Prevent small mobs from tethering
                splitIntoChunks(serverLevel, tetherTarget.blockPosition(), Mth.clamp(Mth.ceil(boundingBoxVolume(tetherTarget.getBoundingBox())), 2, 64));
                tetherTarget.discard();
            } else {
                spawnTransformationParticle(serverLevel, tetherTarget.blockPosition());
                serverLevel.playSound(
                        null,
                        tetherTarget.blockPosition(),
                        SoundEvents.ZOMBIE_INFECT,
                        SoundSource.HOSTILE,
                        1f,
                        1f);
                universalTether(serverLevel, tetherTarget);
            }
            return;
        }
        LivingEntity tethered_result = tethered_result_type.create(serverLevel);
        //spawn the chosen entity
        if(tethered_result!=null) {
            tethered_result.moveTo(tetherTarget.getX(), tetherTarget.getY(), tetherTarget.getZ());
            spawnTransformationParticle(serverLevel, tetherTarget.blockPosition());
            serverLevel.playSound(
                    null,
                    tetherTarget.blockPosition(),
                    SoundEvents.ZOMBIE_INFECT,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            serverLevel.addFreshEntity(tethered_result);
            tetherTarget.discard();
        }
    }
    @Deprecated
    public static void universalTether(ServerLevel slvl, LivingEntity target) {
        if(target instanceof Mob mob) {

            //Set tethered value via Mixin

            //Only affect pathfinder mobs
            mob.setHealth(mob.getMaxHealth());
            //Reset target selectors
            mob.targetSelector.removeAllGoals((e) -> {return true;});
            StaticSiliconiteMethods.debugLog(mob.targetSelector.getAvailableGoals().size()+" - targeting goal count");
            mob.setTarget(null);
            //Make mob target the same things as us
            mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, LivingEntity.class, 0, true, false, StaticSiliconiteMethods::shouldAttackMob));

            //Add attack goal to those with no attack damage attribute
            double attackDamage = 6D;
            double attackKnockback = 0.5D;
            if(mob.getAttribute(Attributes.ATTACK_DAMAGE) == null) {
                attackDamage = 6D;
                attackKnockback = 0.5D;
            }

        } else {
            if(target instanceof Player player) {
                if(!player.isCreative()) player.die(player.damageSources().drown());
                else return;
            }
            splitIntoChunks(slvl, target.blockPosition(), Mth.clamp(Mth.ceil(boundingBoxVolume(target.getBoundingBox())), 2, 64));
            target.discard();
        }
    }

    public static void splitIntoChunks(ServerLevel slvl, BlockPos pos, int count) {
            slvl.playSound(
                    null,
                    pos,
                    SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR,
                    SoundSource.HOSTILE,
                    1f,
                    1f);

            //Particles
            StaticSiliconiteMethods.spawnBloodBurst(slvl, pos);

            //Spawn chunks or gibs
            for (int i = 0; i < count; i++) {
                if(slvl.random.nextFloat() < 0.33f) {
                    ChunkOfFlesh chunkOfFlesh = ModEntities.CHUNK_OF_FLESH.get().create(slvl);
                    chunkOfFlesh.moveTo(pos.getX(), pos.getY(), pos.getZ());
                    chunkOfFlesh.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.1f, (slvl.random.nextDouble())*0.8f, (slvl.random.nextDouble()*2f - 1f)*0.1f));
                    slvl.addFreshEntity(chunkOfFlesh);
                } else {
                    GibFlesh gib = ModEntities.GIB_FLESH.get().create(slvl);
                    gib.moveTo(pos.getX(), pos.getY(), pos.getZ());
                    gib.addDeltaMovement(new Vec3((slvl.random.nextDouble()*2f - 1f)*0.4f, (slvl.random.nextDouble())*0.5f, (slvl.random.nextDouble()*2f - 1f)*0.4f));
                    slvl.addFreshEntity(gib);
                }
            }
    }

    public static boolean isMobFromChemosynthesisMod(LivingEntity entity) {
        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

    public static boolean isEntityFromChemosynthesisMod(Entity entity) {
        ResourceLocation entityTypeKey = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
        return entityTypeKey.getNamespace().equals(Chemosynthesis.MODID);
    }

    // ===== Math ===== //

    public static float flerp(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }

    public static Vector3f RGBtoHSV(int r, int g, int b) {
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        float d = max - min;

        float h;
        float s = max == 0 ? 0 : d / (float)max;
        float v = max/255f;

        if(max == min) {h=0;}
        else if(max == r) {h = (g - b) + d * (g < b ? 6: 0); h /= 6 * d;}
        else if(max == g) {h = (b - r) + d * 2; h /= 6 * d;}
        else {h = (r - g) + d * 4; h /= 6 * d;}

        return new Vector3f(h, s, v);
    }

    public static Vector3f RGBtoHSV(Vector3i rgb) {
        return RGBtoHSV(rgb.x, rgb.y, rgb.z);
    }

    public static Vector3i HSVtoRGB(float h, float s, float v) {
        int i = Mth.floor(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        float rt = 0;
        float gt = 0;
        float bt = 0;

        switch((i % 6)) {
            case 0 -> {rt = v; gt = t; bt = p;}
            case 1 -> {rt = q; gt = v; bt = p;}
            case 2 -> {rt = p; gt = v; bt = t;}
            case 3 -> {rt = p; gt = q; bt = v;}
            case 4 -> {rt = t; gt = p; bt = v;}
            case 5 -> {rt = v; gt = p; bt = q;}
        }

        return new Vector3i(
                Math.round(rt * 255),
                Math.round(gt * 255),
                Math.round(bt * 255)
        );
    }

    public static Vector3i HSVtoRGB(Vector3f hsv) {
        return HSVtoRGB(hsv.x, hsv.y, hsv.z);
    }

    public static Vector4f colorLerp(Vector4i a, Vector4i b, float t) {
        float alphalerp = flerp((float)a.w, (float)b.w, t);

        Vector3f hsva = RGBtoHSV(new Vector3i(a.x, a.y, a.z));
        Vector3f hsvb = RGBtoHSV(new Vector3i(b.x, b.y, b.z));

        Vector3i result = new Vector3i(
                Math.round(flerp(a.x, b.x, t)),
                Math.round(flerp(a.y, b.y, t)),
                Math.round(flerp(a.z, b.z, t))
        );

        return new Vector4f(result.x, result.y, result.z, alphalerp);
    }

    public static double boundingBoxVolume(AABB box) {
        return box.getXsize() * box.getYsize() * box.getZsize();
    }

    // ===== Gecko Functions ===== //

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
        GeoBone[] bulbs = animatable.getBulbsArray(model);
        for (int i = 0; i < animatable.getBulbCount(); i++) {
            bulbs[i].setHidden(animatable.getBrokenOffBulbs() > i);
        }
    }

    public static void updateHeadRotationAnimal(BaseSiliconite animatable, GeoModel<?> model, String boneName, float offsetPitch, float offsetYaw) {
        GeoBone head = model.getBone(boneName).get();

        float yaw   = (float) Math.toRadians(animatable.yHeadRot);
        float pitch = (float) Math.toRadians(animatable.xRotO);
        head.setRotZ((float) (-yaw + Math.toRadians(animatable.yBodyRot) + offsetYaw));
        head.setRotX((-pitch + offsetPitch));
    }

    public static void updateHeadRotationUpright(BaseSiliconite animatable, GeoModel<?> model, String boneName, float offsetPitch, float offsetYaw) {
        GeoBone head = model.getBone(boneName).get();

        float yaw   = (float) Math.toRadians(animatable.yHeadRot);
        float pitch = (float) Math.toRadians(animatable.xRotO);
        head.setRotY((float) (-yaw + Math.toRadians(animatable.yBodyRot) + offsetYaw));
        head.setRotX((-pitch + offsetPitch));
    }

    public static void updateBreathing(GeoModel<?> model, String boneName, double frequency, double intensity, float partialTick, int t) {
        GeoBone body = model.getBone(boneName).get();
        float breatheFactorP = (float) (1 + Math.sin((double) t / frequency) * intensity);
        float breatheFactorN = (float) (1 + Math.sin((double) (t+1) / frequency) * intensity);
        float breatheFactor = Mth.lerp(partialTick, breatheFactorP, breatheFactorN);
        body.setScaleX(breatheFactor);
        body.setScaleY(breatheFactor);
        body.setScaleZ(breatheFactor);
    }

    public static void updateBoneWobble(BaseTethered animatable, GeoModel<?> model, float partialTick) {
        Map<String, Vector3f> boneWobble = animatable.boneWobble;
        if(boneWobble.isEmpty()) return;

        for(String boneName : boneWobble.keySet().stream().toList()) {
            GeoBone bone = model.getBone(boneName).get();
                Vector3f impulse = boneWobble.get(boneName);
                Vector3f nextImpulse = new Vector3f(
                        Mth.lerp(0.3f, impulse.x, 0),
                        Mth.lerp(0.3f, impulse.y, 0),
                        Mth.lerp(0.3f, impulse.z, 0)
                );

                // Apply the impulse to the bone's current rotation
                // Note: This adds to the existing animation rotation
                bone.setRotX((float) (bone.getRotX() + Math.toRadians(Mth.lerp(partialTick, impulse.x, nextImpulse.x))));
                bone.setRotY((float) (bone.getRotY() + Math.toRadians(Mth.lerp(partialTick, impulse.y, nextImpulse.y))));
                bone.setRotZ((float) (bone.getRotZ() + Math.toRadians(Mth.lerp(partialTick, impulse.z, nextImpulse.z))));
        }
    }

    public static void shake(PoseStack pPoseStack, int shakeTimer, float freq, float power) {
        float shakeyX = (float) Math.sin(shakeTimer * freq) * power;
        float shakeyY = (float) Math.cos(shakeTimer * freq) * power;
        pPoseStack.translate(shakeyX, 0, shakeyY);
    }

    // ===== Particles ===== //
    @Deprecated
    public static void spawnBloodBurst(ServerLevel slvl, BlockPos blockPos) {
        DustParticleOptions blood = new DustParticleOptions(new Vector3f(0.8f, 0.0f, 0.0f), 3.0f);
        DustParticleOptions smallBlood = new DustParticleOptions(new Vector3f(0.7f, 0.1f, 0.1f), 1.5f);

        slvl.sendParticles(
                blood,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                15,
                0.3,
                0.5,
                0.3,
                0.1
        );

        slvl.sendParticles(
                smallBlood,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                25,
                0.5,
                0.7,
                0.5,
                0.15
        );
    }
    @Deprecated
    public static void spawnBloodHit(ServerLevel slvl, Vec3 blockPos) {
        DustParticleOptions blood = new DustParticleOptions(new Vector3f(0.8f, 0.0f, 0.0f), 1.3f);
        DustParticleOptions darkBlood = new DustParticleOptions(new Vector3f(0.6f, 0.0f, 0.0f), 1.5f);

        slvl.sendParticles(
                blood,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                5,
                0.3,
                -0.5,
                0.3,
                0.8
        );

        slvl.sendParticles(
                darkBlood,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                3,
                0.15,
                -0.3,
                0.15,
                0.4
        );
    }
    @Deprecated
    public static void spawnTransformationParticle(ServerLevel slvl, BlockPos pos) {
        slvl.sendParticles(
                ParticleTypes.EXPLOSION,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                1,
                0,
                0,
                0,
                0.1
        );
    }





}
