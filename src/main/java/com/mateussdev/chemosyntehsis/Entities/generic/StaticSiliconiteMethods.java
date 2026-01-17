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
        Minecraft.getInstance().player.sendSystemMessage(Component.literal(text));
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
        return a * (1 - a) + b * a;
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
            EntityType.TROPICAL_FISH
    );
    public static boolean shouldAttackMob(LivingEntity entity) {
        if(entity instanceof BaseSiliconite) return false;
        if(isMobFromChemosynthesisMod(entity)) return false;
        if(BLACKLISTED_MOBS.contains(entity.getType())) return false;
        return true;
    }

    // ===== RENDERING ===== //

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

    public static void renderQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose,
                                  float x1, float y1, float z1, float x2, float y2, float z2,
                                  float x3, float y3, float z3, float x4, float y4, float z4,
                                  float red, float green, float blue, int light) {
        float[] normal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);
        // Define the quad vertices with all required attributes
        vertex(vertexConsumer, pose, x1, y1, z1, normal, red, green, blue, 0f, 0f, light);
        vertex(vertexConsumer, pose, x2, y2, z2, normal, red, green, blue, 1f, 0f, light);
        vertex(vertexConsumer, pose, x3, y3, z3, normal, red, green, blue, 1f, 1f, light);
        vertex(vertexConsumer, pose, x4, y4, z4, normal, red, green, blue, 0f, 1f, light);

