package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimTetheredSheep;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethSheep;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelTethSheep<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart leg4;
    private final ModelPart leg3;
    private final ModelPart leg2;
    private final ModelPart leg1;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike4;
    private final ModelPart appendage5;
    private final ModelPart appendage_spike5;
    private final ModelPart tentacle15;
    private final ModelPart tentacle1;
    private final ModelPart tentacle4;
    private final ModelPart tentacle2;
    private final ModelPart tentacle5;
    private final ModelPart tentacle3;
    private final ModelPart tentacle6;
    private final ModelPart tentacle7;
    private final ModelPart tentacle14;
    private final ModelPart tentacle9;
    private final ModelPart tentacle13;
    private final ModelPart tentacle10;
    private final ModelPart tentacle12;
    private final ModelPart tentacle8;
    private final ModelPart tentacle11;
    private final ModelPart appendage1;
    private final ModelPart appendage_spike7;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;

    public ModelTethSheep(ModelPart root) {
        this.root = root.getChild("root");
        this.leg4 = this.root.getChild("leg4");
        this.leg3 = this.root.getChild("leg3");
        this.leg2 = this.root.getChild("leg2");
        this.leg1 = this.root.getChild("leg1");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.appendage3 = this.head.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.appendage4 = this.head.getChild("appendage4");
        this.appendage_spike4 = this.appendage4.getChild("appendage_spike4");
        this.appendage5 = this.head.getChild("appendage5");
        this.appendage_spike5 = this.appendage5.getChild("appendage_spike5");
        this.tentacle15 = this.head.getChild("tentacle15");
        this.tentacle1 = this.body.getChild("tentacle1");
        this.tentacle4 = this.body.getChild("tentacle4");
        this.tentacle2 = this.body.getChild("tentacle2");
        this.tentacle5 = this.body.getChild("tentacle5");
        this.tentacle3 = this.body.getChild("tentacle3");
        this.tentacle6 = this.body.getChild("tentacle6");
        this.tentacle7 = this.body.getChild("tentacle7");
        this.tentacle14 = this.body.getChild("tentacle14");
        this.tentacle9 = this.body.getChild("tentacle9");
        this.tentacle13 = this.body.getChild("tentacle13");
        this.tentacle10 = this.body.getChild("tentacle10");
        this.tentacle12 = this.body.getChild("tentacle12");
        this.tentacle8 = this.body.getChild("tentacle8");
        this.tentacle11 = this.body.getChild("tentacle11");
        this.appendage1 = this.body.getChild("appendage1");
        this.appendage_spike7 = this.appendage1.getChild("appendage_spike7");
        this.appendage2 = this.body.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg4 = root.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -12.0F, -5.0F));

        PartDefinition leg3 = root.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -12.0F, -5.0F));

        PartDefinition leg2 = root.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -12.0F, 7.0F));

        PartDefinition leg1 = root.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -12.0F, 7.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -15.0F, 0.0F));

        PartDefinition rotation_r1 = body.addOrReplaceChild("rotation_r1", CubeListBuilder.create().texOffs(28, 8).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 16.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, -8.0F));

        PartDefinition appendage3 = head.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.5F, -5.0F, -0.8595F, 0.3356F, 3.1098F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage4 = head.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -1.5F, -5.0F, -1.2722F, -0.0606F, 2.3531F));

        PartDefinition appendage_spike4 = appendage4.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage5 = head.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.5F, -5.0F, -0.9548F, 0.5541F, 2.3608F));

        PartDefinition appendage_spike5 = appendage5.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition tentacle15 = head.addOrReplaceChild("tentacle15", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.3572F, -1.234F, -3.0F, -1.1345F, 1.3526F, -1.5708F));

        PartDefinition cube_r1 = tentacle15.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -2.7309F, -7.4435F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition tentacle1 = body.addOrReplaceChild("tentacle1", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.3572F, -1.234F, -3.0F, 0.0F, -1.5708F, -2.2689F));

        PartDefinition cube_r2 = tentacle1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -2.7309F, -7.4435F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition tentacle4 = body.addOrReplaceChild("tentacle4", CubeListBuilder.create(), PartPose.offsetAndRotation(3.3572F, -1.234F, -3.0F, 0.0F, 1.5708F, 2.2689F));

        PartDefinition cube_r3 = tentacle4.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(25, -7).mirror().addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.4363F, 0.0F, 0.0F));

        PartDefinition tentacle2 = body.addOrReplaceChild("tentacle2", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.3572F, -1.984F, 1.0F, 1.789F, 1.2217F, 3.1416F));

        PartDefinition cube_r4 = tentacle2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -2.501F, -7.0436F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition tentacle5 = body.addOrReplaceChild("tentacle5", CubeListBuilder.create().texOffs(25, -7).mirror().addBox(1.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.3572F, -1.234F, 0.0F, 1.789F, -1.1781F, -3.1416F));

        PartDefinition tentacle3 = body.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(25, -7).addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.3572F, 1.766F, 4.0F, 1.789F, 1.3526F, 1.5708F));

        PartDefinition tentacle6 = body.addOrReplaceChild("tentacle6", CubeListBuilder.create().texOffs(25, -7).mirror().addBox(0.0F, -2.5F, -7.0F, 0.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.3572F, 0.766F, 4.0F, -1.3526F, -1.1345F, 1.5708F));

        PartDefinition tentacle7 = body.addOrReplaceChild("tentacle7", CubeListBuilder.create().texOffs(39, -5).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.5F, -3.0F, -1.5708F, -0.4363F, 0.0F));

        PartDefinition tentacle14 = body.addOrReplaceChild("tentacle14", CubeListBuilder.create().texOffs(39, -5).mirror().addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -1.5F, -3.0F, -1.5708F, 0.4363F, 0.0F));

        PartDefinition tentacle9 = body.addOrReplaceChild("tentacle9", CubeListBuilder.create().texOffs(39, -5).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -0.5F, -8.0F, 0.7314F, 0.4165F, -0.7251F));

        PartDefinition tentacle13 = body.addOrReplaceChild("tentacle13", CubeListBuilder.create().texOffs(39, -5).mirror().addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -0.5F, -8.0F, 0.7314F, -0.4165F, 0.7251F));

        PartDefinition tentacle10 = body.addOrReplaceChild("tentacle10", CubeListBuilder.create().texOffs(49, -5).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -0.5F, -6.0F, 1.7791F, 0.6058F, -1.1326F));

        PartDefinition tentacle12 = body.addOrReplaceChild("tentacle12", CubeListBuilder.create().texOffs(49, -5).mirror().addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.0F, -0.5F, -6.0F, 1.7791F, -0.6058F, 1.1326F));

        PartDefinition tentacle8 = body.addOrReplaceChild("tentacle8", CubeListBuilder.create().texOffs(49, -5).addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -1.5F, 5.0F, -1.5708F, 0.4363F, 0.0F));

        PartDefinition tentacle11 = body.addOrReplaceChild("tentacle11", CubeListBuilder.create().texOffs(49, -5).mirror().addBox(0.0F, -1.5F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -1.5F, 5.0F, -1.8702F, -0.5071F, 0.1255F));

        PartDefinition appendage1 = body.addOrReplaceChild("appendage1", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.5F, 4.0F, 0.1569F, -0.1885F, 2.9319F));

        PartDefinition appendage_spike7 = appendage1.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage2 = body.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(52, 8).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.5F, -2.0F, 0.0475F, 0.2369F, -2.8996F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(59, 1).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimTetheredSheep.TETH_SHEEP_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityTethSheep) entity).AS_isIdle, AnimTetheredSheep.TETH_SHEEP_IDLE, ageInTicks, 1f);
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