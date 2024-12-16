package net.Mateuss.Chemosynthesis.client.models;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.Mateuss.Chemosynthesis.client.animations.AnimTetheredZombie;
import net.Mateuss.Chemosynthesis.entity.living_entities.tethered.EntityTethZombie;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class ModelTethZombie<T extends Entity> extends HierarchicalModel<T> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart appendage9;
    private final ModelPart appendage_spike9;
    private final ModelPart appendage6;
    private final ModelPart appendage_spike6;
    private final ModelPart appendage7;
    private final ModelPart appendage_spike7;
    private final ModelPart appendage8;
    private final ModelPart appendage_spike8;
    private final ModelPart head;
    private final ModelPart appendage2;
    private final ModelPart appendage_spike2;
    private final ModelPart appendage3;
    private final ModelPart appendage_spike3;
    private final ModelPart appendage4;
    private final ModelPart appendage_spike4;
    private final ModelPart headwear;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public ModelTethZombie(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.appendage9 = this.body.getChild("appendage9");
        this.appendage_spike9 = this.appendage9.getChild("appendage_spike9");
        this.appendage6 = this.body.getChild("appendage6");
        this.appendage_spike6 = this.appendage6.getChild("appendage_spike6");
        this.appendage7 = this.body.getChild("appendage7");
        this.appendage_spike7 = this.appendage7.getChild("appendage_spike7");
        this.appendage8 = this.body.getChild("appendage8");
        this.appendage_spike8 = this.appendage8.getChild("appendage_spike8");
        this.head = this.body.getChild("head");
        this.appendage2 = this.head.getChild("appendage2");
        this.appendage_spike2 = this.appendage2.getChild("appendage_spike2");
        this.appendage3 = this.head.getChild("appendage3");
        this.appendage_spike3 = this.appendage3.getChild("appendage_spike3");
        this.appendage4 = this.head.getChild("appendage4");
        this.appendage_spike4 = this.appendage4.getChild("appendage_spike4");
        this.headwear = this.head.getChild("headwear");
        this.left_arm = this.body.getChild("left_arm");
        this.right_arm = this.body.getChild("right_arm");
        this.left_leg = this.root.getChild("left_leg");
        this.right_leg = this.root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));

        PartDefinition appendage9 = body.addOrReplaceChild("appendage9", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(0.1703F, 5.7379F, -2.2933F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.6189F, 2.6135F, 0.0824F, 1.6268F, -0.4053F, 2.7111F));

        PartDefinition appendage_spike9 = appendage9.addOrReplaceChild("appendage_spike9", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(0.6703F, -6.7621F, -0.7933F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition appendage6 = body.addOrReplaceChild("appendage6", CubeListBuilder.create().texOffs(0, 32).addBox(-3.1703F, 5.7379F, -2.2933F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6189F, 2.6135F, 0.0824F, 1.6268F, 0.4053F, -2.7111F));

        PartDefinition appendage_spike6 = appendage6.addOrReplaceChild("appendage_spike6", CubeListBuilder.create().texOffs(0, 49).addBox(-1.6703F, -6.7621F, -0.7933F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage7 = body.addOrReplaceChild("appendage7", CubeListBuilder.create().texOffs(0, 32).addBox(-2.8618F, 5.1415F, -2.0478F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6189F, 6.6135F, 0.0824F, 1.817F, 0.7157F, -2.6958F));

        PartDefinition appendage_spike7 = appendage7.addOrReplaceChild("appendage_spike7", CubeListBuilder.create().texOffs(0, 49).addBox(-1.3618F, -7.3585F, -0.5478F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage8 = body.addOrReplaceChild("appendage8", CubeListBuilder.create().texOffs(0, 32).mirror().addBox(-0.1382F, 5.1415F, -2.0478F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.6189F, 6.6135F, 0.0824F, 1.817F, -0.7157F, 2.6958F));

        PartDefinition appendage_spike8 = appendage8.addOrReplaceChild("appendage_spike8", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(0.3618F, -7.3585F, -0.5478F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, 6.5F, -0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition appendage2 = head.addOrReplaceChild("appendage2", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -4.5F, -2.0F, 0.09F, 0.3715F, -2.2287F));

        PartDefinition appendage_spike2 = appendage2.addOrReplaceChild("appendage_spike2", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage3 = head.addOrReplaceChild("appendage3", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -4.5F, -3.0F, -0.8311F, 0.2925F, -3.0675F));

        PartDefinition appendage_spike3 = appendage3.addOrReplaceChild("appendage_spike3", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition appendage4 = head.addOrReplaceChild("appendage4", CubeListBuilder.create().texOffs(0, 32).addBox(-1.5F, 6.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -5.5F, -2.0F, 0.1687F, 0.045F, -3.0069F));

        PartDefinition appendage_spike4 = appendage4.addOrReplaceChild("appendage_spike4", CubeListBuilder.create().texOffs(0, 49).addBox(0.0F, -6.0F, 0.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 6.5F, -0.5F));

        PartDefinition headwear = head.addOrReplaceChild("headwear", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, -12.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, -12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animateWalk(AnimTetheredZombie.TETH_ZOMBIE_RUN, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(((EntityTethZombie) entity).AS_isIdle, AnimTetheredZombie.TETH_ZOMBIE_IDLE, ageInTicks, 1f);
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