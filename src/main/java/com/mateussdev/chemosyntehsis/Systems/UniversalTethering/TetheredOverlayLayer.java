package com.mateussdev.chemosyntehsis.Systems.UniversalTethering;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.Models.BulbSingular;
import com.mateussdev.chemosyntehsis.Util.Rendering.TextureUtilities;
import com.mateussdev.chemosyntehsis.Util.Rendering.UVScaleableVertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.joml.Quaternionf;

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

        //only render if the mob is universally tethered
        if (entity instanceof Mob mob) {
            if(!UniversalTethering.isTethered(mob)) return;
        }
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

            float faceXSize = partSizeZ*partSizeY;
            float faceYSize = partSizeX*partSizeZ;
            float faceZSize = partSizeX*partSizeY;

            float pivotX = part.x/16f;
            float pivotY = part.y/16f;
            float pivotZ = part.z/16f;

            float partCenterX = partSizeX/2f/16f;
            float partCenterY = partSizeY/2f/16f;
            float partCenterZ = partSizeZ/2f/16f;

            //Do not render if object is too small
            if(partVolume > 4f) {

                //Calculate how many bulbs will fit on the face in terms of U and V
                int cntU = 0;
                int cntV = 0;

                float spacing = 10f;

                //Add bulbs to all faces
                for(int i=0; i<6; i++) {

                    switch(i) {
                        case 0, 3:
                            cntU = Math.max(1, Mth.floor(partSizeZ / spacing));
                            cntV = Math.max(1, Mth.floor(partSizeY / spacing));
                            break;
                        case 1, 4:
                            cntU = Math.max(1, Mth.floor(partSizeZ / spacing));
                            cntV = Math.max(1, Mth.floor(partSizeX / spacing));
                            break;
                        case 2, 5:
                            cntU = Math.max(1, Mth.floor(partSizeX / spacing));
                            cntV = Math.max(1, Mth.floor(partSizeY / spacing));
                            break;
                    }

                    int dir = i>2 ? 1 : -1;

                    //Render grid with two for loops
                    for(int u=0; u<cntU; u++) {
                        for(int v=0; v<cntV; v++) {

                            poseStack.pushPose();
                            //Translate to center point

                            float offsetX = 0;
                            float offsetY = 0;
                            float offsetZ = 0;

                            //Apply offset depending on domAxis
                            switch (i) {
                                case 0, 3:
                                    offsetX = partSizeX/2f/16f * dir;
                                    offsetZ = (u*spacing/16f) - (partSizeZ/2f/16f);
                                    offsetY = (v*spacing/16f) - (partSizeY/2f/16f);
                                    break;
                                case 1, 4:
                                    offsetY = partSizeY/2f/16f * dir;
                                    offsetZ = (u*spacing/16f) - (partSizeZ/2f/16f);
                                    offsetX = (v*spacing/16f) - (partSizeX/2f/16f);
                                    break;
                                case 2, 5:
                                    offsetZ = partSizeZ/2f/16f * dir;
                                    offsetX = (u*spacing/16f) - (partSizeX/2f/16f);
                                    offsetY = (v*spacing/16f) - (partSizeY/2f/16f);
                                    break;
                            }

                            poseStack.translate(pivotX, pivotY, pivotZ);
                            //Apply default rotation of the part:
                            //Apply part rotation
                            if (part.xRot != 0.0F || part.yRot != 0.0F || part.zRot != 0.0F) {
                                poseStack.rotateAround((new Quaternionf()).rotationZYX(part.zRot, part.yRot, part.xRot), 0, 0, 0);
                            }
                            poseStack.rotateAround((new Quaternionf()).rotationZYX(-part.getInitialPose().zRot, -part.getInitialPose().yRot, -part.getInitialPose().xRot), 0, 0, 0);
                            //Translate bulb
                            poseStack.translate(partMinX/16f + partCenterX + offsetX, partMinY/16f + partCenterY + offsetY, partMinZ/16f + partCenterZ + offsetZ);

                            //middle of pixel
                            poseStack.translate(0.5f/16f, 0.5f/16f, 0.5f/16f);

                            //center of cell



            //                poseStack.translate(pivotX, pivotY, pivotZ);

                            Quaternionf normalRot = switch (i) {
                                case 0 -> new Quaternionf().rotationY( -(float) Math.PI / 2f);   // -Z to -X
                                case 3 -> new Quaternionf().rotationY((float) Math.PI / 2f);   // -Z to +X
                                case 1 -> new Quaternionf().rotationX((float) Math.PI / 2f);   // -Z to -Y
                                case 4 -> new Quaternionf().rotationX( -(float) Math.PI / 2f);   // -Z to +Y
                                case 2 -> new Quaternionf().rotationY( (float) Math.PI);        // -Z to +Z
                                case 5 -> new Quaternionf();                                      //Keep as is
                                default -> new Quaternionf();
                            };
                            poseStack.mulPose(normalRot);


                            //Bulb wobble
                            float time = age * 0.1f;
                            float wobbleAmount = 0.1f;
                            float wobbleX = (float) Math.sin(time + u*148571) * wobbleAmount;
                            float wobbleY = (float) Math.cos(time + v*368368) * wobbleAmount;
                            float wobbleZ = (float) Math.sin(time * 1.3f + u*24627) * wobbleAmount;
                            poseStack.mulPose(new Quaternionf().rotateXYZ(wobbleX, wobbleY, wobbleZ));

                            //Weird arbitrary translation
                            poseStack.translate(0f, -1.501F, 0f);

                            bulbInstance.renderToBuffer(poseStack, bulbConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

                            poseStack.popPose();
                        }
                    }
                }
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
}
