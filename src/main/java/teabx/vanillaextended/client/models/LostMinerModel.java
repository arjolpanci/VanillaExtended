package teabx.vanillaextended.client.models;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import teabx.vanillaextended.entities.LostMiner;


@OnlyIn(Dist.CLIENT)
public class LostMinerModel extends BipedModel<LostMiner> {
    public RendererModel right_arm;
    public RendererModel right_leg;
    public RendererModel head;
    public RendererModel torso;
    public RendererModel left_arm;
    public RendererModel left_leg;
    public RendererModel helmet_1;
    public RendererModel helmet_2;
    public RendererModel pick_body;
    public RendererModel pick_head;

    public LostMinerModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.right_leg = new RendererModel(this, 0, 16);
        this.right_leg.setRotationPoint(-1.9F, 12.0F, 0.1F);
        this.right_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.torso = new RendererModel(this, 16, 16);
        this.torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.torso.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
        this.helmet_2 = new RendererModel(this, 0, 55);
        this.helmet_2.setRotationPoint(-2.5F, -11.9F, -2.6F);
        this.helmet_2.addBox(0.0F, 0.0F, 0.0F, 5, 2, 5, 0.0F);
        this.setRotateAngle(helmet_2, 0.0F, 0.0F, -0.017453292519943295F);
        this.helmet_1 = new RendererModel(this, 20, 50);
        this.helmet_1.setRotationPoint(-5.0F, -10.0F, -5.2F);
        this.helmet_1.addBox(0.0F, 0.0F, 0.0F, 10, 2, 10, 0.0F);
        this.pick_head = new RendererModel(this, 10, 35);
        this.pick_head.setRotationPoint(-1.2F, 13.2F, -10.4F);
        this.pick_head.addBox(0.0F, 0.0F, 0.0F, 1, 1, 10, 0.0F);
        this.setRotateAngle(pick_head, 1.5707963267948966F, 0.0F, 0.0F);
        this.left_leg = new RendererModel(this, 0, 16);
        this.left_leg.mirror = true;
        this.left_leg.setRotationPoint(1.9F, 12.0F, 0.1F);
        this.left_leg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.pick_body = new RendererModel(this, 0, 40);
        this.pick_body.setRotationPoint(-1.2F, 8.5F, -11.0F);
        this.pick_body.addBox(0.0F, 0.0F, 0.0F, 1, 10, 1, 0.0F);
        this.setRotateAngle(pick_body, 1.5707963267948966F, 0.0F, 0.0F);
        this.right_arm = new RendererModel(this, 40, 16);
        this.right_arm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.right_arm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.setRotateAngle(right_arm, -1.0821041362364843F, 0.0F, 0.0F);
        this.head = new RendererModel(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
        this.left_arm = new RendererModel(this, 40, 16);
        this.left_arm.mirror = true;
        this.left_arm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.left_arm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.right_arm.addChild(this.pick_head);
        this.right_arm.addChild(this.pick_body);
    }

    @Override
    public void render(LostMiner entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.right_leg.render(scale);
        this.torso.render(scale);
        this.helmet_2.render(scale);
        this.helmet_1.render(scale);
        this.left_leg.render(scale);
        this.right_arm.render(scale);
        this.head.render(scale);
        this.left_arm.render(scale);
    }

    @Override
    public void setRotationAngles(LostMiner entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
        this.right_arm.rotateAngleZ = 0.0F;
        this.left_arm.rotateAngleZ = 0.0F;
        this.right_arm.rotateAngleY = -(0.1F - f * 1.4F);
        this.left_arm.rotateAngleY = 0.1F - f * 0.6F;
        float f2 = -(float)Math.PI / (entityIn.isAggressive() ? 1.5F : 2.25F);
        this.right_arm.rotateAngleX = f2;
        this.left_arm.rotateAngleX = f2;
        this.right_arm.rotateAngleX += f * 1.2F - f1 * 0.4F;
        this.left_arm.rotateAngleX += f * 1.2F - f1 * 0.4F;
        this.right_arm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.left_arm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.right_arm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.left_arm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(RendererModel RendererModel, float x, float y, float z) {
        RendererModel.rotateAngleX = x;
        RendererModel.rotateAngleY = y;
        RendererModel.rotateAngleZ = z;
    }
}
