package net.mateuss.chemosynthesis.entity.client;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mateuss.chemosynthesis.entity.animations.AnimTetheredCow;
import net.mateuss.chemosynthesis.entity.animations.ModAnimDefinitions;
import net.mateuss.chemosynthesis.entity.custom.EntityTethCow;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelTethCow<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart body;
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
    private final ModelPart appendage12;
    private final ModelPart appendage_spike12;
    private final ModelPart appendage10;
    private final ModelPart appendage_spike10;
    private final ModelPart appendage11;
    private final ModelPart appendage_spike11;
    private final ModelPart appendage13;
    private final ModelPart appendage_spike13;
    private final ModelPart head;
    private final ModelPart tentacle1;
    private final ModelPart tentacle2;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike4;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;

    public ModelTethCow(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.appendage5 = this.body.getChild("appendage5");
        this.appendage_spike5 = this.appendage5.getChild("appendage_spike5");
        this.appendage6 = this.body.getChild("appendage6");
        this.appendage_spike6 = this.appendage6.getChild("appendage_spike6");
        this.appendage7 = this.body.getChild("appendage7");
        this.appendage_spike7 = this.appendage7.getChild("appendage_spike7");
        this.appendage8 = this.body.getChild("appendage8");
        this.appendage_spike8 = this.appendage8.getChild("appendage_spike8");
        this.appendage9 = this.body.getChild("appendage9");
        this.appendage_spike9 = this.appendage9.getChild("appendage_spike9");
        this.appendage12 = this.body.getChild("appendage12");
        this.appendage_spike12 = this.appendage12.getChild("appendage_spike12");
        this.appendage10 = this.body.getChild("appendage10");
        this.appendage_spike10 = this.appendage10.getChild("appendage_spike10");
        this.appendage11 = this.body.getChild("appendage11");
        this.appendage_spike11 = this.appendage11.getChild("appendage_spike11");
        this.appendage13 = this.body.getChild("appendage13");
        this.appendage_spike13 = this.appendage13.getChild("appendage_spike13");
        this.head = this.body.getChild("head");
        this.tentacle1 = this.head.getChild("tentacle1");
        this.tentacle2 = this.head.getChild("tentacle2");
        this.appendage4 = this.head.getChild("appendage4");
        this.appendage_spike4 = this.appendage4.getChild("appendage_spike4");
        this.appendage3 = this.head.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.appendage2 = this.head.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.leg1 = this.root.getChild("leg1");
        this.leg2 = this.root.getChild("leg2");
        this.leg3 = this.root.getChild("leg3");
        this.leg4 = this.root.getChild("leg4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -8.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(18, 4).addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(52, 0).addBox(-2.0F, 2.0F, -8.0F, 4.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 10.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition appendage5 = body.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 0.5F, 1.0F, 2.1783F, -0.5355F, 3.0623F));

        PartDefinition appendage_spike5 = appendage5.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = body.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 0.5F, 1.0F, 2.1783F, 0.5355F, -3.0623F));

        PartDefinition appendage_spike6 = appendage6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage7 = body.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.5F, 2.0F, 1.6442F, 0.1974F, 2.9402F));

        PartDefinition appendage_spike7 = appendage7.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage8 = body.addOrReplaceChild("appendage8", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.5F, 2.0F, 1.6442F, -0.1974F, -2.9402F));

        PartDefinition appendage_spike8 = appendage8.addOrReplaceChild("appendage_spike8", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage9 = body.addOrReplaceChild("appendage9", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -7.5F, -3.0F, 1.0832F, -1.2308F, -2.2946F));

        PartDefinition appendage_spike9 = appendage9.addOrReplaceChild("appendage_spike9", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage12 = body.addOrReplaceChild("appendage12", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -6.5F, 0.0F, 2.7798F, 1.1499F, -2.234F));

        PartDefinition appendage_spike12 = appendage12.addOrReplaceChild("appendage_spike12", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage10 = body.addOrReplaceChild("appendage10", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.5F, -3.0F, 0.4276F, -1.3734F, -1.9213F));

        PartDefinition appendage_spike10 = appendage10.addOrReplaceChild("appendage_spike10", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage11 = body.addOrReplaceChild("appendage11", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 0.5F, -4.0F, 0.4276F, 1.3734F, 1.9213F));

        PartDefinition appendage_spike11 = appendage11.addOrReplaceChild("appendage_spike11", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage13 = body.addOrReplaceChild("appendage13", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, 4.5F, 1.0F, 0.7769F, 1.3293F, 2.2625F));

        PartDefinition appendage_spike13 = appendage13.addOrReplaceChild("appendage_spike13", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(22, 0).addBox(-5.0F, -5.0F, -4.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, 1.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition tentacle1 = head.addOrReplaceChild("tentacle1", CubeListBuilder.create().texOffs(27, -5).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -0.5F, -6.0F));

        PartDefinition tentacle2 = head.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(37, -5).addBox(1.0F, -2.0F, -4.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 1.0F, -7.0F));

        PartDefinition appendage4 = head.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.5F, -4.5F, 0.1687F, 0.045F, 3.0145F));

        PartDefinition appendage_spike4 = appendage4.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage3 = head.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -2.5F, -4.0F, 0.1687F, 0.045F, -2.7451F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage2 = head.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(52, 8).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -1.5F, -5.0F, -0.7345F, -0.0175F, 3.0547F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(59, 1).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 8.0F, 15.0F));

        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 8.0F, 15.0F));

        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 8.0F, 2.0F));

        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -1.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 8.0F, 2.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimTetheredCow.TETH_COW_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityTethCow) entity).AS_isIdle, AnimTetheredCow.TETH_COW_IDLE, ageInTicks, 1f);
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