package com.mateussdev.chemosyntehsis.Entities.generic.UniversalTethering;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.ITethered;
import com.mateussdev.chemosyntehsis.Util.Models.BulbSingular;
import com.mateussdev.chemosyntehsis.Util.Rendering.TextureUtilities;
import com.mateussdev.chemosyntehsis.Util.Rendering.TriPlanarUVVertexConsumer;
import com.mateussdev.chemosyntehsis.Util.Rendering.UVScaleableVertexConsumer;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class TetheredOverlayLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    // reuse one bulb model instance (baked layer) - lazy init
    private BulbSingular<?> bulbInstance;

    private final RenderLayerParent<T, M> ownerRenderer;

    private static final ResourceLocation BULB_TEX = new ResourceLocation(Chemosynthesis.MODID, "textures/util/bulb_singular.png");
    private RenderType bulbRenderType = RenderType.entityCutout(BULB_TEX);

    private static final ResourceLocation TINT_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tethered_overlay.png");
    private static final RenderType TINT_RENDER_TYPE = RenderType.entityTranslucent(TINT_TEXTURE);

    private static final ResourceLocation TENDRIL_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tendril_overlay.png");
    private static final RenderType TENDRIL_RENDER_TYPE = RenderType.entityTranslucent(TENDRIL_TEXTURE);

    public TetheredOverlayLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
        this.ownerRenderer = renderer;
    }

    //    @SuppressWarnings("unchecked")
    private void ensureBulbModel() {
        if (this.bulbInstance == null) {
            // bake the model layer (same approach vanilla uses for models)
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(BulbSingular.LAYER_LOCATION);
            this.bulbInstance = new BulbSingular<>(root);
        }
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTick, float age, float headYaw, float headPitch) {
        if (!(entity instanceof ITethered tethered) || !tethered.isTethered()) return;
        //collect all ModelPart instances by reflecting fields
        Set<ModelPart> parts = collectModelParts(this.getParentModel());

        ResourceLocation texLoc = ownerRenderer.getTextureLocation(entity);
        float uvWidth = TextureUtilities.getUvWidthScaleForTexture(texLoc, 4f, 64);
        float uvHeight = TextureUtilities.getUvHeightScaleForTexture(texLoc, 4f, 64);



        VertexConsumer scaledConsumer = new UVScaleableVertexConsumer(buffer.getBuffer(TINT_RENDER_TYPE), uvWidth, uvHeight);
        if (this.getParentModel() instanceof VillagerHeadModel headModel) {
            headModel.hatVisible(false);
        }
        this.getParentModel().renderToBuffer(
                poseStack,
                scaledConsumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                0.5F
        );
        float offset = 1.01f;
        scaledConsumer = new UVScaleableVertexConsumer(buffer.getBuffer(TENDRIL_RENDER_TYPE), uvWidth, uvHeight);
        this.getParentModel().renderToBuffer(
                poseStack,
                scaledConsumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                1F
        );

        if (this.getParentModel() instanceof VillagerHeadModel headModel) {
            headModel.hatVisible(true);
        }

        ensureBulbModel();

        VertexConsumer bulbConsumer = buffer.getBuffer(bulbRenderType);

        for (ModelPart part : parts) {

            float largestW = 0, largestH = 0, largestD = 0;
            float largestMinX = 99999, largestMinY = 99999, largestMinZ = 99999;
            float largestMaxX = -99999, largestMaxY = -99999, largestMaxZ = -99999;

            float partMinX = 99999, partMinY = 99999, partMinZ = 99999;
            float partMaxX = -99999, partMaxY = -99999, partMaxZ = -99999;

            List<ModelPart.Cube> cubes = collectCubesFromPart(part);
            for(ModelPart.Cube cuboid : cubes) {
                //Update mins and maxes
                if(cuboid.minX < partMinX) partMinX = cuboid.minX;
                if(cuboid.minY < partMinY) partMinY = cuboid.minY;
                if(cuboid.minZ < partMinZ) partMinZ = cuboid.minZ;
                if(cuboid.maxX > partMaxX) partMaxX = cuboid.maxX;
                if(cuboid.maxY > partMaxY) partMaxY = cuboid.maxY;
                if(cuboid.maxZ > partMaxZ) partMaxZ = cuboid.maxZ;

                float sizeX = cuboid.maxX - cuboid.minX;
                float sizeY = cuboid.maxY - cuboid.minY;
                float sizeZ = cuboid.maxZ - cuboid.minZ;


                if(sizeX*sizeY*sizeZ > largestW*largestD*largestH) {
                    // Bigger volume
                    largestW = sizeX;
                    largestH = sizeY;
                    largestD = sizeZ;

                    largestMinX = cuboid.minX;
                    largestMinY = cuboid.minY;
                    largestMinZ = cuboid.minZ;

                    largestMaxX = cuboid.maxX;
                    largestMaxY = cuboid.maxY;
                    largestMaxZ = cuboid.maxZ;
                }
            }

            float partSizeX = partMaxX - partMinX;
            float partSizeY = partMaxY - partMinY;
            float partSizeZ = partMaxZ - partMinZ;

            float partVolume = partSizeX * partSizeY * partSizeZ;

            float pivotX = part.x/16f;
            float pivotY = part.y/16f;
            float pivotZ = part.z/16f;

            float partCenterX = partSizeX/2f/16f;
            float partCenterY = partSizeY/2f/16f;
            float partCenterZ = partSizeZ/2f/16f;

            //Do not render if object is too small
            if(partVolume > 4f) {
                poseStack.pushPose();
                //Translate to center point



                poseStack.translate(partMinX/16f + partCenterX, partMinY/16f + partCenterY, partMinZ/16f + partCenterZ);
                poseStack.translate(pivotX, pivotY, pivotZ);
                poseStack.translate(0.5f/16f, 0.5f/16f, 0.5f/16f);


                //Apply default rotation of the part:
                //Apply part rotation
                if (part.xRot != 0.0F || part.yRot != 0.0F || part.zRot != 0.0F) {
                    poseStack.rotateAround((new Quaternionf()).rotationZYX(part.zRot, part.yRot, part.xRot), 0, 0, 0);
                }
                poseStack.rotateAround((new Quaternionf()).rotationZYX(-part.getInitialPose().zRot, -part.getInitialPose().yRot, -part.getInitialPose().xRot), 0, 0, 0);
                //Translate bulb to center of
//                poseStack.translate(pivotX, pivotY, pivotZ);


                //Bulb wobble
                float time = age * 0.1f;
                float wobbleAmount = 0.1f;
                float wobbleX = (float) Math.sin(time + pivotX) * wobbleAmount;
                float wobbleY = (float) Math.cos(time + pivotY) * wobbleAmount;
                float wobbleZ = (float) Math.sin(time * 1.3f + pivotZ) * wobbleAmount;
                poseStack.mulPose(new Quaternionf().rotateXYZ(wobbleX, wobbleY, wobbleZ));

                //Weird arbitrary translation
                poseStack.translate(0f, -1.501F, 0f);

                bulbInstance.renderToBuffer(poseStack, bulbConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

                poseStack.popPose();
            }
        }
    }

    // Collect model parts via reflection
    private Set<ModelPart> collectModelParts(EntityModel<?> model) {
        Set<ModelPart> parts = new LinkedHashSet<>();
        Class<?> cls = model.getClass();
        while (cls != null && cls != EntityModel.class && cls != Object.class) {
            for (Field f : cls.getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    Object val = f.get(model);
                    if (val instanceof ModelPart mp) {
                        parts.add(mp);
                    } else if (val instanceof ModelPart[] arr) {
                        Collections.addAll(parts, arr);
                    } else if (val instanceof Iterable<?> it) {
                        for (Object o : it) if (o instanceof ModelPart mp2) parts.add(mp2);
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            cls = cls.getSuperclass();
        }
        return parts;
    }


    //Collect the cubes from the model part :melvin:
    private List<ModelPart.Cube> collectCubesFromPart(ModelPart part) {
        List<ModelPart.Cube> cubes = new ArrayList<>();
        try {
            Field f = ModelPart.class.getDeclaredField("cubes");
            f.setAccessible(true);
            cubes = (List<ModelPart.Cube>) f.get(part);
        } catch (Exception e) {
            //Failed to get cubes
        }
        return cubes;
    }

    private Quaternionf getOutwardRotation(float x, float y, float z) {
        // Create direction vector from center (0,0,0) to pivot point (x,y,z)
        Vector3f direction = new Vector3f(x, y, z);

        // Normalize the direction vector
        float length = direction.length();
        if (length > 0.0001f) {
            direction.div(length);
        } else {
            // If pivot is at center, default to pointing up
            direction.set(0, 1, 0);
        }

        // Default forward direction for most Minecraft models is (0, 0, 1)
        Vector3f forward = new Vector3f(0, 0, 1);

        // Calculate the rotation needed to rotate forward to direction
        // Using axis-angle rotation
        float dot = forward.dot(direction);

        // If vectors are nearly parallel
        if (Math.abs(dot) > 0.9999f) {
            if (dot > 0) {
                // Same direction, no rotation needed
                return new Quaternionf();
            } else {
                // Opposite direction, rotate 180 degrees around Y
                return new Quaternionf().rotateY((float) Math.PI);
            }
        }

        // Calculate rotation axis and angle
        Vector3f axis = new Vector3f(forward).cross(direction).normalize();
        float angle = (float) Math.acos(dot);

        return new Quaternionf().rotateAxis(angle, axis);
    }
}
