package com.mateussdev.chemosyntehsis.Entities.Projectiles.bulb_harpoon;

import com.mateussdev.chemosyntehsis.Chemosynthesis;
import com.mateussdev.chemosyntehsis.Entities.generic.StaticSiliconiteMethods;
import com.mateussdev.chemosyntehsis.Util.Models.StemModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BulbHarpoonEntity_Render extends GeoEntityRenderer<BulbHarpoonEntity> {

    private final StemModel stemModel;

    public BulbHarpoonEntity_Render(EntityRendererProvider.Context context) {
        super(context, new BulbHarpoonEntity_Model());
        this.stemModel = new StemModel();
    }

    @Override
    public void preRender(PoseStack poseStack, BulbHarpoonEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(!isReRender) {
            poseStack.translate(0, -0.01f, 0);
        }
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public boolean shouldRender(BulbHarpoonEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void actuallyRender(PoseStack poseStack, BulbHarpoonEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        if(isReRender) return;

        if(animatable.getOwner() == null) return;

        Vec3 parentPos = animatable.getOwner().getPosition(partialTick);
        Vec3 harpoonPos = animatable.getEyePosition(partialTick);

        double dst = parentPos.distanceTo(harpoonPos);
        double singleLength = 0.45d;
        RenderType rendertype1 = RenderType.entitySolid(new ResourceLocation(Chemosynthesis.MODID, "textures/entity/stem.png"));

        int segmentCount = Mth.floor(dst/singleLength);
        // Calculate the Direction Vector from Parent to Harpoon
        Vec3 delta = parentPos.subtract(harpoonPos);
        Vec3 dir = delta.normalize();

// Model faces -Z, so forward is inverted
        Vector3f forward = new Vector3f((float)-dir.x, (float)-dir.y, (float)-dir.z);

// Choose a stable up vector
        Vector3f up = Math.abs(forward.y) > 0.99f
                ? new Vector3f(1, 0, 0)
                : new Vector3f(0, 1, 0);

        Vector3f right = up.cross(forward, new Vector3f()).normalize();
        up = forward.cross(right, new Vector3f()).normalize();

        float roll = (animatable.tickCount + partialTick) * 0.2f;

        Matrix3f rollMat = new Matrix3f()
                .rotate(roll, forward);

        rollMat.transform(right);
        rollMat.transform(up);

//        float yaw = (float) Math.atan2(forward.x, forward.z);
//        float pitch = (float) Math.asin(-forward.y);

// Roll is now encoded in up/right, extract it
        float computedRoll = (float) Math.atan2(
                right.y,
                up.y
        );



        float yaw = (float) Math.atan2(delta.z, delta.x);

        float horizontalDist = (float) Math.sqrt(delta.x * delta.x + delta.z * delta.z);
        float pitch = (float) Math.atan2(delta.y, horizontalDist);

        for (int i = 0; i < segmentCount; i++) {
            BakedGeoModel bakedGeoModel = stemModel.getBakedModel(stemModel.getModelResource(animatable));
            GeoBone bone = bakedGeoModel.topLevelBones().get(0);
            GeoBone inner_bone = bakedGeoModel.getBone("stem5").get();


            float t = (float)i / segmentCount;
            bone.setPosX((float) (delta.x * t) * 16f);
            bone.setPosY((float) (delta.y * t) * 16f);
            bone.setPosZ((float) (delta.z * t) * -16f);

            bone.setRotX(-pitch);
            bone.setRotY(-yaw - Mth.PI/2);
            inner_bone.setRotZ((float) (computedRoll + Math.toDegrees(i/3f)));



            this.reRender(bakedGeoModel, poseStack, bufferSource, animatable, rendertype1, bufferSource.getBuffer(rendertype1), partialTick, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }


}