//        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue, 0f, 0f, light);
//        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue, 1f, 1f, light);
    }
    public static void renderShadedQuad(VertexConsumer vertexConsumer, PoseStack.Pose pose, Level level,
                                        float x1, float y1, float z1, float x2, float y2, float z2,
                                        float x3, float y3, float z3, float x4, float y4, float z4,
                                        float red, float green, float blue) {
        float[] normal = calculateNormal(x1, y1, z1, x2, y2, z2, x3, y3, z3);

        // Define the quad vertices with all required attributes

        vertex(vertexConsumer, pose, x1, y1, z1, normal, red, green, blue, 0f, 0f, getPackedLight(level, floatsToBlockPos(x1, y1, z1)));
        vertex(vertexConsumer, pose, x2, y2, z2, normal, red, green, blue, 1f, 0f, getPackedLight(level, floatsToBlockPos(x2, y2, z2)));
        vertex(vertexConsumer, pose, x3, y3, z3, normal, red, green, blue, 1f, 1f, getPackedLight(level, floatsToBlockPos(x3, y3, z3)));
        vertex(vertexConsumer, pose, x4, y4, z4, normal, red, green, blue, 0f, 1f, getPackedLight(level, floatsToBlockPos(x4, y4, z4)));

//        vertex(vertexConsumer, pose, x1, y1, z1, red, green, blue, 0f, 0f, light);
//        vertex(vertexConsumer, pose, x3, y3, z3, red, green, blue, 1f, 1f, light);
    }

    public static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, double x, double y, double z, float[] normal, float red, float green, float blue, float u, float v, int light) {
        vertexConsumer.vertex(pose.pose(), (float) x, (float) y, (float) z)
                .color(red, green, blue, 1.0F) // Color
                .uv(u, v)               // Texture UV
                .overlayCoords(OverlayTexture.NO_OVERLAY) // Overlay texture
                .uv2(light)                // Lightmap texture (full brightness)
                .normal(pose.normal(), 0.0F, 1.0F, 0.0F) // Normal vector (arbitrary up)
                .endVertex();
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

    private static double simulateGravity(float x, float distance) {
        double midpoint = distance/2;

        return ((double)x-midpoint)*((double)x-midpoint) * (1/(midpoint*midpoint)) - 1;
    }

    public static void renderParabolaTube(VertexConsumer vertexConsumer, PoseStack.Pose pose, int light, double startX, double startY, double startZ,
                                          double endX, double endY, double endZ, float red, float green, float blue, float thickness,
                                          int segments, float distance, float gravityMul) {
        double dx = (endX - startX) / segments;
        double dy = (endY - startY) / segments;
        double dz = (endZ - startZ) / segments;

        double prevX = startX;
        double prevY = startY;
        double prevZ = startZ;

        // Calculate initial perpendicular vectors
        float[] normal = {0, 1, 0}; // Arbitrary up vector for initial perpendicular calculation

        for (int i = 1; i <= segments; i++) {
            double s_endX = startX + dx * i;
            double s_endY = startY + dy * i + simulateGravity((distance / segments) * i, distance) * gravityMul;
            double s_endZ = startZ + dz * i;

            double midX = (prevX + s_endX) / 2;
            double midY = (prevY + s_endY) / 2;
            midY += simulateGravity((distance / segments) * (i - 0.5f), distance) * gravityMul;
            double midZ = (prevZ + s_endZ) / 2;

            // Calculate direction of segment
            float[] direction = {(float) (s_endX - prevX), (float) (s_endY - prevY), (float) (s_endZ - prevZ)};
            normalize(direction);

            // Compute perpendicular vectors
            float[] perp1 = cross(normal, direction);
            float[] perp2 = cross(direction, perp1);
            normalize(perp1);
            normalize(perp2);

            // Scale by thickness
            scale(perp1, thickness / 2);
            scale(perp2, thickness / 2);

            // Define corners of the square tube at start and end of the segment
            float[][] startCorners = new float[4][3];
            float[][] endCorners = new float[4][3];

            // Calculate corner positions
            calculateCorners(prevX, prevY, prevZ, perp1, perp2, startCorners);
            calculateCorners(s_endX, s_endY, s_endZ, perp1, perp2, endCorners);

            // Render quads between the corners
            for (int j = 0; j < 4; j++) {
                int next = (j + 1) % 4;

                renderQuad(vertexConsumer, pose,
                        startCorners[j][0], startCorners[j][1], startCorners[j][2],
                        startCorners[next][0], startCorners[next][1], startCorners[next][2],
                        endCorners[next][0], endCorners[next][1], endCorners[next][2],
                        endCorners[j][0], endCorners[j][1], endCorners[j][2],
                        red, green, blue, light);
            }

            // Update previous segment endpoint
            prevX = s_endX;
            prevY = s_endY;
            prevZ = s_endZ;
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

    private static void calculateCorners(double x, double y, double z, float[] perp1, float[] perp2, float[][] corners) {
        corners[0] = new float[]{(float) (x + perp1[0] + perp2[0]), (float) (y + perp1[1] + perp2[1]), (float) (z + perp1[2] + perp2[2])};
        corners[1] = new float[]{(float) (x - perp1[0] + perp2[0]), (float) (y - perp1[1] + perp2[1]), (float) (z - perp1[2] + perp2[2])};
        corners[2] = new float[]{(float) (x - perp1[0] - perp2[0]), (float) (y - perp1[1] - perp2[1]), (float) (z - perp1[2] - perp2[2])};
        corners[3] = new float[]{(float) (x + perp1[0] - perp2[0]), (float) (y + perp1[1] - perp2[1]), (float) (z + perp1[2] - perp2[2])};
    }

    private static int getPackedLight(Level world, BlockPos pos) {
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos); // Block light level
        int skyLight = world.getBrightness(LightLayer.SKY, pos);    // Sky light level
        return LightTexture.pack(blockLight, skyLight);
    }

    private static BlockPos floatsToBlockPos(float x, float y, float z) {
        int bx = Mth.floor(x);
        int by = Mth.floor(y);
        int bz = Mth.floor(z);

        return new BlockPos(bx, by, bz);
    }

    public static float[] RGBtoHSB(float r, float g, float b) {
        int ir = Mth.floor(r*255);
        int ig = Mth.floor(g*255);
        int ib = Mth.floor(b*255);
        return Color.RGBtoHSB(ir, ig, ib, null);
    }

    public static float[] HSBtoRGB(float h, float s, float b) {
        int rgb = Color.HSBtoRGB(h, s, b);

        int red = (rgb>>16)&0xFF;

        int green = (rgb>>8)&0xFF;

        int blue = rgb&0xFF;

        float _r = (float) red / 255f;
        float _g = (float) green / 255f;
        float _b = (float) blue / 255f;

        return new float[]{_r, _g, _b};
    }

    public static void shake(PoseStack pPoseStack, int shakeTimer, float freq, float power) {
        float shakeyX = (float) Math.sin(shakeTimer * freq) * power;
        float shakeyY = (float) Math.cos(shakeTimer * freq) * power;
        pPoseStack.translate(shakeyX, 0, shakeyY);
    }



}
