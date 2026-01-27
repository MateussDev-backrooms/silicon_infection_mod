package com.mateussdev.chemosyntehsis.Entities.generic;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Core.ModBlocks;
import com.mateussdev.chemosyntehsis.Core.ModEntities;
import com.mateussdev.chemosyntehsis.Entities.GibEntities.flesh_gib.GibFlesh;
import com.mateussdev.chemosyntehsis.Entities.chunk_of_flesh.ChunkOfFlesh;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
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

import java.awt.*;
import java.util.*;

public class StaticSiliconiteMethods {

    // ===== Static Vars ===== //

    public static Map<EntityType<?>, EntityType<? extends BaseTethered>> tetherHashMap =
            //Defines all tetherable mobs and their tether result
            //the key is the target mob and the value is the tether result
            new HashMap<>();

    public static Map<Block, Block> infectionConversionMap = new HashMap<>();

    public static Map<EntityType<? extends BaseOrganelle>, Integer> vegetatedRadiusMap = new HashMap<>();

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
        tetherHashMap.put(EntityType.ENDERMAN, ModEntities.TETH_ENDERMAN.get());

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
        vegetatedRadiusMap.put(ModEntities.VEG_BULB.get(), 3);
        vegetatedRadiusMap.put(ModEntities.VEG_ROLLER.get(), 5);
        vegetatedRadiusMap.put(ModEntities.VASC_ROLLER.get(), 8);
    }

    public static void debugLog(String text) {
        if(Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal(text));
        }
    }

    public static void tetherMob(ServerLevel serverLevel, LivingEntity tetherTarget) {
        EntityType<? extends LivingEntity> tethered_result_type = tetherHashMap.get(tetherTarget.getType());
        if(tethered_result_type==null) {
            //Split into chunks depending on the bounding box size
            splitIntoChunks(serverLevel, tetherTarget.blockPosition(), Mth.clamp(Mth.ceil(boundingBoxVolume(tetherTarget.getBoundingBox())), 2, 64));
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

            //Spawn chunks
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

    // ===== Math ===== //

    public static float flerp(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }

    public static Vector3f RGBtoHSV(int r, int g, int b) {
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        float d = max - min;

        float h = 0;
        float s = max == 0 ? 0 : (float)d / (float)max;
        float v = max/255f;

        if(max == min) {h=0;}
        else if(max == r) {h = (g - b) + d * (g < b ? 6: 0); h /= 6 * d;}
        else if(max == g) {h = (b - r) + d * 2; h /= 6 * d;}
        else if(max == b) {h = (r - g) + d * 4; h /= 6 * d;}

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

    public static Vector4f colorLerpHSV(Vector4i a, Vector4i b, float t) {
        float alphalerp = flerp((float)a.w, (float)b.w, t);

        Vector3f hsva = RGBtoHSV(new Vector3i(a.x, a.y, a.z));
        Vector3f hsvb = RGBtoHSV(new Vector3i(b.x, b.y, b.z));

//        Vector3i result = HSVtoRGB(new Vector3f(
//           flerp(hsva.x, hsvb.x, t),
//           flerp(hsva.y, hsvb.y, t),
//           flerp(hsva.z, hsvb.z, t)
//        ));

        Vector3i result = new Vector3i(
                Math.round(flerp(a.x, b.x, t)),
                Math.round(flerp(a.y, b.y, t)),
                Math.round(flerp(a.z, b.z, t))
        );

        return new Vector4f(result.x, result.y, result.z, alphalerp);
    }

    public static double boundingBoxVolume(AABB box) {
        return box.getXsize() * box.getXsize() * box.getZsize();
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
        head.setRotX((float) (-pitch + offsetPitch));
    }

    public static void updateHeadRotationUpright(BaseSiliconite animatable, GeoModel<?> model, String boneName, float offsetPitch, float offsetYaw) {
        GeoBone head = model.getBone(boneName).get();

        float yaw   = (float) Math.toRadians(animatable.yHeadRot);
        float pitch = (float) Math.toRadians(animatable.xRotO);
        head.setRotY((float) (-yaw + Math.toRadians(animatable.yBodyRot) + offsetYaw));
        head.setRotX((float) (-pitch + offsetPitch));
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

    public static void spawnBloodBurst(ServerLevel slvl, BlockPos blockPos) {
        // Main burst with varying sizes
        DustParticleOptions blood = new DustParticleOptions(
                new Vector3f(0.8f, 0.0f, 0.0f),
                3.0f
        );

        DustParticleOptions smallBlood = new DustParticleOptions(
                new Vector3f(0.7f, 0.1f, 0.1f), // Slight orange tint for variation
                1.5f
        );

        DustParticleOptions sprayBlood = new DustParticleOptions(
                new Vector3f(0.9f, 0.05f, 0.05f), // Bright red for spray
                0.8f
        );

        // Main burst
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

        // Smaller particles for spray effect
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

        // Fine spray mist
        slvl.sendParticles(
                sprayBlood,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                30,
                0.7,
                1.0,
                0.7,
                0.2
        );

        // Add dripping/splatter effect on surfaces
        DustParticleOptions splatter = new DustParticleOptions(
                new Vector3f(0.7f, 0.0f, 0.0f),
                1.5f
        );

        // Create splatter in a wider area
        for (int i = 0; i < 8; i++) {
            double offsetX = (slvl.random.nextDouble() - 0.5) * 1.5;
            double offsetZ = (slvl.random.nextDouble() - 0.5) * 1.5;

            slvl.sendParticles(
                    splatter,
                    blockPos.getX() + offsetX,
                    blockPos.getY() + 0.1,
                    blockPos.getZ() + offsetZ,
                    1,
                    0,
                    0,
                    0,
                    0
            );
        }

        // Optional: Add crit particles for sparkle effect (blood droplets catching light)
        slvl.sendParticles(
                ParticleTypes.CRIT,
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ(),
                5,
                0.2,
                0.3,
                0.2,
                0.1
        );
    }

    public static void spawnBloodHit(ServerLevel slvl, Vec3 blockPos) {
        // Main blood splash
        DustParticleOptions blood = new DustParticleOptions(
                new Vector3f(0.8f, 0.0f, 0.0f),
                1.3f
        );

        // Variant with darker red for depth
        DustParticleOptions darkBlood = new DustParticleOptions(
                new Vector3f(0.6f, 0.0f, 0.0f),
                1.5f
        );

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

        // Add some darker particles for variation
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

        // Add dripping effect using falling dust (blood droplets)
        DustParticleOptions drip = new DustParticleOptions(
                new Vector3f(0.7f, 0.0f, 0.0f),
                1.5f
        );



        slvl.sendParticles(
                drip,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                3,
                0.1,
                -0.8,
                0.1,
                0.05
        );

        // Small smoke puff for impact effect
        slvl.sendParticles(
                ParticleTypes.SMOKE,
                blockPos.x(),
                blockPos.y(),
                blockPos.z(),
                2,
                0.05,
                0.05,
                0.05,
                0.01
        );
    }

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
        if(entity instanceof BaseSiliconite) return false;
        if(isMobFromChemosynthesisMod(entity)) return false;
        if(BLACKLISTED_MOBS.contains(entity.getType())) return false;
        return true;
    }

    // ===== RENDERING ===== //

    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, double x, double y, double z, double nx, double ny, double nz, float red, float green, float blue, float alpha, float u, float v, int light) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, 1.0F) // Color
                .uv(u, v)               // Texture UV
                .overlayCoords(OverlayTexture.NO_OVERLAY) // Overlay texture
                .uv2(light)                // Lightmap texture (full brightness)
                .normal(pose.normal(), (float) nx, (float) ny, (float) nz) // Normal vector (arbitrary up)
                .endVertex();
    }

    private static float[] calculateNormal(float x1, float y1, float z1,
                                           float x2, float y2, float z2,
                                           float x3, float y3, float z3) {
        // Calculate two vectors on the plane of the quad
        float[] vector1 = new float[]{x2 - x1, y2 - y1, z2 - z1};
        float[] vector2 = new float[]{x3 - x1, y3 - y1, z3 - z1};

        // Compute cross product
        float[] normal = cross(vector1, vector2);
        normalize(normal); // Normalize to unit length
        return normal;
    }


    public static void renderQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                  float x1, float y1, float z1, float x2, float y2, float z2,
                                  float x3, float y3, float z3, float x4, float y4, float z4,
                                  float red, float green, float blue, int light) {
        float[] normal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        // Define the quad vertices with all required attributes
        vertex(vertexConsumer, pose, x1, y1, z1, normal[0], normal[1], normal[2], red, green, blue, 1f, 0f, 0f, light);
        vertex(vertexConsumer, pose, x2, y2, z2, normal[0], normal[1], normal[2], red, green, blue, 1f, 1f, 0f, light);
        vertex(vertexConsumer, pose, x3, y3, z3, normal[0], normal[1], normal[2], red, green, blue, 1f, 1f, 1f, light);
        vertex(vertexConsumer, pose, x4, y4, z4, normal[0], normal[1], normal[2], red, green, blue, 1f, 0f, 1f, light);

//        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue, 0f, 0f, light);
//        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue, 1f, 1f, light);
    }

    public static void renderLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
                                  double endX, double endY, double endZ, float red, float green, float blue, float thickness) {
        // Compute the direction vector
        float dx = (float) (endX - startX);
        float dy = (float) (endY - startY);
        float dz = (float) (endZ - startZ);

        // Normalize direction vector
        float length = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (length == 0) return; // Avoid division by zero
        dx /= length;
        dy /= length;
        dz /= length;

        // Create perpendicular offset for thickness (using cross product)
        float offsetX = -dz * thickness / 2;
        float offsetZ = dx * thickness / 2;

        // Define the four corners of the thick line (rectangle)
        float startX1 = (float) startX + offsetX;
        float startZ1 = (float) startZ + offsetZ;
        float startX2 = (float) startX - offsetX;
        float startZ2 = (float) startZ - offsetZ;

        float endX1 = (float) endX + offsetX;
        float endZ1 = (float) endZ + offsetZ;
        float endX2 = (float) endX - offsetX;
        float endZ2 = (float) endZ - offsetZ;

        // Render the rectangle (two triangles forming a quad)

        renderQuad(vertexConsumer, pose, startX1, (float) startY, startZ1, endX1, (float) endY, endZ1, endX2, (float) endY, endZ2, startX2, (float) startY, startZ2, red, green, blue, 15728880);
    }

    public static void renderParabolaLine(VertexConsumer vertexConsumer, PoseStack.Pose pose, double startX, double startY, double startZ,
                                          double endX, double endY, double endZ, float red, float green, float blue, float thickness, int segments, float distance, float gravityMul) {
        double dx = (endX - startX)/segments;
        double dy = (endY - startY)/segments;
        double dz = (endZ - startZ)/segments;

        double prevX = startX;
        double prevY = startY;
        double prevZ = startZ;

        for(int i=1; i<=segments; i++) {
            double s_endX = startX + dx*i;
            double s_endY = startY + dy*i + simulateGravity((distance/segments)*i, distance)*gravityMul;
            double s_endZ = startZ + dz*i;

            double midX = (prevX + s_endX)/2;
            double midY = (prevY + s_endY)/2;
            midY += simulateGravity((distance/segments)*(i - 0.5f), distance)*gravityMul;
            double midZ = (prevZ + s_endZ)/2;

            renderLine(vertexConsumer, pose, prevX, prevY, prevZ, s_endX, s_endY, s_endZ, red, green, blue, thickness);

            prevX = s_endX;
            prevY = s_endY;
            prevZ = s_endZ;
        }
    }

    private static void renderQuadSegment(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                          Vec3[] startCorners, Vec3[] endCorners,
                                          float r, float g, float b, int packedLight,
                                          float uStart, float uEnd) {
        // Render all 4 sides of the tube segment
        for (int i = 0; i < 4; i++) {
            int next = (i + 1) % 4;

            Vec3 v1 = startCorners[i];
            Vec3 v2 = startCorners[next];
            Vec3 v3 = endCorners[next];
            Vec3 v4 = endCorners[i];

            // Calculate normal
            Vec3 edge1 = v2.subtract(v1);
            Vec3 edge2 = v3.subtract(v1);
            Vec3 normal = edge1.cross(edge2).normalize();

            // Render the quad with proper UVs to avoid stretching
            vertex(vertexConsumer, pose, v1.x, v1.y, v1.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uStart, 0, packedLight);
            vertex(vertexConsumer, pose, v2.x, v2.y, v2.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uStart, 1, packedLight);
            vertex(vertexConsumer, pose, v3.x, v3.y, v3.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uEnd, 1, packedLight);
            vertex(vertexConsumer, pose, v4.x, v4.y, v4.z,
                    normal.x, normal.y, normal.z,
                    r, g, b, 1.0f, uEnd, 0, packedLight);
        }
    }





    private static double simulateGravity(float x, float distance) {
        double midpoint = distance;

        return ((double)x-midpoint)*((double)x-midpoint) * (1/(midpoint*midpoint)) - 1;
    }

    public static void renderParabolaTube(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                          Vec3 start, Vec3 end, float r, float g, float b,
                                          float thickness, float distance, int segments, int packedLight, float gravityMul) {
        // Direction vector
        Vec3 dir = end.subtract(start);
        double length = dir.length();
        dir = dir.normalize();

        // Calculate perpendicular vectors for tube cross-section
        Vec3 up = Math.abs(dir.y) > 0.9 ? new Vec3(0, 0, 1) : new Vec3(0, 1, 0);
        Vec3 right = dir.cross(up).normalize().scale(thickness / 2);
        up = right.cross(dir).normalize().scale(thickness / 2);

        // Generate points along the beam
        Vec3[] points = new Vec3[segments + 1];
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            // Add slight curve for visual appeal
            double curve = simulateGravity((distance/segments)*i, distance)*gravityMul;
            points[i] = start.add(dir.scale(t * length))
                    .add(up.scale(curve));
        }

        // Render tube segments with overlapping ends to prevent gaps
        for (int i = 0; i < segments; i++) {
            Vec3 current = points[i];
            Vec3 next = points[i + 1];

            // Get perpendicular vectors at current point
            Vec3 segDir = next.subtract(current).normalize();
            Vec3 segRight = segDir.cross(up).normalize().scale(thickness / 2);
            Vec3 segUp = segRight.cross(segDir).normalize().scale(thickness / 2);

            // Calculate corner positions
            Vec3[] startCorners = {
                    current.add(segRight).add(segUp),
                    current.add(segRight).subtract(segUp),
                    current.subtract(segRight).subtract(segUp),
                    current.subtract(segRight).add(segUp)
            };

            // Get perpendicular vectors at next point
            Vec3 nextSegDir = (i + 2 <= segments) ?
                    points[i + 2].subtract(next).normalize() : segDir;
            Vec3 nextRight = nextSegDir.cross(up).normalize().scale(thickness / 2);
            Vec3 nextUp = nextRight.cross(nextSegDir).normalize().scale(thickness / 2);

            Vec3[] endCorners = {
                    next.add(nextRight).add(nextUp),
                    next.add(nextRight).subtract(nextUp),
                    next.subtract(nextRight).subtract(nextUp),
                    next.subtract(nextRight).add(nextUp)
            };

            // Render the quad segment
            renderQuadSegment(vertexConsumer, pose,
                    startCorners, endCorners,
                    r, g, b, packedLight,
                    (float) i / (segments*0.25f), (float) (i + 1) / (segments*0.25f));
        }
    }

    // Helper functions
    private static void normalize(float[] vector) {
        float length = (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
        if (length != 0) {
            vector[0] /= length;
            vector[1] /= length;
            vector[2] /= length;
        }
    }

    private static float[] cross(float[] a, float[] b) {
        return new float[]{
                a[1] * b[2] - a[2] * b[1],
                a[2] * b[0] - a[0] * b[2],
                a[0] * b[1] - a[1] * b[0]
        };
    }

    private static void scale(float[] vector, float scalar) {
        vector[0] *= scalar;
        vector[1] *= scalar;
        vector[2] *= scalar;
    }





}
