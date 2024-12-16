package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimBrachaticStage;
import net.Mateuss.Chemosynthesis.entity.living_entities.conjugonal.EntityBrachaticStage;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelBrachaticStage<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart legFR1;
    private final ModelPart legFR2;
    private final ModelPart legFR3;
    private final ModelPart legFL1;
    private final ModelPart legFL2;
    private final ModelPart legFL3;
    private final ModelPart legBL1;
    private final ModelPart legBL2;
    private final ModelPart legBL3;
    private final ModelPart legBR1;
    private final ModelPart legBR2;
    private final ModelPart legBR3;
    private final ModelPart body;
    private final ModelPart blob1;
    private final ModelPart blob2;
    private final ModelPart blob4;
    private final ModelPart blob10;
    private final ModelPart blob11;
    private final ModelPart head2;
    private final ModelPart blob12;
    private final ModelPart blob7;
    private final ModelPart blob3;
    private final ModelPart blob8;
    private final ModelPart blob5;
    private final ModelPart blob9;
    private final ModelPart blob6;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart appendage32;
    private final ModelPart appendage_spike33;
    private final ModelPart appendage33;
    private final ModelPart appendage_spike34;
    private final ModelPart appendage34;
    private final ModelPart appendage_spike35;
    private final ModelPart appendage35;
    private final ModelPart appendage_spike36;
    private final ModelPart appendage14;
    private final ModelPart appendage_spike15;
    private final ModelPart appendage28;
    private final ModelPart appendage_spike29;
    private final ModelPart appendage15;
    private final ModelPart appendage_spike16;
    private final ModelPart appendage29;
    private final ModelPart appendage_spike30;
    private final ModelPart appendage16;
    private final ModelPart appendage_spike17;
    private final ModelPart appendage30;
    private final ModelPart appendage_spike31;
    private final ModelPart appendage17;
    private final ModelPart appendage_spike18;
    private final ModelPart appendage31;
    private final ModelPart appendage_spike32;
    private final ModelPart appendage12;
    private final ModelPart appendage_spike13;
    private final ModelPart appendage26;
    private final ModelPart appendage_spike27;
    private final ModelPart appendage13;
    private final ModelPart appendage_spike14;
    private final ModelPart appendage27;
    private final ModelPart appendage_spike28;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage5;
    private final ModelPart appendage_spike5;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage6;
    private final ModelPart appendage_spike6;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike4;
    private final ModelPart appendage7;
    private final ModelPart appendage_spike7;

    public ModelBrachaticStage(ModelPart root) {
        this.root = root.getChild("root");
        this.legFR1 = this.root.getChild("legFR1");
        this.legFR2 = this.legFR1.getChild("legFR2");
        this.legFR3 = this.legFR2.getChild("legFR3");
        this.legFL1 = this.root.getChild("legFL1");
        this.legFL2 = this.legFL1.getChild("legFL2");
        this.legFL3 = this.legFL2.getChild("legFL3");
        this.legBL1 = this.root.getChild("legBL1");
        this.legBL2 = this.legBL1.getChild("legBL2");
        this.legBL3 = this.legBL2.getChild("legBL3");
        this.legBR1 = this.root.getChild("legBR1");
        this.legBR2 = this.legBR1.getChild("legBR2");
        this.legBR3 = this.legBR2.getChild("legBR3");
        this.body = this.root.getChild("body");
        this.blob1 = this.body.getChild("blob1");
        this.blob2 = this.body.getChild("blob2");
        this.blob4 = this.body.getChild("blob4");
        this.blob10 = this.body.getChild("blob10");
        this.blob11 = this.body.getChild("blob11");
        this.head2 = this.blob11.getChild("head2");
        this.blob12 = this.body.getChild("blob12");
        this.blob7 = this.body.getChild("blob7");
        this.blob3 = this.body.getChild("blob3");
        this.blob8 = this.body.getChild("blob8");
        this.blob5 = this.body.getChild("blob5");
        this.blob9 = this.body.getChild("blob9");
        this.blob6 = this.body.getChild("blob6");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.appendage32 = this.head.getChild("appendage32");
        this.appendage_spike33 = this.appendage32.getChild("appendage_spike33");
        this.appendage33 = this.head.getChild("appendage33");
        this.appendage_spike34 = this.appendage33.getChild("appendage_spike34");
        this.appendage34 = this.head.getChild("appendage34");
        this.appendage_spike35 = this.appendage34.getChild("appendage_spike35");
        this.appendage35 = this.head.getChild("appendage35");
        this.appendage_spike36 = this.appendage35.getChild("appendage_spike36");
        this.appendage14 = this.head.getChild("appendage14");
        this.appendage_spike15 = this.appendage14.getChild("appendage_spike15");
        this.appendage28 = this.head.getChild("appendage28");
        this.appendage_spike29 = this.appendage28.getChild("appendage_spike29");
        this.appendage15 = this.head.getChild("appendage15");
        this.appendage_spike16 = this.appendage15.getChild("appendage_spike16");
        this.appendage29 = this.head.getChild("appendage29");
        this.appendage_spike30 = this.appendage29.getChild("appendage_spike30");
        this.appendage16 = this.head.getChild("appendage16");
        this.appendage_spike17 = this.appendage16.getChild("appendage_spike17");
        this.appendage30 = this.head.getChild("appendage30");
        this.appendage_spike31 = this.appendage30.getChild("appendage_spike31");
        this.appendage17 = this.head.getChild("appendage17");
        this.appendage_spike18 = this.appendage17.getChild("appendage_spike18");
        this.appendage31 = this.head.getChild("appendage31");
        this.appendage_spike32 = this.appendage31.getChild("appendage_spike32");
        this.appendage12 = this.neck.getChild("appendage12");
        this.appendage_spike13 = this.appendage12.getChild("appendage_spike13");
        this.appendage26 = this.neck.getChild("appendage26");
        this.appendage_spike27 = this.appendage26.getChild("appendage_spike27");
        this.appendage13 = this.neck.getChild("appendage13");
        this.appendage_spike14 = this.appendage13.getChild("appendage_spike14");
        this.appendage27 = this.neck.getChild("appendage27");
        this.appendage_spike28 = this.appendage27.getChild("appendage_spike28");
        this.appendage2 = this.body.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.appendage5 = this.body.getChild("appendage5");
        this.appendage_spike5 = this.appendage5.getChild("appendage_spike5");
        this.appendage3 = this.body.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.appendage6 = this.body.getChild("appendage6");
        this.appendage_spike6 = this.appendage6.getChild("appendage_spike6");
        this.appendage4 = this.body.getChild("appendage4");
        this.appendage_spike4 = this.appendage4.getChild("appendage_spike4");
        this.appendage7 = this.body.getChild("appendage7");
        this.appendage_spike7 = this.appendage7.getChild("appendage_spike7");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition legFR1 = root.addOrReplaceChild("legFR1", CubeListBuilder.create().texOffs(76, 0).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -11.0F, -4.0F, -0.3008F, -0.4353F, -2.5074F));

        PartDefinition legFR2 = legFR1.addOrReplaceChild("legFR2", CubeListBuilder.create().texOffs(40, 22).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.766F, 9.6428F, 0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition legFR3 = legFR2.addOrReplaceChild("legFR3", CubeListBuilder.create().texOffs(40, 26).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1145F, 0.8718F, 0.5F, 0.0F, 0.0F, 0.6109F));

        PartDefinition legFL1 = root.addOrReplaceChild("legFL1", CubeListBuilder.create().texOffs(0, 78).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -15.0F, -6.0F, -1.9607F, -0.2697F, -1.1549F));

        PartDefinition legFL2 = legFL1.addOrReplaceChild("legFL2", CubeListBuilder.create().texOffs(40, 22).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6094F, 9.0686F, 0.139F, -0.2485F, 0.229F, -0.1694F));

        PartDefinition legFL3 = legFL2.addOrReplaceChild("legFL3", CubeListBuilder.create().texOffs(40, 26).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1145F, 0.8718F, 0.5F, 0.0044F, 0.0602F, 0.4949F));

        PartDefinition legBL1 = root.addOrReplaceChild("legBL1", CubeListBuilder.create().texOffs(12, 78).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -15.0F, 6.0F, 2.2254F, 0.5424F, -1.566F));

        PartDefinition legBL2 = legBL1.addOrReplaceChild("legBL2", CubeListBuilder.create().texOffs(40, 22).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6637F, 9.551F, 0.0193F, -0.1932F, 0.277F, 0.0504F));

        PartDefinition legBL3 = legBL2.addOrReplaceChild("legBL3", CubeListBuilder.create().texOffs(40, 26).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1145F, 0.8718F, 0.5F, -0.0061F, 0.0601F, 0.32F));

        PartDefinition legBR1 = root.addOrReplaceChild("legBR1", CubeListBuilder.create().texOffs(24, 78).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -15.0F, 6.0F, 0.7269F, 0.0491F, -2.1112F));

        PartDefinition legBR2 = legBR1.addOrReplaceChild("legBR2", CubeListBuilder.create().texOffs(40, 22).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.6637F, 9.551F, 0.0193F, -0.1932F, 0.277F, 0.0504F));

        PartDefinition legBR3 = legBR2.addOrReplaceChild("legBR3", CubeListBuilder.create().texOffs(40, 26).addBox(-10.9549F, -0.8917F, -1.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.1145F, 0.8718F, 0.5F, -0.0061F, 0.0601F, 0.32F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 78).addBox(-1.5F, -5.5F, -1.5F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.5F, -19.5F, 1.5F, 0.0F, 0.0F, -0.4363F));

        PartDefinition blob1 = body.addOrReplaceChild("blob1", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -18.0F, -5.0F, 10.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition blob2 = body.addOrReplaceChild("blob2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 0.0F));

        PartDefinition cube_r2 = blob2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(72, 18).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -2.5F, -4.5F, 0.0538F, 0.4295F, 0.2359F));

        PartDefinition blob4 = body.addOrReplaceChild("blob4", CubeListBuilder.create(), PartPose.offset(-1.0F, -4.0F, 2.0F));

        PartDefinition cube_r3 = blob4.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(72, 18).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -3.5F, -2.5F, 0.3343F, 0.0905F, 0.2631F));

        PartDefinition blob10 = body.addOrReplaceChild("blob10", CubeListBuilder.create(), PartPose.offset(4.875F, -16.0F, -6.125F));

        PartDefinition cube_r4 = blob10.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, -5).addBox(0.0725F, -1.444F, -4.7423F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.625F, 0.25F, 0.375F, 0.2657F, -0.3476F, -0.012F));

        PartDefinition cube_r5 = blob10.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(30, -5).addBox(0.0F, -1.75F, -5.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.375F, 0.5F, -0.625F, 0.2496F, 0.0325F, 0.0887F));

        PartDefinition cube_r6 = blob10.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(72, 32).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.375F, -0.5F, 3.125F, 0.6984F, 1.1781F, 0.7742F));

        PartDefinition blob11 = body.addOrReplaceChild("blob11", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.5F, -16.5F, 4.5F, -0.7418F, 0.0F, -0.5672F));

        PartDefinition cube_r7 = blob11.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(56, 64).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3343F, 0.0905F, 0.2631F));

        PartDefinition head2 = blob11.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(-5.5215F, 0.3228F, 0.8816F, 1.7828F, 1.1546F, 2.1214F));

        PartDefinition head_r1 = head2.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(30, 4).addBox(-11.0F, -6.5F, 13.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0382F, 5.2364F, -11.7671F, 0.087F, 0.0068F, -0.0782F));

        PartDefinition cube_r8 = head2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(40, -5).addBox(-9.0F, -7.5F, 8.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5382F, 5.4864F, -12.2671F, -0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r9 = head2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(30, -5).addBox(-10.0F, -6.0F, 9.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.7882F, 5.2364F, -12.2671F, 0.0873F, 0.0F, 0.0F));

        PartDefinition blob12 = body.addOrReplaceChild("blob12", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.5F, -0.5F, 2.5F, -0.7298F, -0.1468F, -0.7292F));

        PartDefinition cube_r10 = blob12.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(72, 18).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3343F, 0.0905F, 0.2631F));

        PartDefinition blob7 = body.addOrReplaceChild("blob7", CubeListBuilder.create(), PartPose.offsetAndRotation(4.5F, -2.5F, 4.5F, -0.176F, -0.1289F, 0.2847F));

        PartDefinition cube_r11 = blob7.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(72, 18).addBox(-3.5F, -3.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0077F, 0.1744F, 0.2175F));

        PartDefinition blob3 = body.addOrReplaceChild("blob3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.8493F, -5.599F, -4.0978F, 0.4758F, 0.2457F, -0.3604F));

        PartDefinition cube_r12 = blob3.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 28).addBox(-7.4091F, -5.9207F, -3.9224F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0444F, 0.3207F, 0.1202F));

        PartDefinition blob8 = body.addOrReplaceChild("blob8", CubeListBuilder.create(), PartPose.offsetAndRotation(1.1507F, 0.401F, -1.0978F, -0.2223F, 0.2457F, -0.3604F));

        PartDefinition cube_r13 = blob8.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 28).addBox(-4.4609F, -5.5644F, -5.4789F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3131F, 0.1744F, 0.2175F));

        PartDefinition blob5 = body.addOrReplaceChild("blob5", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.8493F, -5.599F, 1.9022F, 0.1602F, -0.1445F, -0.5573F));

        PartDefinition cube_r14 = blob5.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 28).addBox(-5.5F, -5.5F, -3.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0077F, 0.1744F, 0.2175F));

        PartDefinition blob9 = body.addOrReplaceChild("blob9", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.8493F, -14.599F, -2.0978F, 0.8721F, -0.3242F, -0.2629F));

        PartDefinition cube_r15 = blob9.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 28).addBox(-5.5F, -5.5F, -3.5F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0077F, 0.1744F, 0.2175F));

        PartDefinition blob6 = body.addOrReplaceChild("blob6", CubeListBuilder.create(), PartPose.offsetAndRotation(3.1507F, -11.599F, 1.9022F, 0.0406F, -0.3863F, 0.0976F));

        PartDefinition cube_r16 = blob6.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 28).addBox(-4.5731F, -5.8072F, -3.7155F, 9.0F, 9.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0077F, 0.1744F, 0.2175F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -17.0072F, 5.1186F, -0.0873F, 0.0F, 0.4363F));

        PartDefinition cube_r17 = neck.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(84, 63).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, -2.0071F, 0.0F, 0.0F));

        PartDefinition cube_r18 = neck.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(70, 78).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -0.3301F, -2.618F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0894F, 5.3857F, 0.5236F, 0.0F, 0.0F));

        PartDefinition cube_r19 = head.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(72, 54).addBox(-0.5F, 0.0F, -8.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -7.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition cube_r20 = head.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(84, 70).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2059F, -2.1704F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r21 = head.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(48, 80).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0111F, 0.5161F, -1.0036F, 0.0F, 0.0F));

        PartDefinition appendage32 = head.addOrReplaceChild("appendage32", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.4034F, -8.5043F, -1.309F, 0.0F, 3.1416F));

        PartDefinition appendage_spike33 = appendage32.addOrReplaceChild("appendage_spike33", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage33 = head.addOrReplaceChild("appendage33", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.5588F, -5.0889F, -0.9163F, 0.0F, 3.1416F));

        PartDefinition appendage_spike34 = appendage33.addOrReplaceChild("appendage_spike34", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage34 = head.addOrReplaceChild("appendage34", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5741F, -2.0904F, -0.2618F, 0.0F, 3.1416F));

        PartDefinition appendage_spike35 = appendage34.addOrReplaceChild("appendage_spike35", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage35 = head.addOrReplaceChild("appendage35", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1664F, -0.5362F, 0.6109F, 0.0F, 3.1416F));

        PartDefinition appendage_spike36 = appendage35.addOrReplaceChild("appendage_spike36", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage14 = head.addOrReplaceChild("appendage14", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3677F, -1.6526F, -0.3055F, 0.2056F, -0.0325F, -1.5316F));

        PartDefinition appendage_spike15 = appendage14.addOrReplaceChild("appendage_spike15", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage28 = head.addOrReplaceChild("appendage28", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3677F, -1.6526F, -0.3055F, 0.2056F, 0.0325F, 1.5316F));

        PartDefinition appendage_spike29 = appendage28.addOrReplaceChild("appendage_spike29", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage15 = head.addOrReplaceChild("appendage15", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1419F, -3.8001F, -2.0017F, -0.1247F, -0.251F, -1.5445F));

        PartDefinition appendage_spike16 = appendage15.addOrReplaceChild("appendage_spike16", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage29 = head.addOrReplaceChild("appendage29", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1419F, -3.8001F, -2.0017F, -0.1247F, 0.251F, 1.5445F));

        PartDefinition appendage_spike30 = appendage29.addOrReplaceChild("appendage_spike30", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage16 = head.addOrReplaceChild("appendage16", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5005F, -5.2486F, -5.8553F, -0.4002F, 0.1905F, -1.5438F));

        PartDefinition appendage_spike17 = appendage16.addOrReplaceChild("appendage_spike17", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage30 = head.addOrReplaceChild("appendage30", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5005F, -5.2486F, -5.8553F, -0.4002F, -0.1905F, 1.5438F));

        PartDefinition appendage_spike31 = appendage30.addOrReplaceChild("appendage_spike31", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage17 = head.addOrReplaceChild("appendage17", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3809F, -4.1618F, -9.122F, -0.5551F, -0.0481F, -1.9129F));

        PartDefinition appendage_spike18 = appendage17.addOrReplaceChild("appendage_spike18", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage31 = head.addOrReplaceChild("appendage31", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3809F, -4.1618F, -9.122F, -0.6716F, -0.0533F, 1.6645F));

        PartDefinition appendage_spike32 = appendage31.addOrReplaceChild("appendage_spike32", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage12 = neck.addOrReplaceChild("appendage12", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2683F, -1.6404F, 1.5568F, 0.3177F, -1.0864F, -1.6952F));

        PartDefinition appendage_spike13 = appendage12.addOrReplaceChild("appendage_spike13", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage26 = neck.addOrReplaceChild("appendage26", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2683F, -1.6404F, 1.5568F, 0.3177F, 1.0864F, 1.6952F));

        PartDefinition appendage_spike27 = appendage26.addOrReplaceChild("appendage_spike27", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage13 = neck.addOrReplaceChild("appendage13", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5349F, -5.5567F, 4.8244F, 0.2148F, -0.4618F, -1.4107F));

        PartDefinition appendage_spike14 = appendage13.addOrReplaceChild("appendage_spike14", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage27 = neck.addOrReplaceChild("appendage27", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5349F, -5.5567F, 4.8244F, 0.2148F, 0.4618F, 1.4107F));

        PartDefinition appendage_spike28 = appendage27.addOrReplaceChild("appendage_spike28", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage2 = body.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2683F, -8.6476F, -3.3246F, -1.0925F, 0.0839F, -0.4776F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage5 = body.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2683F, -8.6476F, -3.3246F, -1.0925F, -0.0839F, 0.4776F));

        PartDefinition appendage_spike5 = appendage5.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage3 = body.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2683F, -11.6476F, -3.3246F, -1.5236F, -0.4828F, -0.503F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = body.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2683F, -12.6476F, -3.3246F, -1.6075F, 0.2648F, 0.512F));

        PartDefinition appendage_spike6 = appendage6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage4 = body.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.2683F, -9.6476F, -3.3246F, -1.4939F, -0.4267F, -0.023F));

        PartDefinition appendage_spike4 = appendage4.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 98).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage7 = body.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(0, 92).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2683F, -9.6476F, -3.3246F, -1.5812F, 0.4267F, 0.023F));

        PartDefinition appendage_spike7 = appendage7.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 98).addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimBrachaticStage.BRACHATIC_SPRINT, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityBrachaticStage) entity).AS_isIdle, AnimBrachaticStage.BRACHATIC_IDLE, ageInTicks, 1f);
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