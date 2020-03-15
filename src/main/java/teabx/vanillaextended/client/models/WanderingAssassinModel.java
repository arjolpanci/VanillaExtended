package teabx.vanillaextended.client.models;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import teabx.vanillaextended.entities.WanderingAssassin;

public class WanderingAssassinModel extends EntityModel<WanderingAssassin> {

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

    public WanderingAssassinModel(){
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
        this.right_arm.setRotationPoint(-5.0F, 2.0F  - 8.0F, 0.0F);
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
    public void render(WanderingAssassin entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
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

    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }

}
