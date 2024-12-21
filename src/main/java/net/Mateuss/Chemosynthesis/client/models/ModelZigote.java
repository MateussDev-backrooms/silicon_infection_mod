package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimSiliconRoller;
import net.Mateuss.Chemosynthesis.client.animations.AnimZigote;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityOrganelleZigote;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelZigote<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart bulb1;
    private final ModelPart bulb2;
    private final ModelPart leg1;
    private final ModelPart knee1;
    private final ModelPart leg2;
    private final ModelPart knee2;
    private final ModelPart leg3;
    private final ModelPart knee3;
    private final ModelPart leg4;
    private final ModelPart knee4;

    public ModelZigote(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.bulb1 = this.body.getChild("bulb1");
        this.bulb2 = this.body.getChild("bulb2");
        this.leg1 = this.root.getChild("leg1");
        this.knee1 = this.leg1.getChild("knee1");
        this.leg2 = this.root.getChild("leg2");
        this.knee2 = this.leg2.getChild("knee2");
        this.leg3 = this.root.getChild("leg3");
        this.knee3 = this.leg3.getChild("knee3");
        this.leg4 = this.root.getChild("leg4");
        this.knee4 = this.leg4.getChild("knee4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -2.5F, -2.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.5F, 0.0F));

        PartDefinition bulb1 = body.addOrReplaceChild("bulb1", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3677F, -1.4992F, 0.4488F, 0.2597F, 0.0338F, 3.0151F));

        PartDefinition bulb2 = body.addOrReplaceChild("bulb2", CubeListBuilder.create().texOffs(0, 21).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 10).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8823F, -1.4992F, -1.0512F, -0.0725F, 0.0623F, -2.9198F));

        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create(), PartPose.offset(2.0F, -2.5F, -2.0F));

        PartDefinition cube_r1 = leg1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -1.0F, -0.4363F, -0.7854F, 0.0F));

        PartDefinition knee1 = leg1.addOrReplaceChild("knee1", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -0.75F, -2.0F));

        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create(), PartPose.offset(-2.0F, -2.5F, -2.0F));

        PartDefinition cube_r2 = leg2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -1.0F, -0.4363F, 0.7854F, 0.0F));

        PartDefinition knee2 = leg2.addOrReplaceChild("knee2", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -0.75F, -2.0F));

        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create(), PartPose.offset(-2.0F, -2.5F, 2.0F));

        PartDefinition cube_r3 = leg3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.0F, 1.0F, 0.4363F, -0.7854F, 0.0F));

        PartDefinition knee3 = leg3.addOrReplaceChild("knee3", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -0.75F, 2.0F));

        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create(), PartPose.offset(2.0F, -2.5F, 2.0F));

        PartDefinition cube_r4 = leg4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 1.0F, 0.4363F, 0.7854F, 0.0F));

        PartDefinition knee4 = leg4.addOrReplaceChild("knee4", CubeListBuilder.create().texOffs(4, 21).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -0.75F, 2.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimZigote.ZIGOTE_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityOrganelleZigote) entity).AS_isIdle, AnimZigote.ZIGOTE_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}