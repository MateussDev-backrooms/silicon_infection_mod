package com.mateussdev.chemosyntehsis.Systems.UniversalTethering;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Util.Models.BulbModel;
import com.mateussdev.chemosyntehsis.Util.Models.BulbSingular;
import com.mateussdev.chemosyntehsis.Util.Rendering.TextureUtilities;
import com.mateussdev.chemosyntehsis.Util.Rendering.UVScaleableVertexConsumer;
import com.mateussdev.chemosyntehsis.Util.StaticRenderingMethods;
import com.mateussdev.chemosyntehsis.Util.StaticSiliconiteMethods;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.*;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.*;

public class TetheredGeoOverlayLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    private BulbModel bulbModel;
    private BulbSingular<?> bulbInstance;
    public TetheredGeoOverlayLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
        this.bulbModel = new BulbModel();
    }

    private static final Map<BakedGeoModel, List<GeoBone>> MODEL_PARTS_CACHE = new HashMap<>();

    private static final ResourceLocation BULB_TEX = new ResourceLocation(Chemosynthesis.MODID, "textures/util/bulb_singular.png");
    private final RenderType BULB_RENDER_TYPE = RenderType.entityCutout(BULB_TEX);

    private static final ResourceLocation TINT_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tethered_overlay.png");
    private static final RenderType TINT_RENDER_TYPE = RenderType.entityTranslucent(TINT_TEXTURE);

    private static final ResourceLocation TENDRIL_TEXTURE = new ResourceLocation(Chemosynthesis.MODID, "textures/util/tendril_overlay.png");
    private static final RenderType TENDRIL_RENDER_TYPE = RenderType.entityTranslucent(TENDRIL_TEXTURE);

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        //only render if the mob is universally tethered
        if (animatable instanceof Mob mob) {
            if(!UniversalTethering.isTethered(mob)) return;
        }

        //Prevent any entity from my mod from rendering ts
        if(animatable instanceof Entity entity) {
            if(StaticSiliconiteMethods.isEntityFromChemosynthesisMod(entity)) return;
        }

        ResourceLocation texLoc = getRenderer().getTextureLocation(animatable);
        float uvWidth = TextureUtilities.getUvWidthScaleForTexture(texLoc, 4f, 64);
        float uvHeight = TextureUtilities.getUvHeightScaleForTexture(texLoc, 4f, 64);


        //Render overlays
        VertexConsumer scaledConsumer = new UVScaleableVertexConsumer(bufferSource.getBuffer(TINT_RENDER_TYPE), uvWidth, uvHeight);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType, scaledConsumer, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

//        StaticRenderingMethods.renderDebugCube(
//                buffer, poseStack, poseStack.last(), 0, 2, 0, 1, 1, 1, 1, 1, 1, 1, LightTexture.FULL_BRIGHT
//        );

        scaledConsumer = new UVScaleableVertexConsumer(bufferSource.getBuffer(TENDRIL_RENDER_TYPE), uvWidth, uvHeight);
        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, renderType, scaledConsumer, partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

    }

    @Override
    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        //only render if the mob is universally tethered
        if (animatable instanceof Mob mob) {
            if(!UniversalTethering.isTethered(mob)) return;
        }

        //Prevent any entity from my mod from rendering ts
        if(animatable instanceof Entity entity) {
            if(StaticSiliconiteMethods.isEntityFromChemosynthesisMod(entity)) return;
        }

        if (bone.getCubes().isEmpty()) return;

        //TODO: Add bulbs to ts cuz man this is bulb positioning all over again, but even more annoying
//        poseStack.pushPose();
//        poseStack.translate(0f, 1.501f, 0f);
//        VertexConsumer bulbConsumer = bufferSource.getBuffer(BULB_RENDER_TYPE);
//        ensureBulbModel();
//        bulbInstance.renderToBuffer(poseStack, bulbConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
//        poseStack.popPose();

    }

    private float[] getBoneBounds(GeoBone bone) {
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        for (GeoCube cube : bone.getCubes()) {
            Vec3 size = cube.size();
            Vec3 pivot = cube.pivot();

            // GeckoLib pivot is center of cube, size is full extent
            float halfX = (float) (size.x / 2f);
            float halfY = (float) (size.y / 2f);
            float halfZ = (float) (size.z / 2f);

            if (pivot.x - halfX < minX) minX = (float) (pivot.x - halfX);
            if (pivot.y - halfY < minY) minY = (float) (pivot.y - halfY);
            if (pivot.z - halfZ < minZ) minZ = (float) (pivot.z - halfZ);
            if (pivot.x + halfX > maxX) maxX = (float) (pivot.x + halfX);
            if (pivot.y + halfY > maxY) maxY = (float) (pivot.y + halfY);
            if (pivot.z + halfZ > maxZ) maxZ = (float) (pivot.z + halfZ);
        }

        return new float[]{minX, minY, minZ, maxX, maxY, maxZ};
    }

    private void ensureBulbModel() {
        if (this.bulbInstance == null) {
            // bake the model layer (same approach vanilla uses for models)
            ModelPart root = Minecraft.getInstance().getEntityModels().bakeLayer(BulbSingular.LAYER_LOCATION);
            this.bulbInstance = new BulbSingular<>(root);
        }
    }
}
