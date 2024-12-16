package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimSilipede;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySilipede;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelSilipede<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart tail;
    private final ModelPart appendage1;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage36;
    private final ModelPart appendage_spike37;
    private final ModelPart appendage37;
    private final ModelPart appendage_spike38;
    private final ModelPart appendage18;
    private final ModelPart appendage_spike19;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage19;
    private final ModelPart appendage_spike20;
    private final ModelPart appendage38;
    private final ModelPart appendage_spike39;
    private final ModelPart appendage39;
    private final ModelPart appendage_spike40;
    private final ModelPart appendage40;
    private final ModelPart appendage_spike41;
    private final ModelPart appendage41;
    private final ModelPart appendage_spike42;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike4;
    private final ModelPart appendage20;
    private final ModelPart appendage_spike21;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike5;
    private final ModelPart appendage21;
    private final ModelPart appendage_spike22;
    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart appendage5;
    private final ModelPart appendage_spike6;
    private final ModelPart appendage6;
    private final ModelPart appendage_spike7;
    private final ModelPart appendage7;
    private final ModelPart appendage_spike8;
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
    private final ModelPart appendage8;
    private final ModelPart appendage_spike9;
    private final ModelPart appendage22;
    private final ModelPart appendage_spike23;
    private final ModelPart appendage9;
    private final ModelPart appendage_spike10;
    private final ModelPart appendage23;
    private final ModelPart appendage_spike24;
    private final ModelPart appendage10;
    private final ModelPart appendage_spike11;
    private final ModelPart appendage24;
    private final ModelPart appendage_spike25;
    private final ModelPart appendage11;
    private final ModelPart appendage_spike12;
    private final ModelPart appendage25;
    private final ModelPart appendage_spike26;

    public ModelSilipede(ModelPart root) {
        this.root = root.getChild("root");
        this.tail = this.root.getChild("tail");
        this.appendage1 = this.tail.getChild("appendage1");
        this.appendage_spike2 = this.appendage1.getChild("appendage_spike2");
        this.appendage36 = this.tail.getChild("appendage36");
        this.appendage_spike37 = this.appendage36.getChild("appendage_spike37");
        this.appendage37 = this.tail.getChild("appendage37");
        this.appendage_spike38 = this.appendage37.getChild("appendage_spike38");
        this.appendage18 = this.tail.getChild("appendage18");
        this.appendage_spike19 = this.appendage18.getChild("appendage_spike19");
        this.appendage2 = this.tail.getChild("appendage2");
        this.appendage_spike3 = this.appendage2.getChild("appendage_spike3");
        this.appendage19 = this.tail.getChild("appendage19");
        this.appendage_spike20 = this.appendage19.getChild("appendage_spike20");
        this.appendage38 = this.tail.getChild("appendage38");
        this.appendage_spike39 = this.appendage38.getChild("appendage_spike39");
        this.appendage39 = this.tail.getChild("appendage39");
        this.appendage_spike40 = this.appendage39.getChild("appendage_spike40");
        this.appendage40 = this.tail.getChild("appendage40");
        this.appendage_spike41 = this.appendage40.getChild("appendage_spike41");
        this.appendage41 = this.tail.getChild("appendage41");
        this.appendage_spike42 = this.appendage41.getChild("appendage_spike42");
        this.appendage3 = this.tail.getChild("appendage3");
        this.appendage_spike4 = this.appendage3.getChild("appendage_spike4");
        this.appendage20 = this.tail.getChild("appendage20");
        this.appendage_spike21 = this.appendage20.getChild("appendage_spike21");
        this.appendage4 = this.tail.getChild("appendage4");
        this.appendage_spike5 = this.appendage4.getChild("appendage_spike5");
        this.appendage21 = this.tail.getChild("appendage21");
        this.appendage_spike22 = this.appendage21.getChild("appendage_spike22");
        this.body = this.root.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.appendage5 = this.head.getChild("appendage5");
        this.appendage_spike6 = this.appendage5.getChild("appendage_spike6");
        this.appendage6 = this.head.getChild("appendage6");
        this.appendage_spike7 = this.appendage6.getChild("appendage_spike7");
        this.appendage7 = this.head.getChild("appendage7");
        this.appendage_spike8 = this.appendage7.getChild("appendage_spike8");
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
        this.appendage8 = this.body.getChild("appendage8");
        this.appendage_spike9 = this.appendage8.getChild("appendage_spike9");
        this.appendage22 = this.body.getChild("appendage22");
        this.appendage_spike23 = this.appendage22.getChild("appendage_spike23");
        this.appendage9 = this.body.getChild("appendage9");
        this.appendage_spike10 = this.appendage9.getChild("appendage_spike10");
        this.appendage23 = this.body.getChild("appendage23");
        this.appendage_spike24 = this.appendage23.getChild("appendage_spike24");
        this.appendage10 = this.body.getChild("appendage10");
        this.appendage_spike11 = this.appendage10.getChild("appendage_spike11");
        this.appendage24 = this.body.getChild("appendage24");
        this.appendage_spike25 = this.appendage24.getChild("appendage_spike25");
        this.appendage11 = this.body.getChild("appendage11");
        this.appendage_spike12 = this.appendage11.getChild("appendage_spike12");
        this.appendage25 = this.body.getChild("appendage25");
        this.appendage_spike26 = this.appendage25.getChild("appendage_spike26");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 20.4056F, 10.6297F));

        PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -4.5587F, -11.0817F));

        PartDefinition cube_r1 = tail.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, -0.75F, -4.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5587F, 11.0817F, -0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 22).addBox(-0.5F, -1.0F, -8.0F, 1.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 7.0F, -0.48F, 0.0F, 0.0F));

        PartDefinition appendage1 = tail.addOrReplaceChild("appendage1", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.653F, 12.452F, 0.2618F, 0.0F, -1.309F));

        PartDefinition appendage_spike2 = appendage1.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage36 = tail.addOrReplaceChild("appendage36", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1585F, 4.6105F, 13.3231F, 1.0212F, -0.2595F, -1.5605F));

        PartDefinition appendage_spike37 = appendage36.addOrReplaceChild("appendage_spike37", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage37 = tail.addOrReplaceChild("appendage37", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1585F, 4.6105F, 13.3231F, 1.0212F, 0.2595F, 1.5605F));

        PartDefinition appendage_spike38 = appendage37.addOrReplaceChild("appendage_spike38", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage18 = tail.addOrReplaceChild("appendage18", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.653F, 12.452F, 0.2618F, 0.0F, 1.309F));

        PartDefinition appendage_spike19 = appendage18.addOrReplaceChild("appendage_spike19", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage2 = tail.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.653F, 9.452F, -0.0436F, 0.0F, -1.309F));

        PartDefinition appendage_spike3 = appendage2.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage19 = tail.addOrReplaceChild("appendage19", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 4.653F, 9.452F, -0.0436F, 0.0F, 1.309F));

        PartDefinition appendage_spike20 = appendage19.addOrReplaceChild("appendage_spike20", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage38 = tail.addOrReplaceChild("appendage38", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1502F, 4.3457F, 13.0146F, 1.1458F, 0.0421F, -3.1414F));

        PartDefinition appendage_spike39 = appendage38.addOrReplaceChild("appendage_spike39", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage39 = tail.addOrReplaceChild("appendage39", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1502F, 4.3457F, 9.0146F, 0.8403F, 0.0421F, -3.1414F));

        PartDefinition appendage_spike40 = appendage39.addOrReplaceChild("appendage_spike40", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage40 = tail.addOrReplaceChild("appendage40", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1502F, 2.8457F, 4.0146F, 0.7531F, 0.0421F, -3.1414F));

        PartDefinition appendage_spike41 = appendage40.addOrReplaceChild("appendage_spike41", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage41 = tail.addOrReplaceChild("appendage41", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.1502F, 0.8457F, 0.0146F, 0.4476F, 0.0421F, -3.1414F));

        PartDefinition appendage_spike42 = appendage41.addOrReplaceChild("appendage_spike42", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage3 = tail.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.653F, 4.452F, -0.0426F, 0.0094F, -1.091F));

        PartDefinition appendage_spike4 = appendage3.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage20 = tail.addOrReplaceChild("appendage20", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 2.653F, 4.452F, -0.0426F, -0.0094F, 1.091F));

        PartDefinition appendage_spike21 = appendage20.addOrReplaceChild("appendage_spike21", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage4 = tail.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.653F, 0.452F, -0.0378F, 0.0218F, -0.7858F));

        PartDefinition appendage_spike5 = appendage4.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage21 = tail.addOrReplaceChild("appendage21", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.653F, 0.452F, -0.0378F, -0.0218F, 0.7858F));

        PartDefinition appendage_spike22 = appendage21.addOrReplaceChild("appendage_spike22", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -4.5218F, -10.9027F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 1).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -3.0F, -1.9635F, 0.0F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 1).addBox(-1.0F, -0.25F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7309F, 0.6119F, -1.2217F, 0.0F, 0.0F));

        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, -13.891F, 0.3916F));

        PartDefinition cube_r5 = neck.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(20, 16).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, -2.0071F, 0.0F, 0.0F));

        PartDefinition cube_r6 = neck.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 12).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -0.3301F, -2.618F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -7.0894F, 5.3857F));

        PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(14, 0).addBox(-0.5F, 0.0F, -8.5F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -6.0F, -7.0F, 0.48F, 0.0F, 0.0F));

        PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(20, 16).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2059F, -2.1704F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(20, 16).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0111F, 0.5161F, -1.0036F, 0.0F, 0.0F));

        PartDefinition appendage5 = head.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.4034F, -12.5043F, -2.4053F, 0.3035F, 2.8231F));

        PartDefinition appendage_spike6 = appendage5.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = head.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.4034F, -12.5043F, -2.4053F, -0.3035F, -2.8231F));

        PartDefinition appendage_spike7 = appendage6.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage7 = head.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.4034F, -12.5043F, -1.5708F, 0.0F, 3.1416F));

        PartDefinition appendage_spike8 = appendage7.addOrReplaceChild("appendage_spike8", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage32 = head.addOrReplaceChild("appendage32", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.4034F, -8.5043F, -1.309F, 0.0F, 3.1416F));

        PartDefinition appendage_spike33 = appendage32.addOrReplaceChild("appendage_spike33", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage33 = head.addOrReplaceChild("appendage33", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.5588F, -5.0889F, -0.9163F, 0.0F, 3.1416F));

        PartDefinition appendage_spike34 = appendage33.addOrReplaceChild("appendage_spike34", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage34 = head.addOrReplaceChild("appendage34", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.5741F, -2.0904F, -0.2618F, 0.0F, 3.1416F));

        PartDefinition appendage_spike35 = appendage34.addOrReplaceChild("appendage_spike35", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage35 = head.addOrReplaceChild("appendage35", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.1664F, -0.5362F, 0.6109F, 0.0F, 3.1416F));

        PartDefinition appendage_spike36 = appendage35.addOrReplaceChild("appendage_spike36", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage14 = head.addOrReplaceChild("appendage14", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3677F, -1.6526F, -0.3055F, 0.2056F, -0.0325F, -1.5316F));

        PartDefinition appendage_spike15 = appendage14.addOrReplaceChild("appendage_spike15", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage28 = head.addOrReplaceChild("appendage28", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.3677F, -1.6526F, -0.3055F, 0.2056F, 0.0325F, 1.5316F));

        PartDefinition appendage_spike29 = appendage28.addOrReplaceChild("appendage_spike29", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage15 = head.addOrReplaceChild("appendage15", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1419F, -3.8001F, -2.0017F, -0.1247F, -0.251F, -1.5445F));

        PartDefinition appendage_spike16 = appendage15.addOrReplaceChild("appendage_spike16", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage29 = head.addOrReplaceChild("appendage29", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1419F, -3.8001F, -2.0017F, -0.1247F, 0.251F, 1.5445F));

        PartDefinition appendage_spike30 = appendage29.addOrReplaceChild("appendage_spike30", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage16 = head.addOrReplaceChild("appendage16", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5005F, -5.2486F, -5.8553F, -0.4002F, 0.1905F, -1.5438F));

        PartDefinition appendage_spike17 = appendage16.addOrReplaceChild("appendage_spike17", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage30 = head.addOrReplaceChild("appendage30", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5005F, -5.2486F, -5.8553F, -0.4002F, -0.1905F, 1.5438F));

        PartDefinition appendage_spike31 = appendage30.addOrReplaceChild("appendage_spike31", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage17 = head.addOrReplaceChild("appendage17", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3809F, -4.1618F, -9.122F, -0.6264F, 0.2657F, -1.3833F));

        PartDefinition appendage_spike18 = appendage17.addOrReplaceChild("appendage_spike18", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage31 = head.addOrReplaceChild("appendage31", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.3809F, -4.1618F, -9.122F, -0.6264F, -0.2657F, 1.3833F));

        PartDefinition appendage_spike32 = appendage31.addOrReplaceChild("appendage_spike32", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage12 = neck.addOrReplaceChild("appendage12", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2683F, -1.6404F, 1.5568F, 0.3177F, -1.0864F, -1.6952F));

        PartDefinition appendage_spike13 = appendage12.addOrReplaceChild("appendage_spike13", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage26 = neck.addOrReplaceChild("appendage26", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2683F, -1.6404F, 1.5568F, 0.3177F, 1.0864F, 1.6952F));

        PartDefinition appendage_spike27 = appendage26.addOrReplaceChild("appendage_spike27", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage13 = neck.addOrReplaceChild("appendage13", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5349F, -5.5567F, 4.8244F, 0.2148F, -0.4618F, -1.4107F));

        PartDefinition appendage_spike14 = appendage13.addOrReplaceChild("appendage_spike14", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage27 = neck.addOrReplaceChild("appendage27", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.5349F, -5.5567F, 4.8244F, 0.2148F, 0.4618F, 1.4107F));

        PartDefinition appendage_spike28 = appendage27.addOrReplaceChild("appendage_spike28", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage8 = body.addOrReplaceChild("appendage8", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.3839F, -1.227F, -0.3963F, 0.3923F, -1.3271F));

        PartDefinition appendage_spike9 = appendage8.addOrReplaceChild("appendage_spike9", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage22 = body.addOrReplaceChild("appendage22", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.3839F, -1.227F, -0.3963F, -0.3923F, 1.3271F));

        PartDefinition appendage_spike23 = appendage22.addOrReplaceChild("appendage_spike23", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage9 = body.addOrReplaceChild("appendage9", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.3839F, -2.227F, -0.3325F, 0.1406F, -1.3614F));

        PartDefinition appendage_spike10 = appendage9.addOrReplaceChild("appendage_spike10", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage23 = body.addOrReplaceChild("appendage23", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.3839F, -2.227F, -0.3325F, -0.1406F, 1.3614F));

        PartDefinition appendage_spike24 = appendage23.addOrReplaceChild("appendage_spike24", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage10 = body.addOrReplaceChild("appendage10", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.3839F, -2.727F, -0.164F, -0.2374F, -1.4772F));

        PartDefinition appendage_spike11 = appendage10.addOrReplaceChild("appendage_spike11", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage24 = body.addOrReplaceChild("appendage24", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -8.3839F, -2.727F, -0.164F, 0.2374F, 1.4772F));

        PartDefinition appendage_spike25 = appendage24.addOrReplaceChild("appendage_spike25", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage11 = body.addOrReplaceChild("appendage11", CubeListBuilder.create().texOffs(19, 25).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2608F, -12.3942F, -0.727F, -0.0575F, -0.7193F, -1.5091F));

        PartDefinition appendage_spike12 = appendage11.addOrReplaceChild("appendage_spike12", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage25 = body.addOrReplaceChild("appendage25", CubeListBuilder.create().texOffs(19, 25).mirror().addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2608F, -12.3942F, -0.727F, -0.0575F, 0.7193F, 1.5091F));

        PartDefinition appendage_spike26 = appendage25.addOrReplaceChild("appendage_spike26", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimSilipede.SILIPEDE_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntitySilipede) entity).AS_isIdle, AnimSilipede.SILIPEDE_IDLE, ageInTicks, 1f);
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