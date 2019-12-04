package teabx.vanillaextended.client.models;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import teabx.vanillaextended.entities.SkeletonKing;

public class SkeletonKingModel extends EntityModel<SkeletonKing> implements IHasArm {

    public RendererModel right_arm;
    public RendererModel right_leg;
    public RendererModel head;
    public RendererModel torso;
    public RendererModel left_arm;
    public RendererModel left_leg;
    public RendererModel crown_front;
    public RendererModel crown_right;
    public RendererModel crown_rear;
    public RendererModel crown_left;
    public RendererModel crown_jewel_m;
    public RendererModel crown_jewel_r;
    public RendererModel crown_jewel_l;

    public SkeletonKingModel() {
        this.textureWidth = 64;
        this.textureHeight = 32;

        this.head = new RendererModel(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F - 8.0F, -4.0F, 8, 8, 8, 0.0F);

        this.torso = new RendererModel(this, 16, 16);
        this.torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.torso.addBox(-4.0F, 0.0F - 8.0F, -2.0F, 8, 12, 4, 0.0F);

        this.left_leg = new RendererModel(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setRotationPoint(2.0F, 12.0F - 8.0F, 0.1F);
        this.left_leg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);
        this.right_leg = new RendererModel(this, 0, 16);
        this.right_leg.setRotationPoint(-2.0F, 12.0F - 8.0F, 0.1F);
        this.right_leg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, 0.0F);

        this.right_arm = new RendererModel(this, 40, 16);
        this.right_arm.setRotationPoint(-5.0F, 2.0F  - 4.0F, 0.0F);
        this.right_arm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
        this.setRotateAngle(right_arm, 0.0F, 0.0F - 4.0F, 0.10000736613927509F);
        this.left_arm = new RendererModel(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setRotationPoint(5.0F, 2.0F  - 8.0F, 0.0F);
        this.left_arm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
        this.setRotateAngle(left_arm, 0.0F, 0.0F, -0.10000736613927509F);

        this.crown_jewel_l = new RendererModel(this, 50, 21);
        this.crown_jewel_l.setRotationPoint(1.8F, -11.7F, -4.5F);
        this.crown_jewel_l.addBox(0.0F, 0.0F - 8.0F, 0.0F, 2, 2, 1, 0.0F);
        this.crown_left = new RendererModel(this, 25, 0);
        this.crown_left.setRotationPoint(3.9F, -8.9F, 4.0F);
        this.crown_left.addBox(0.0F, -1.0F - 8.0F, 0.0F, 8, 2, 1, 0.0F);
        this.setRotateAngle(crown_left, 0.0F, 1.5707963267948966F, 0.0F);
        this.crown_jewel_m = new RendererModel(this, 57, 20);
        this.crown_jewel_m.setRotationPoint(-0.5F, -12.8F, -4.5F);
        this.crown_jewel_m.addBox(-0.5F, 0.0F - 8.0F, 0.0F, 2, 4, 1, 0.0F);
        this.crown_front = new RendererModel(this, 44, 0);
        this.crown_front.setRotationPoint(-4.4F, -8.9F, -4.6F);
        this.crown_front.addBox(0.0F, -1.0F - 8.0F, 0.0F, 9, 2, 1, 0.0F);
        this.crown_jewel_r = new RendererModel(this, 57, 27);
        this.crown_jewel_r.setRotationPoint(-3.3F, -11.7F, -4.5F);
        this.crown_jewel_r.addBox(-0.5F, 0.0F - 8.0F, 0.0F, 2, 2, 1, 0.0F);
        this.crown_right = new RendererModel(this, 45, 4);
        this.crown_right.setRotationPoint(-4.9F, -8.9F, 4.1F);
        this.crown_right.addBox(0.0F, -1.0F - 8.0F, 0.0F, 8, 2, 1, 0.0F);
        this.setRotateAngle(crown_right, 0.0F, 1.5707963267948966F, 0.0F);
        this.crown_rear = new RendererModel(this, 44, 10);
        this.crown_rear.setRotationPoint(-4.4F, -8.9F, 4.0F);
        this.crown_rear.addBox(0.0F, -1.0F - 8.0F, 0.0F, 9, 2, 1, 0.0F);

    }

    @Override
    public void render(SkeletonKing entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.torso.render(scale *1.5F);
        this.crown_rear.render(scale *1.5F);
        this.left_leg.render(scale *1.5F);
        this.right_arm.render(scale *1.5F);
        this.crown_jewel_l.render(scale *1.5F);
        this.crown_left.render(scale *1.5F);
        this.crown_jewel_m.render(scale *1.5F);
        this.crown_front.render(scale *1.5F);
        this.left_arm.render(scale *1.5F);
        this.head.render(scale *1.5F);
        this.crown_jewel_r.render(scale *1.5F);
        this.crown_right.render(scale *1.5F);
        this.right_leg.render(scale *1.5F);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }

    @Override
    public void postRenderArm(float scale, HandSide side) {
        float f = side == HandSide.RIGHT ? 0.5F : -0.5F;
        RendererModel renderermodel = this.right_arm;
        renderermodel.rotationPointX += f;
        renderermodel.rotationPointY += f + 3.5F;
        renderermodel.postRender(scale * 1.5F);
        renderermodel.rotationPointX -= f;
        renderermodel.rotationPointY -= f + 3.5F;
    }

    @Override
    public void setRotationAngles(SkeletonKing entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        if(entityIn.isAggressive()){
            float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
            this.right_arm.rotateAngleZ = 0.0F;
            this.left_arm.rotateAngleZ = 0.0F;
            this.right_arm.rotateAngleY = -(0.1F - f * 0.6F);
            this.left_arm.rotateAngleY = 0.1F - f * 0.6F;
            this.right_arm.rotateAngleX = (-(float)Math.PI / 2F);
            this.left_arm.rotateAngleX = (-(float)Math.PI / 2F);
            this.right_arm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
            this.left_arm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
            this.right_arm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.left_arm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.right_arm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.left_arm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

        }

        this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / 1.0F;
        this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount / 1.0F;
        this.right_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 0.6F * limbSwingAmount / 1.0F;
        this.left_arm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.5F * limbSwingAmount / 1.0F;
    }
}
