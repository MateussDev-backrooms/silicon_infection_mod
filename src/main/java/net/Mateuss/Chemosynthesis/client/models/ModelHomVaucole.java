package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimHomVaucole;
import net.Mateuss.Chemosynthesis.client.animations.AnimVegRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.homunculoid.EntityHomVaucole;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelHomVaucole<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart vein;
    private final ModelPart tendril1;
    private final ModelPart tendril2;

    public ModelHomVaucole(ModelPart root) {
        this.root = root.getChild("root");
        this.vein = this.root.getChild("vein");
        this.tendril1 = this.vein.getChild("tendril1");
        this.tendril2 = this.vein.getChild("tendril2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition vein = root.addOrReplaceChild("vein", CubeListBuilder.create().texOffs(42, 67).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = vein.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -15.0F, -4.0F, 11.0F, 19.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -13.0F, 0.0F, -0.0057F, 0.0433F, -0.131F));

        PartDefinition cube_r2 = vein.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(40, 29).addBox(-4.0F, -5.0F, -5.5F, 10.0F, 10.0F, 10.0F, new CubeDeformation(2.0F)), PartPose.offsetAndRotation(-2.0F, -19.0F, 0.0F, 0.0F, 0.0F, 0.4363F));

        PartDefinition cube_r3 = vein.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 49).addBox(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -31.0F, 0.0F, 0.0F, -0.3054F, 0.0F));

        PartDefinition cube_r4 = vein.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(42, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -11.0F, 0.0F, 0.2333F, -0.3189F, -0.6485F));

        PartDefinition cube_r5 = vein.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 29).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, new CubeDeformation(1.0F)), PartPose.offsetAndRotation(-5.0F, -26.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

        PartDefinition cube_r6 = vein.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(58, 49).addBox(-6.0F, -2.25F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0213F, -20.9056F, 0.0F, 0.0F, 0.0F, 1.6144F));

        PartDefinition cube_r7 = vein.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(32, 49).addBox(-4.0F, -2.0F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.4889F, -29.9321F, 0.0F, 0.0F, 0.0F, 1.8326F));

        PartDefinition cube_r8 = vein.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(32, 49).addBox(-11.0F, -1.5F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.2947F, -9.917F, 0.0F, 0.0F, 0.0F, 1.2654F));

        PartDefinition cube_r9 = vein.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(58, 49).addBox(-4.0F, -2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.2947F, -9.917F, 0.0F, 0.0F, 0.0F, 0.829F));

        PartDefinition cube_r10 = vein.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(32, 49).addBox(-6.0F, -5.0F, -2.5F, 8.0F, 4.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.9233F, -1.3004F, 0.0F, 0.0F, 0.0F, 1.1345F));

        PartDefinition tendril1 = vein.addOrReplaceChild("tendril1", CubeListBuilder.create().texOffs(0, 67).addBox(-3.5F, -13.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, -32.0F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r11 = tendril1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 67).addBox(-3.0F, -10.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

        PartDefinition tendril2 = vein.addOrReplaceChild("tendril2", CubeListBuilder.create().texOffs(0, 67).addBox(-3.5F, -13.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -35.0F, 0.0F));

        PartDefinition cube_r12 = tendril2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 67).addBox(-3.0F, -10.0F, 0.0F, 7.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, 0.0F, 1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(((EntityHomVaucole) entity).AS_isIdle, AnimHomVaucole.HOMUNCULUS_VAUCOLE_IDLE, ageInTicks, 1f);
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