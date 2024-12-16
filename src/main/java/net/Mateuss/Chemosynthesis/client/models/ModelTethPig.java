package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimTetheredPig;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethPig;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelTethPig<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart leg4;
    private final ModelPart leg3;
    private final ModelPart leg2;
    private final ModelPart leg1;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart tentacle1;
    private final ModelPart tentacle2;
    private final ModelPart appendage1;
    private final ModelPart appendage_spike1;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike4;
    private final ModelPart appendage6;
    private final ModelPart appendage_spike6;
    private final ModelPart appendage5;
    private final ModelPart appendage_spike5;
    private final ModelPart appendage7;
    private final ModelPart appendage_spike7;

    public ModelTethPig(ModelPart root) {
        this.root = root.getChild("root");
        this.leg4 = this.root.getChild("leg4");
        this.leg3 = this.root.getChild("leg3");
        this.leg2 = this.root.getChild("leg2");
        this.leg1 = this.root.getChild("leg1");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.tentacle1 = this.head.getChild("tentacle1");
        this.tentacle2 = this.head.getChild("tentacle2");
        this.appendage1 = this.head.getChild("appendage1");
        this.appendage_spike1 = this.appendage1.getChild("appendage_spike1");
        this.appendage2 = this.head.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.appendage3 = this.head.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.appendage4 = this.body.getChild("appendage4");
        this.appendage_spike4 = this.appendage4.getChild("appendage_spike4");
        this.appendage6 = this.body.getChild("appendage6");
        this.appendage_spike6 = this.appendage6.getChild("appendage_spike6");
        this.appendage5 = this.body.getChild("appendage5");
        this.appendage_spike5 = this.appendage5.getChild("appendage_spike5");
        this.appendage7 = this.body.getChild("appendage7");
        this.appendage_spike7 = this.appendage7.getChild("appendage_spike7");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, -5.0F));

        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -6.0F, -5.0F));

        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, 7.0F));

        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -6.0F, 7.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(28, 8).addBox(-5.0F, -10.0F, -7.0F, 10.0F, 16.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.0F, 2.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -1.5F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.5F, -8.25F, 0.087F, 0.0068F, -0.0782F));

        PartDefinition tentacle1 = head.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.offset(1.75F, 1.5F, -8.75F));

        PartDefinition cube_r1 = tentacle1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(37, -5).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition tentacle2 = head.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.offset(-1.5F, 1.75F, -8.75F));

        PartDefinition cube_r2 = tentacle2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(37, -2).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0436F, 0.0F, 0.0F));

        PartDefinition appendage1 = head.addOrReplaceChild("appendage1", CubeListBuilder.create().texOffs(52, 0).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.5F, -6.0F, -0.4363F, 0.0F, 3.1416F));

        PartDefinition appendage_spike1 = appendage1.addOrReplaceChild("appendage_spike1", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage2 = head.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(52, 0).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.5F, -4.0F, 0.043F, 0.0076F, 2.9672F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage3 = head.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(52, 0).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.5F, -2.0F, 0.4608F, -0.1393F, -2.8689F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage4 = body.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(52, 0).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -3.5F, -1.0F, 1.5392F, -0.6533F, 2.9684F));

        PartDefinition appendage_spike4 = appendage4.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = body.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(52, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -3.5F, -1.0F, 1.5392F, 0.6533F, -2.9684F));

        PartDefinition appendage_spike6 = appendage6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage5 = body.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(52, 0).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 0.5F, 0.0F, 1.9218F, -0.7903F, 3.1318F));

        PartDefinition appendage_spike5 = appendage5.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage7 = body.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(52, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.5F, 0.0F, 1.9218F, 0.7903F, -3.1318F));

        PartDefinition appendage_spike7 = appendage7.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimTetheredPig.TETH_PIG_SPRINT, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityTethPig) entity).AS_isIdle, AnimTetheredPig.TETH_PIG_IDLE, ageInTicks, 1f);
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