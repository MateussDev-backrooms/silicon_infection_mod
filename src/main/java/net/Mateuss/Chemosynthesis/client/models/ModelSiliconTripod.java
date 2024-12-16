package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimSiliconRoller;
import net.Mateuss.Chemosynthesis.entity.living_entities.pure.EntitySiliconTripod;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelSiliconTripod<T extends Entity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    private final ModelPart root;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage5;
    private final ModelPart appendage_spike5;
    private final ModelPart appendage6;
    private final ModelPart appendage_spike6;

    public ModelSiliconTripod(ModelPart root) {
        this.root = root.getChild("root");
        this.appendage2 = this.root.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.appendage5 = this.root.getChild("appendage5");
        this.appendage_spike5 = this.appendage5.getChild("appendage_spike5");
        this.appendage6 = this.root.getChild("appendage6");
        this.appendage_spike6 = this.appendage6.getChild("appendage_spike6");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.5F, 0.0F));

        PartDefinition appendage2 = root.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage5 = root.addOrReplaceChild("appendage5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.9163F));

        PartDefinition appendage_spike5 = appendage5.addOrReplaceChild("appendage_spike5", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = root.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.9163F));

        PartDefinition appendage_spike6 = appendage6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimSiliconRoller.SILICON_TRIPOD_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntitySiliconTripod) entity).AS_isIdle, AnimSiliconRoller.SILICON_TRIPOD_IDLE, ageInTicks, 1f);
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