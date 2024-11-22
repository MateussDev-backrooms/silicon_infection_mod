package net.mateuss.chemosynthesis.entity.client;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mateuss.chemosynthesis.entity.animations.ModAnimDefinitions;
import net.mateuss.chemosynthesis.entity.custom.EntityHomunculus;
import net.mateuss.chemosynthesis.entity.custom.EntityTethSheep;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelHomunculus<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart back_veins;
    private final ModelPart second_vein;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart heart;

    public ModelHomunculus(ModelPart root) {
        this.root = root.getChild("root");
        this.back_veins = this.root.getChild("back_veins");
        this.second_vein = this.back_veins.getChild("second_vein");
        this.appendage2 = this.second_vein.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.appendage3 = this.second_vein.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.heart = this.back_veins.getChild("heart");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition back_veins = root.addOrReplaceChild("back_veins", CubeListBuilder.create().texOffs(42, 29).addBox(-3.75F, -23.0F, -4.0F, 8.0F, 23.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(-5.75F, -22.75F, -6.25F, 11.0F, 18.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 0.0F, 4.0F));

        PartDefinition cube_r1 = back_veins.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 56).addBox(-4.0F, -19.0F, -1.0F, 7.0F, 21.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -10.0F, -2.5F, -0.0869F, 0.0076F, 0.0869F));

        PartDefinition second_vein = back_veins.addOrReplaceChild("second_vein", CubeListBuilder.create(), PartPose.offset(-3.0F, -15.0F, -1.75F));

        PartDefinition cube_r2 = second_vein.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(28, 73).addBox(-3.0F, -19.0F, 0.0F, 4.0F, 21.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.1744F, -0.0076F, -0.043F));

        PartDefinition cube_r3 = second_vein.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(50, 0).addBox(-6.0F, -15.0F, -3.0F, 8.0F, 10.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 6.0F, -0.75F, 0.1736F, 0.0226F, 0.0902F));

        PartDefinition cube_r4 = second_vein.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(72, 21).addBox(-3.0F, -19.0F, 0.0F, 4.0F, 21.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 1.0F, -2.5F, 0.0852F, 0.0189F, -0.2174F));

        PartDefinition appendage2 = second_vein.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(50, 21).addBox(-1.4074F, 6.1166F, -1.6736F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -7.5F, -5.25F, -0.3007F, 0.0448F, -2.8409F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(62, 21).addBox(-1.9074F, -5.3986F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 5.5152F, -0.6736F));

        PartDefinition appendage3 = second_vein.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(50, 21).addBox(-1.4074F, 6.1166F, -1.6736F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -7.5F, -5.25F, -0.903F, -0.0365F, 3.1307F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(62, 21).addBox(-1.9074F, -5.3986F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 5.5152F, -0.6736F));

        PartDefinition heart = back_veins.addOrReplaceChild("heart", CubeListBuilder.create(), PartPose.offset(0.0F, -12.0F, -7.5F));

        PartDefinition cube_r5 = heart.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(28, 59).addBox(-8.0F, -8.0F, -12.0F, 12.0F, 6.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(68, 59).addBox(-2.0F, -15.0F, -11.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.0F, -17.0F, -10.0F, 12.0F, 16.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 9.0F, 0.0F, -0.3054F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

//        this.animateWalk(ModAnimDefinitions.TETH_SHEEP_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityHomunculus) entity).AS_isIdle, ModAnimDefinitions.HOMUNCULUS_HEARTBEAT, ageInTicks, 1f);
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