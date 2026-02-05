package com.mateussdev.chemosyntehsis.Entities.generic.UniversalTethering;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.ITethered;
import com.mateussdev.chemosyntehsis.Util.Models.BulbSingular;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.util.*;

public class TetheredOverlayLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    // reuse one bulb model instance (baked layer) - lazy init
    private BulbSingular<?> bulbInstance;

    private static final ResourceLocation BULB_TEX = new ResourceLocation(Chemosynthesis.MODID, "textures/util/bulb_singular.png");
    private RenderType bulbRenderType = RenderType.entityCutout(BULB_TEX);

    private static final ResourceLocation TINT_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tethered_overlay.png");
    private static final RenderType TINT_RENDER_TYPE = RenderType.entityTranslucent(TINT_TEXTURE);

    private static final ResourceLocation TENDRIL_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tendril_overlay.png");
    private static final RenderType TENDRIL_RENDER_TYPE = RenderType.entityTranslucent(TENDRIL_TEXTURE);

    public TetheredOverlayLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
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

        VertexConsumer scaledConsumer = new UVScaledVertexConsumer(buffer.getBuffer(TINT_RENDER_TYPE), 4f, 4f);
        poseStack.pushPose();
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
        poseStack.scale(offset, offset, offset);
        scaledConsumer = new UVScaledVertexConsumer(buffer.getBuffer(TENDRIL_RENDER_TYPE), 4f, 4f);
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
        poseStack.scale(offset+0.01f, offset+0.01f, offset+0.01f);
        poseStack.popPose();

        ensureBulbModel();


        VertexConsumer bulbConsumer = buffer.getBuffer(bulbRenderType);

        for (ModelPart part : parts) {
            poseStack.pushPose();

            float partoffsetX = part.x/16f, partoffsetY = part.y/16f, partoffsetZ = part.z/16f;


            // Translate to pivot point
            poseStack.translate(partoffsetX, partoffsetY, partoffsetZ);

            // Get rotation to point outward
            Quaternionf outwardRotation = getOutwardRotation(partoffsetX, partoffsetY, partoffsetZ);
            poseStack.mulPose(outwardRotation);

            // Optional: Add slight animation/wobble
            float time = age * 0.1f;
            float wobbleAmount = 0.05f;
            float wobbleX = (float) Math.sin(time + partoffsetX) * wobbleAmount;
            float wobbleY = (float) Math.cos(time + partoffsetY) * wobbleAmount;
            float wobbleZ = (float) Math.sin(time * 1.3f + partoffsetZ) * wobbleAmount;
            poseStack.mulPose(new Quaternionf().rotateXYZ(wobbleX, wobbleY, wobbleZ));

            if (part.xRot != 0.0F || part.yRot != 0.0F || part.zRot != 0.0F) {
                poseStack.rotateAround((new Quaternionf()).rotationZYX(part.zRot, part.yRot, part.xRot), 0, 0, 0);
            }

            if (part.xScale != 1.0F || part.yScale != 1.0F || part.zScale != 1.0F) {
                poseStack.scale(part.xScale, part.yScale, part.zScale);
            }
            poseStack.translate(0f, -1.501F, 0f);

            bulbInstance.renderToBuffer(poseStack, bulbConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

            poseStack.popPose();
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


    // Custom VertexConsumer that scales UV coordinates
    private static class UVScaledVertexConsumer implements VertexConsumer {
        private final VertexConsumer wrapped;
        private final float uScale;
        private final float vScale;

        public UVScaledVertexConsumer(VertexConsumer wrapped, float uScale, float vScale) {
            this.wrapped = wrapped;
            this.uScale = uScale;
            this.vScale = vScale;
        }

        @Override
        public VertexConsumer uv(float u, float v) { // Scale UV coordinates to make texture repeat
            return wrapped.uv(u * uScale, v * vScale);
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            return wrapped.vertex(x, y, z);
        }

        @Override
        public VertexConsumer color(int red, int green, int blue, int alpha) {
            return wrapped.color(red, green, blue, alpha);
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            return wrapped.overlayCoords(u, v);
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            return wrapped.uv2(u, v);
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            return wrapped.normal(x, y, z);
        }

        @Override
        public void endVertex() {
            wrapped.endVertex();
        }

        @Override
        public void defaultColor(int r, int g, int b, int a) {
            wrapped.defaultColor(r, g, b, a);
        }

        @Override
        public void unsetDefaultColor() {
            wrapped.unsetDefaultColor();
        }
    }
}
