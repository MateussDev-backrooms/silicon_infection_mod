package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimHomMitochondria;
import net.Mateuss.Chemosynthesis.client.animations.AnimHomVaucole;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomMitochondria;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomVaucole;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelHomMitochondria<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart tube;
    private final ModelPart tube2;
    private final ModelPart spewer;
    private final ModelPart tendril3;
    private final ModelPart tendril4;
    private final ModelPart bulb4;
    private final ModelPart appendage_spike5;
    private final ModelPart bulb5;
    private final ModelPart appendage_spike6;
    private final ModelPart bulb6;
    private final ModelPart appendage_spike7;
    private final ModelPart bulb1;
    private final ModelPart appendage_spike2;
    private final ModelPart bulb2;
    private final ModelPart appendage_spike3;
    private final ModelPart bulb3;
    private final ModelPart appendage_spike4;
    private final ModelPart vein;
    private final ModelPart tendril1;
    private final ModelPart vein2;
    private final ModelPart tendril2;

    public ModelHomMitochondria(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.tube = this.body.getChild("tube");
        this.tube2 = this.tube.getChild("tube2");
        this.spewer = this.tube2.getChild("spewer");
        this.tendril3 = this.tube2.getChild("tendril3");
        this.tendril4 = this.tube2.getChild("tendril4");
        this.bulb4 = this.tube.getChild("bulb4");
        this.appendage_spike5 = this.bulb4.getChild("appendage_spike5");
        this.bulb5 = this.tube.getChild("bulb5");
        this.appendage_spike6 = this.bulb5.getChild("appendage_spike6");
        this.bulb6 = this.tube.getChild("bulb6");
        this.appendage_spike7 = this.bulb6.getChild("appendage_spike7");
        this.bulb1 = this.body.getChild("bulb1");
        this.appendage_spike2 = this.bulb1.getChild("appendage_spike2");
        this.bulb2 = this.body.getChild("bulb2");
        this.appendage_spike3 = this.bulb2.getChild("appendage_spike3");
        this.bulb3 = this.body.getChild("bulb3");
        this.appendage_spike4 = this.bulb3.getChild("appendage_spike4");
        this.vein = this.root.getChild("vein");
        this.tendril1 = this.vein.getChild("tendril1");
        this.vein2 = this.root.getChild("vein2");
        this.tendril2 = this.vein2.getChild("tendril2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 24).addBox(-5.0F, -5.0F, 0.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.2426F, 11.2426F, 0.9599F, 0.0F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition tube = body.addOrReplaceChild("tube", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 2.0F));

        PartDefinition cube_r3 = tube.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(48, 0).addBox(-3.0F, -3.5F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, -10.75F, -7.25F, 3.1416F, 0.0F, 0.0F));

        PartDefinition cube_r4 = tube.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 46).addBox(-4.0F, -3.5F, -4.75F, 8.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.0F, -1.5F, 1.3963F, 0.0F, 0.0F));

        PartDefinition cube_r5 = tube.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 24).addBox(-5.0F, -6.0F, -10.0F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition tube2 = tube.addOrReplaceChild("tube2", CubeListBuilder.create(), PartPose.offset(-0.5F, -10.75F, -7.5F));

        PartDefinition cube_r6 = tube2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(74, 79).addBox(-2.0F, -2.5F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 7.75F, -5.25F, 3.098F, 0.0F, 0.0F));

        PartDefinition cube_r7 = tube2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(40, 44).addBox(-2.0F, -2.5F, -5.0F, 5.0F, 5.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.25F, -2.75F, -1.3963F, 0.0F, 0.0F));

        PartDefinition spewer = tube2.addOrReplaceChild("spewer", CubeListBuilder.create(), PartPose.offset(0.0F, 7.75F, -9.25F));

        PartDefinition cube_r8 = spewer.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(52, 71).addBox(-3.0F, -3.5F, -2.0F, 7.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.098F, 0.0F, 0.0F));

        PartDefinition tendril3 = tube2.addOrReplaceChild("tendril3", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, 0.75F, -2.5F, -3.0543F, 0.0F, 0.0F));

        PartDefinition cube_r9 = tendril3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(82, 0).addBox(-3.0F, -10.0F, -1.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, -1.5708F, 1.4399F, -1.5708F));

        PartDefinition tendril4 = tube2.addOrReplaceChild("tendril4", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.5F, 0.75F, -2.5F, -3.0543F, 0.0F, 0.0F));

        PartDefinition cube_r10 = tendril4.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(82, 0).addBox(-3.0F, -10.0F, -1.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, -1.5708F, 1.4399F, -1.5708F));

        PartDefinition bulb4 = tube.addOrReplaceChild("bulb4", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -13.5F, -7.25F, 0.2125F, 0.4774F, 3.0087F));

        PartDefinition appendage_spike5 = bulb4.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb5 = tube.addOrReplaceChild("bulb5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -13.5F, -7.25F, 0.2356F, -0.1472F, -2.9353F));

        PartDefinition appendage_spike6 = bulb5.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb6 = tube.addOrReplaceChild("bulb6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -13.5F, -9.25F, -0.4009F, 0.0854F, 3.1346F));

        PartDefinition appendage_spike7 = bulb6.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb1 = body.addOrReplaceChild("bulb1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -17.0F, 16.0F, -0.0873F, 0.0F, -2.9671F));

        PartDefinition appendage_spike2 = bulb1.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb2 = body.addOrReplaceChild("bulb2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -16.5F, 17.75F, 0.5633F, -0.0702F, -2.8565F));

        PartDefinition appendage_spike3 = bulb2.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition bulb3 = body.addOrReplaceChild("bulb3", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -16.5F, 16.75F, 0.9063F, 0.4334F, -3.0864F));

        PartDefinition appendage_spike4 = bulb3.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 24).addBox(0.0F, -9.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 9.0F, -0.5F));

        PartDefinition vein = root.addOrReplaceChild("vein", CubeListBuilder.create().texOffs(0, 80).addBox(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r11 = vein.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(74, 71).addBox(-6.0F, -2.25F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.348F, -19.6051F, 0.0F, 0.0F, 0.0F, -2.8798F));

        PartDefinition cube_r12 = vein.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 63).addBox(-11.0F, -1.5F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8714F, -7.6166F, 0.0F, 0.0F, 0.0F, 1.6144F));

        PartDefinition cube_r13 = vein.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(74, 71).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.8714F, -7.6166F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition cube_r14 = vein.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 63).addBox(-6.0F, -5.0F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3927F));

        PartDefinition tendril1 = vein.addOrReplaceChild("tendril1", CubeListBuilder.create().texOffs(82, 0).addBox(-3.5F, -13.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -20.0F, 0.0F));

        PartDefinition cube_r15 = tendril1.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(82, 0).addBox(-3.0F, -10.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition vein2 = root.addOrReplaceChild("vein2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r16 = vein2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(74, 71).addBox(-2.0F, -2.25F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.348F, -19.6051F, 0.0F, 0.0F, 0.0F, 2.8798F));

        PartDefinition cube_r17 = vein2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 63).addBox(3.0F, -1.5F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8714F, -7.6166F, 0.0F, 0.0F, 0.0F, -1.6144F));

        PartDefinition cube_r18 = vein2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(74, 71).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.8714F, -7.6166F, 0.0F, 0.0F, 0.0F, -1.3963F));

        PartDefinition cube_r19 = vein2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 63).addBox(-2.0F, -5.0F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition tendril2 = vein2.addOrReplaceChild("tendril2", CubeListBuilder.create().texOffs(82, 0).addBox(-3.5F, -13.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -19.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition cube_r20 = tendril2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(82, 0).addBox(-3.0F, -10.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(((EntityHomMitochondria) entity).AS_isIdle, AnimHomMitochondria.MITOCHONDRIA_IDLE, ageInTicks, 1f);
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