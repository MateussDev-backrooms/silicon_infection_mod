package com.mateussdev.chemosyntehsis.Entities.generic.UniversalTethering;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.ITethered;
import com.mateussdev.chemosyntehsis.Util.Models.BulbSingular;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.*;

public class TetheredOverlayLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    // reuse one bulb model instance (baked layer) - lazy init
    private BulbSingular<?> bulbInstance;

    private static final ResourceLocation BULB_TEX = new ResourceLocation(Chemosynthesis.MODID, "textures/util/bulb_singular.png");
    private RenderType bulbRenderType = RenderType.entityCutout(BULB_TEX);

    private static final ResourceLocation TINT_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tethered_overlay.png");
    private static final RenderType TINT_RENDER_TYPE = RenderType.entityTranslucent(TINT_TEXTURE);

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
        poseStack.popPose();

        ensureBulbModel();

        // Grab the parent model instance (the model that this RenderLayer is attached to)
        M parentModel = this.getParentModel();

        // collect all ModelPart instances by reflecting fields (works across different vanilla/mod models)
        Set<ModelPart> parts = collectModelParts(parentModel);

        // VertexConsumer for bulb
        VertexConsumer bulbConsumer = buffer.getBuffer(bulbRenderType);

        // For each model part, ask it to iterate its cuboids and give us the transform + cuboid
        for (ModelPart part : parts) {
            poseStack.pushPose();

            float partX = part.x;
            float partY = part.y;
            float partZ = part.z;

            part.translateAndRotate(poseStack);
            poseStack.translate(partX, partY, partZ);
            bulbInstance.renderToBuffer(poseStack, bulbConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

            poseStack.popPose();
        }
    }

    // Reflection utility: find all ModelPart fields and any Iterable<ModelPart> / ModelPart[]
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
