package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimSiliconRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconRoller;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelSiliconRoller<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart appendage1;
    private final ModelPart appendage_spike;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike4;
    private final ModelPart appendage5;
    private final ModelPart appendage_spike5;
    private final ModelPart appendage6;
    private final ModelPart appendage_spike6;
    private final ModelPart appendage7;
    private final ModelPart appendage_spike7;
    private final ModelPart appendage8;
    private final ModelPart appendage_spike8;
    private final ModelPart appendage9;
    private final ModelPart appendage_spike9;
    private final ModelPart appendage10;
    private final ModelPart appendage_spike10;
    private final ModelPart appendage11;
    private final ModelPart appendage_spike11;
    private final ModelPart appendage12;
    private final ModelPart appendage_spike12;
    private final ModelPart appendage13;
    private final ModelPart appendage_spike13;
    private final ModelPart appendage14;
    private final ModelPart appendage_spike14;
    private final ModelPart appendage15;
    private final ModelPart appendage_spike15;
    private final ModelPart appendage16;
    private final ModelPart appendage_spike16;
    private final ModelPart appendage17;
    private final ModelPart appendage_spike17;
    private final ModelPart appendage18;
    private final ModelPart appendage_spike18;

    public ModelSiliconRoller(ModelPart root) {
        this.root = root.getChild("root");
        this.appendage1 = this.root.getChild("appendage1");
        this.appendage_spike = this.appendage1.getChild("appendage_spike");
        this.appendage2 = this.root.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.appendage3 = this.root.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.appendage4 = this.root.getChild("appendage4");
        this.appendage_spike4 = this.appendage4.getChild("appendage_spike4");
        this.appendage5 = this.root.getChild("appendage5");
        this.appendage_spike5 = this.appendage5.getChild("appendage_spike5");
        this.appendage6 = this.root.getChild("appendage6");
        this.appendage_spike6 = this.appendage6.getChild("appendage_spike6");
        this.appendage7 = this.root.getChild("appendage7");
        this.appendage_spike7 = this.appendage7.getChild("appendage_spike7");
        this.appendage8 = this.root.getChild("appendage8");
        this.appendage_spike8 = this.appendage8.getChild("appendage_spike8");
        this.appendage9 = this.root.getChild("appendage9");
        this.appendage_spike9 = this.appendage9.getChild("appendage_spike9");
        this.appendage10 = this.root.getChild("appendage10");
        this.appendage_spike10 = this.appendage10.getChild("appendage_spike10");
        this.appendage11 = this.root.getChild("appendage11");
        this.appendage_spike11 = this.appendage11.getChild("appendage_spike11");
        this.appendage12 = this.root.getChild("appendage12");
        this.appendage_spike12 = this.appendage12.getChild("appendage_spike12");
        this.appendage13 = this.root.getChild("appendage13");
        this.appendage_spike13 = this.appendage13.getChild("appendage_spike13");
        this.appendage14 = this.root.getChild("appendage14");
        this.appendage_spike14 = this.appendage14.getChild("appendage_spike14");
        this.appendage15 = this.root.getChild("appendage15");
        this.appendage_spike15 = this.appendage15.getChild("appendage_spike15");
        this.appendage16 = this.root.getChild("appendage16");
        this.appendage_spike16 = this.appendage16.getChild("appendage_spike16");
        this.appendage17 = this.root.getChild("appendage17");
        this.appendage_spike17 = this.appendage17.getChild("appendage_spike17");
        this.appendage18 = this.root.getChild("appendage18");
        this.appendage_spike18 = this.appendage18.getChild("appendage_spike18");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.5F, 0.0F));

        PartDefinition appendage1 = root.addOrReplaceChild("appendage1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition appendage_spike = appendage1.addOrReplaceChild("appendage_spike", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage2 = root.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage3 = root.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage4 = root.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition appendage_spike4 = appendage4.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage5 = root.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition appendage_spike5 = appendage5.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = root.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition appendage_spike6 = appendage6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage7 = root.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

        PartDefinition appendage_spike7 = appendage7.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage8 = root.addOrReplaceChild("appendage8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

        PartDefinition appendage_spike8 = appendage8.addOrReplaceChild("appendage_spike8", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage9 = root.addOrReplaceChild("appendage9", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -3.1416F));

        PartDefinition appendage_spike9 = appendage9.addOrReplaceChild("appendage_spike9", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage10 = root.addOrReplaceChild("appendage10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, -3.1416F));

        PartDefinition appendage_spike10 = appendage10.addOrReplaceChild("appendage_spike10", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage11 = root.addOrReplaceChild("appendage11", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, -3.1416F));

        PartDefinition appendage_spike11 = appendage11.addOrReplaceChild("appendage_spike11", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage12 = root.addOrReplaceChild("appendage12", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -3.1416F));

        PartDefinition appendage_spike12 = appendage12.addOrReplaceChild("appendage_spike12", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage13 = root.addOrReplaceChild("appendage13", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, -3.1416F));

        PartDefinition appendage_spike13 = appendage13.addOrReplaceChild("appendage_spike13", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage14 = root.addOrReplaceChild("appendage14", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, -3.1416F));

        PartDefinition appendage_spike14 = appendage14.addOrReplaceChild("appendage_spike14", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage15 = root.addOrReplaceChild("appendage15", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F, 3.1416F));

        PartDefinition appendage_spike15 = appendage15.addOrReplaceChild("appendage_spike15", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage16 = root.addOrReplaceChild("appendage16", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.7854F, 0.0F));

        PartDefinition appendage_spike16 = appendage16.addOrReplaceChild("appendage_spike16", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage17 = root.addOrReplaceChild("appendage17", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, -0.7854F, 0.0F));

        PartDefinition appendage_spike17 = appendage17.addOrReplaceChild("appendage_spike17", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage18 = root.addOrReplaceChild("appendage18", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, -0.7854F, 3.1416F));

        PartDefinition appendage_spike18 = appendage18.addOrReplaceChild("appendage_spike18", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimSiliconRoller.SILICON_ROLLER_ROLL, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntitySiliconRoller) entity).AS_isIdle, AnimSiliconRoller.SILICON_ROLLER_IDLE, ageInTicks, 1f);
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