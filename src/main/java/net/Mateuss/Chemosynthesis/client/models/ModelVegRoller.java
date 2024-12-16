package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimVegRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.vegetative.EntityVegRoller;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelVegRoller<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart bulb1;
    private final ModelPart appendage_spike2;
    private final ModelPart bulb2;
    private final ModelPart appendage_spike3;
    private final ModelPart bulb3;
    private final ModelPart appendage_spike4;
    private final ModelPart bulb4;
    private final ModelPart appendage_spike5;
    private final ModelPart bulb5;
    private final ModelPart appendage_spike7;
    private final ModelPart bulb6;
    private final ModelPart appendage_spike6;
    private final ModelPart bulb7;
    private final ModelPart appendage_spike8;
    private final ModelPart bulb8;
    private final ModelPart appendage_spike9;
    private final ModelPart tendril1;
    private final ModelPart tendril2;
    private final ModelPart tendril4;
    private final ModelPart tendril3;
    private final ModelPart tendril5;

    public ModelVegRoller(ModelPart root) {
        this.root = root.getChild("root");
        this.bulb1 = this.root.getChild("bulb1");
        this.appendage_spike2 = this.bulb1.getChild("appendage_spike2");
        this.bulb2 = this.root.getChild("bulb2");
        this.appendage_spike3 = this.bulb2.getChild("appendage_spike3");
        this.bulb3 = this.root.getChild("bulb3");
        this.appendage_spike4 = this.bulb3.getChild("appendage_spike4");
        this.bulb4 = this.root.getChild("bulb4");
        this.appendage_spike5 = this.bulb4.getChild("appendage_spike5");
        this.bulb5 = this.root.getChild("bulb5");
        this.appendage_spike7 = this.bulb5.getChild("appendage_spike7");
        this.bulb6 = this.root.getChild("bulb6");
        this.appendage_spike6 = this.bulb6.getChild("appendage_spike6");
        this.bulb7 = this.root.getChild("bulb7");
        this.appendage_spike8 = this.bulb7.getChild("appendage_spike8");
        this.bulb8 = this.root.getChild("bulb8");
        this.appendage_spike9 = this.bulb8.getChild("appendage_spike9");
        this.tendril1 = this.root.getChild("tendril1");
        this.tendril2 = this.root.getChild("tendril2");
        this.tendril4 = this.root.getChild("tendril4");
        this.tendril3 = this.root.getChild("tendril3");
        this.tendril5 = this.root.getChild("tendril5");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(8, 24).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition bulb1 = root.addOrReplaceChild("bulb1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, 0.0F, 0.0F, 0.0F, -2.4871F));

        PartDefinition appendage_spike2 = bulb1.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb2 = root.addOrReplaceChild("bulb2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, 1.0F, 0.0F, 0.0F, 0.0F, 2.4871F));

        PartDefinition appendage_spike3 = bulb2.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 9.0F, -0.5F));

        PartDefinition bulb3 = root.addOrReplaceChild("bulb3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 1.5708F, -0.9163F, 1.5708F));

        PartDefinition appendage_spike4 = bulb3.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 9.0F, -0.5F));

        PartDefinition bulb4 = root.addOrReplaceChild("bulb4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 1.0F, -2.0F, -1.5708F, 0.9163F, 1.5708F));

        PartDefinition appendage_spike5 = bulb4.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, -9.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 9.0F, 0.5F));

        PartDefinition bulb5 = root.addOrReplaceChild("bulb5", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, -1.0F, -0.7006F, 0.3897F, 1.994F));

        PartDefinition appendage_spike7 = bulb5.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, -9.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 9.0F, 0.5F));

        PartDefinition bulb6 = root.addOrReplaceChild("bulb6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, -1.0F, -0.7006F, -0.3897F, -1.994F));

        PartDefinition appendage_spike6 = bulb6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -9.0F, -1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, 0.5F));

        PartDefinition bulb7 = root.addOrReplaceChild("bulb7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 1.0F, 0.7006F, 0.3897F, -1.994F));

        PartDefinition appendage_spike8 = bulb7.addOrReplaceChild("appendage_spike8", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb8 = root.addOrReplaceChild("bulb8", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 1.0F, 1.0F, 0.7006F, -0.3897F, 1.994F));

        PartDefinition appendage_spike9 = bulb8.addOrReplaceChild("appendage_spike9", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 9.0F, -0.5F));

        PartDefinition tendril1 = root.addOrReplaceChild("tendril1", CubeListBuilder.create().texOffs(18, 0).addBox(-3.5F, -13.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

        PartDefinition cube_r1 = tendril1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 0).addBox(-3.0F, -10.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition tendril2 = root.addOrReplaceChild("tendril2", CubeListBuilder.create().texOffs(22, 7).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -2.0F, -2.0F, -1.2787F, -0.7401F, -0.4194F));

        PartDefinition tendril4 = root.addOrReplaceChild("tendril4", CubeListBuilder.create().texOffs(22, 10).mirror().addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, -2.0F, -2.0F, -1.2787F, 0.7401F, 0.4194F));

        PartDefinition tendril3 = root.addOrReplaceChild("tendril3", CubeListBuilder.create(), PartPose.offsetAndRotation(2.0F, -2.0F, 2.0F, 1.2787F, -0.7401F, 0.4194F));

        PartDefinition cube_r2 = tendril3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 7).mirror().addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition tendril5 = root.addOrReplaceChild("tendril5", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.0F, -2.0F, 2.0F, 1.2787F, 0.7401F, -0.4194F));

        PartDefinition cube_r3 = tendril5.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(22, 10).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(((EntityVegRoller) entity).AS_isIdle, AnimVegRoller.VEG_ROLLER_IDLE, ageInTicks, 1f);
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