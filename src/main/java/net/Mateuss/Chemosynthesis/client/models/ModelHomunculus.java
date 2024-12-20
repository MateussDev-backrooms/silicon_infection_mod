package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimHomunculus;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomunculus;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelHomunculus<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart blobulus;
    private final ModelPart blobulus2;
    private final ModelPart vein1;
    private final ModelPart vein2;
    private final ModelPart vein3;
    private final ModelPart vein4;
    private final ModelPart siliconite1;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart appendage14;
    private final ModelPart appendage28;
    private final ModelPart appendage15;
    private final ModelPart appendage29;
    private final ModelPart appendage12;
    private final ModelPart appendage26;
    private final ModelPart appendage13;
    private final ModelPart appendage27;
    private final ModelPart appendage8;
    private final ModelPart appendage22;
    private final ModelPart appendage9;
    private final ModelPart appendage23;
    private final ModelPart appendage10;
    private final ModelPart appendage24;
    private final ModelPart appendage11;
    private final ModelPart appendage25;
    private final ModelPart siliconite2;
    private final ModelPart neck2;
    private final ModelPart head2;
    private final ModelPart appendage2;
    private final ModelPart appendage3;
    private final ModelPart appendage4;
    private final ModelPart appendage5;
    private final ModelPart appendage6;
    private final ModelPart appendage7;
    private final ModelPart appendage16;
    private final ModelPart appendage17;
    private final ModelPart appendage18;
    private final ModelPart appendage19;
    private final ModelPart appendage20;
    private final ModelPart appendage21;
    private final ModelPart appendage30;
    private final ModelPart appendage31;
    private final ModelPart appendage32;
    private final ModelPart appendage33;

    public ModelHomunculus(ModelPart root) {
        this.root = root.getChild("root");
        this.blobulus = this.root.getChild("blobulus");
        this.blobulus2 = this.root.getChild("blobulus2");
        this.vein1 = this.root.getChild("vein1");
        this.vein2 = this.root.getChild("vein2");
        this.vein3 = this.root.getChild("vein3");
        this.vein4 = this.root.getChild("vein4");
        this.siliconite1 = this.root.getChild("siliconite1");
        this.neck = this.siliconite1.getChild("neck");
        this.head = this.neck.getChild("head");
        this.appendage14 = this.head.getChild("appendage14");
        this.appendage28 = this.head.getChild("appendage28");
        this.appendage15 = this.head.getChild("appendage15");
        this.appendage29 = this.head.getChild("appendage29");
        this.appendage12 = this.neck.getChild("appendage12");
        this.appendage26 = this.neck.getChild("appendage26");
        this.appendage13 = this.neck.getChild("appendage13");
        this.appendage27 = this.neck.getChild("appendage27");
        this.appendage8 = this.siliconite1.getChild("appendage8");
        this.appendage22 = this.siliconite1.getChild("appendage22");
        this.appendage9 = this.siliconite1.getChild("appendage9");
        this.appendage23 = this.siliconite1.getChild("appendage23");
        this.appendage10 = this.siliconite1.getChild("appendage10");
        this.appendage24 = this.siliconite1.getChild("appendage24");
        this.appendage11 = this.siliconite1.getChild("appendage11");
        this.appendage25 = this.siliconite1.getChild("appendage25");
        this.siliconite2 = this.root.getChild("siliconite2");
        this.neck2 = this.siliconite2.getChild("neck2");
        this.head2 = this.neck2.getChild("head2");
        this.appendage2 = this.head2.getChild("appendage2");
        this.appendage3 = this.head2.getChild("appendage3");
        this.appendage4 = this.head2.getChild("appendage4");
        this.appendage5 = this.head2.getChild("appendage5");
        this.appendage6 = this.neck2.getChild("appendage6");
        this.appendage7 = this.neck2.getChild("appendage7");
        this.appendage16 = this.neck2.getChild("appendage16");
        this.appendage17 = this.neck2.getChild("appendage17");
        this.appendage18 = this.siliconite2.getChild("appendage18");
        this.appendage19 = this.siliconite2.getChild("appendage19");
        this.appendage20 = this.siliconite2.getChild("appendage20");
        this.appendage21 = this.siliconite2.getChild("appendage21");
        this.appendage30 = this.siliconite2.getChild("appendage30");
        this.appendage31 = this.siliconite2.getChild("appendage31");
        this.appendage32 = this.siliconite2.getChild("appendage32");
        this.appendage33 = this.siliconite2.getChild("appendage33");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition blobulus = root.addOrReplaceChild("blobulus", CubeListBuilder.create(), PartPose.offset(-0.1177F, -23.0F, 0.1246F));

        PartDefinition cube_r1 = blobulus.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.8938F, -7.4436F, -3.0002F, 11.0F, 19.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.8936F, 1.4329F, -1.8907F));

        PartDefinition cube_r2 = blobulus.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 81).addBox(-9.929F, 3.7564F, -5.5429F, 10.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.9454F, 0.8584F, -1.0706F));

        PartDefinition cube_r3 = blobulus.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(40, 69).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1246F, -10.0F, -3.1177F, 2.1379F, 1.1599F, 2.1282F));

        PartDefinition cube_r4 = blobulus.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(42, 23).addBox(-5.0F, -5.0F, -2.0F, 10.0F, 13.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8754F, -0.5356F, -6.293F, 0.0F, 1.2654F, 0.0F));

        PartDefinition cube_r5 = blobulus.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(42, 0).addBox(-8.8962F, -9.2576F, -5.1246F, 10.0F, 13.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 3.0F, -4.0F, -1.5708F, 1.3526F, -1.5708F));

        PartDefinition blobulus2 = root.addOrReplaceChild("blobulus2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.8823F, -34.0F, 2.1246F, 2.9099F, 0.3405F, 3.063F));

        PartDefinition cube_r6 = blobulus2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-2.9721F, -3.6796F, -3.9899F, 11.0F, 19.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.8891F, 1.327F, 1.7405F));

        PartDefinition cube_r7 = blobulus2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(82, 18).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1246F, -16.0F, -1.1177F, -1.4354F, 0.9642F, -1.2854F));

        PartDefinition cube_r8 = blobulus2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(82, 0).addBox(-4.879F, -15.0F, -3.8535F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.2654F, 0.0F));

        PartDefinition cube_r9 = blobulus2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(80, 69).addBox(-4.6551F, -5.2137F, -2.0276F, 10.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1246F, -10.0F, -3.1177F, 2.1379F, 1.1599F, 2.1282F));

        PartDefinition cube_r10 = blobulus2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 58).addBox(-10.2038F, -6.4548F, -3.6151F, 10.0F, 13.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.8754F, -0.5356F, -6.293F, 2.9508F, 1.183F, 2.8574F));

        PartDefinition cube_r11 = blobulus2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(42, 46).addBox(-8.8962F, -9.2576F, -5.1246F, 10.0F, 13.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 3.0F, -4.0F, -1.5708F, 1.3526F, -1.5708F));

        PartDefinition vein1 = root.addOrReplaceChild("vein1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, -4.0F, 0.0F, -0.9163F, 0.0F));

        PartDefinition cube_r12 = vein1.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(52, 103).addBox(-2.0F, -4.0F, -1.25F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.75F, -6.0F, 2.3562F, 0.0F, 0.0F));

        PartDefinition cube_r13 = vein1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(38, 99).addBox(-2.5F, -5.0F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.5F, -1.0F, 1.789F, 0.0F, 0.0F));

        PartDefinition cube_r14 = vein1.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(58, 89).addBox(-2.0F, -11.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, -2.0F, 0.0F, 0.0F, -0.1309F));

        PartDefinition vein2 = root.addOrReplaceChild("vein2", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, -10.0F, -2.0F, 0.0F, 0.9163F, 0.0F));

        PartDefinition cube_r15 = vein2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(52, 103).addBox(-2.0F, -4.0F, -1.25F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.75F, -6.0F, 2.3562F, 0.0F, 0.0F));

        PartDefinition cube_r16 = vein2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(38, 99).addBox(-2.5F, -5.0F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.5F, -1.0F, 1.789F, 0.0F, 0.0F));

        PartDefinition cube_r17 = vein2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(58, 89).addBox(-2.0F, -11.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, -2.0F, -0.2182F, 0.0F, -0.1309F));

        PartDefinition vein3 = root.addOrReplaceChild("vein3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -10.0F, 1.0F, 0.0F, 2.8362F, 0.0F));

        PartDefinition cube_r18 = vein3.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(52, 103).addBox(-2.0F, -4.0F, -1.25F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.75F, -6.0F, 2.3998F, 0.0F, 0.0F));

        PartDefinition cube_r19 = vein3.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(38, 99).addBox(-2.5F, -5.0F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.5F, -1.0F, 1.789F, 0.0F, 0.0F));

        PartDefinition cube_r20 = vein3.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(58, 89).addBox(-2.0F, -11.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, -2.0F, -0.2182F, 0.0F, -0.1309F));

        PartDefinition vein4 = root.addOrReplaceChild("vein4", CubeListBuilder.create(), PartPose.offsetAndRotation(4.0F, -10.0F, 1.0F, 0.0F, -2.5307F, 0.0F));

        PartDefinition cube_r21 = vein4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(52, 103).addBox(-2.0F, -4.0F, -1.25F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.75F, -6.0F, 2.3126F, 0.0F, 0.0F));

        PartDefinition cube_r22 = vein4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(38, 99).addBox(-2.5F, -5.0F, -1.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 7.5F, -1.0F, 1.789F, 0.0F, 0.0F));

        PartDefinition cube_r23 = vein4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(58, 89).addBox(-2.0F, -11.0F, -1.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 8.0F, -2.0F, -0.3897F, 0.05F, -0.0099F));

        PartDefinition siliconite1 = root.addOrReplaceChild("siliconite1", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, -36.1161F, -7.273F, -0.7474F, -0.3408F, 1.1039F));

        PartDefinition cube_r24 = siliconite1.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(82, 34).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -3.0F, -1.9635F, 0.0F, 0.0F));

        PartDefinition cube_r25 = siliconite1.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(82, 34).addBox(-1.0F, -0.25F, -8.5F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7309F, 0.6119F, -1.2217F, 0.0F, 0.0F));

        PartDefinition neck = siliconite1.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -14.891F, -0.3584F, -1.1345F, 0.0F, 3.1416F));

        PartDefinition cube_r26 = neck.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(106, 89).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, -2.0071F, 0.0F, 0.0F));

        PartDefinition cube_r27 = neck.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(102, 43).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -0.3301F, -2.618F, 0.0F, 0.0F));

        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0894F, 5.3857F, -0.2639F, -0.3051F, 0.0138F));

        PartDefinition cube_r28 = head.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(106, 89).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2059F, -2.1704F, -0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r29 = head.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(106, 89).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0111F, 0.5161F, -1.0036F, 0.0F, 0.0F));

        PartDefinition appendage14 = head.addOrReplaceChild("appendage14", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3677F, -1.6526F, -0.3055F, 0.2056F, -0.0325F, -1.5316F));

        PartDefinition appendage28 = head.addOrReplaceChild("appendage28", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3677F, -1.6526F, -0.3055F, 0.6063F, -0.5046F, 1.645F));

        PartDefinition appendage15 = head.addOrReplaceChild("appendage15", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1419F, -3.8001F, -2.0017F, 0.2135F, -0.5329F, -1.9618F));

        PartDefinition appendage29 = head.addOrReplaceChild("appendage29", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1419F, -3.8001F, -2.0017F, 0.0455F, 0.276F, 2.1775F));

        PartDefinition appendage12 = neck.addOrReplaceChild("appendage12", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2683F, -1.6404F, 1.5568F, 0.3177F, -1.0864F, -1.6952F));

        PartDefinition appendage26 = neck.addOrReplaceChild("appendage26", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2683F, -1.6404F, 1.5568F, 0.3177F, 1.0864F, 1.6952F));

        PartDefinition appendage13 = neck.addOrReplaceChild("appendage13", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5349F, -5.5567F, 4.8244F, 0.2148F, -0.4618F, -1.4107F));

        PartDefinition appendage27 = neck.addOrReplaceChild("appendage27", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5349F, -5.5567F, 4.8244F, 0.2148F, 0.4618F, 1.4107F));

        PartDefinition appendage8 = siliconite1.addOrReplaceChild("appendage8", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.3839F, -1.227F, -0.9038F, 0.2621F, -1.142F));

        PartDefinition appendage22 = siliconite1.addOrReplaceChild("appendage22", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.3839F, -1.227F, 0.2146F, -0.3923F, 1.3271F));

        PartDefinition appendage9 = siliconite1.addOrReplaceChild("appendage9", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.3839F, -2.227F, -0.6311F, -0.0354F, -1.2321F));

        PartDefinition appendage23 = siliconite1.addOrReplaceChild("appendage23", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.3839F, -2.227F, -0.0271F, -0.1406F, 1.3614F));

        PartDefinition appendage10 = siliconite1.addOrReplaceChild("appendage10", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.3839F, -2.727F, -0.382F, -0.5236F, -1.3617F));

        PartDefinition appendage24 = siliconite1.addOrReplaceChild("appendage24", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.3839F, -2.727F, -0.164F, 0.2374F, 1.4772F));

        PartDefinition appendage11 = siliconite1.addOrReplaceChild("appendage11", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2608F, -12.3942F, -0.727F, -0.2756F, -0.7193F, -1.5091F));

        PartDefinition appendage25 = siliconite1.addOrReplaceChild("appendage25", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2608F, -12.3942F, -0.727F, -0.2426F, 0.5506F, 1.5646F));

        PartDefinition siliconite2 = root.addOrReplaceChild("siliconite2", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -15.1161F, 2.727F, 1.7595F, 0.8698F, 1.3517F));

        PartDefinition cube_r30 = siliconite2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(82, 34).addBox(-1.0F, -1.0F, -8.0F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -3.0F, -1.9635F, 0.0F, 0.0F));

        PartDefinition cube_r31 = siliconite2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(82, 34).addBox(-1.2535F, -1.4979F, -8.5596F, 2.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7309F, 0.6119F, -1.0908F, 0.0F, 0.0F));

        PartDefinition neck2 = siliconite2.addOrReplaceChild("neck2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -14.891F, -0.3584F, -1.2294F, 0.0262F, -2.8565F));

        PartDefinition cube_r32 = neck2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(106, 89).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 4.0F, -2.0071F, 0.0F, 0.0F));

        PartDefinition cube_r33 = neck2.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(102, 43).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -0.3301F, -2.618F, 0.0F, 0.0F));

        PartDefinition head2 = neck2.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -7.0894F, 5.3857F, -0.9319F, -0.3172F, 0.3665F));

        PartDefinition cube_r34 = head2.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(106, 89).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.2059F, -2.1704F, -0.6545F, 0.0F, 0.0F));

        PartDefinition cube_r35 = head2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(106, 89).addBox(-0.5F, -1.0F, -5.5F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0111F, 0.5161F, -1.0036F, 0.0F, 0.0F));

        PartDefinition appendage2 = head2.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3677F, -1.6526F, -0.3055F, 0.1221F, 0.1511F, -1.5971F));

        PartDefinition appendage3 = head2.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3677F, -1.6526F, -0.3055F, 0.8418F, -0.8249F, 1.6256F));

        PartDefinition appendage4 = head2.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1419F, -3.8001F, -2.0017F, 0.154F, -0.8736F, -2.0737F));

        PartDefinition appendage5 = head2.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1419F, -3.8001F, -2.0017F, 0.257F, 0.2326F, 2.4893F));

        PartDefinition appendage6 = neck2.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2683F, -1.6404F, 1.5568F, -0.0256F, -1.1171F, -1.2138F));

        PartDefinition appendage7 = neck2.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2683F, -1.6404F, 1.5568F, 0.8189F, 0.8663F, 2.2912F));

        PartDefinition appendage16 = neck2.addOrReplaceChild("appendage16", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5349F, -5.5567F, 4.8244F, 0.2294F, -0.9521F, -1.349F));

        PartDefinition appendage17 = neck2.addOrReplaceChild("appendage17", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5349F, -5.5567F, 4.8244F, 0.5937F, 0.4011F, 1.6932F));

        PartDefinition appendage18 = siliconite2.addOrReplaceChild("appendage18", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4528F, -2.4052F, -1.1831F, -0.3963F, 0.3923F, -1.3271F));

        PartDefinition appendage19 = siliconite2.addOrReplaceChild("appendage19", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4597F, -1.633F, -0.3746F, -0.3963F, -0.3923F, 1.3271F));

        PartDefinition appendage20 = siliconite2.addOrReplaceChild("appendage20", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.3839F, -2.227F, -0.3325F, 0.1406F, -1.3614F));

        PartDefinition appendage21 = siliconite2.addOrReplaceChild("appendage21", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.3839F, -2.227F, -0.338F, -0.1262F, 1.403F));

        PartDefinition appendage30 = siliconite2.addOrReplaceChild("appendage30", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.3839F, -2.727F, -0.164F, -0.2374F, -1.4772F));

        PartDefinition appendage31 = siliconite2.addOrReplaceChild("appendage31", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.3839F, -2.727F, -0.2622F, 0.2567F, 1.6108F));

        PartDefinition appendage32 = siliconite2.addOrReplaceChild("appendage32", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2608F, -12.3942F, -0.727F, -0.0575F, -0.7193F, -1.5091F));

        PartDefinition appendage33 = siliconite2.addOrReplaceChild("appendage33", CubeListBuilder.create().texOffs(126, 0).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(114, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2608F, -12.3942F, -0.727F, -0.4093F, 0.5028F, 1.5717F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

//        this.animateWalk(ModAnimDefinitions.TETH_SHEEP_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityHomunculus) entity).AS_isIdle, AnimHomunculus.HOMUNCULUS_HEARTBEAT, ageInTicks, 1f);
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