package teabx.vanillaextended.entities.goals;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import teabx.vanillaextended.entities.SkeletonKing;
import teabx.vanillaextended.items.ItemList;
import teabx.vanillaextended.items.KingBow;

import java.util.EnumSet;

public class CustomBowAttackGoal extends RangedBowAttackGoal<SkeletonKing> {

    private int attackCooldown;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private final SkeletonKing entity;
    private final double moveSpeedAmp;
    private final float maxAttackDistance;

    public CustomBowAttackGoal(SkeletonKing mob, double moveSpeedAmpIn, int attackCooldownIn, float maxAttackDistanceIn) {
        super(mob, moveSpeedAmpIn, attackCooldownIn, maxAttackDistanceIn);
        this.entity = mob;
        this.moveSpeedAmp = moveSpeedAmpIn;
        this.attackCooldown = attackCooldownIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() == null ? false : this.isBowInMainhand();
    }

    @Override
    protected boolean isBowInMainhand() {
        ItemStack main = this.entity.getHeldItemMainhand();
        ItemStack off  = this.entity.getHeldItemOffhand();
        return main.getItem() instanceof KingBow || off.getItem() instanceof KingBow;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.entity.getAttackTarget();
        if (livingentity != null) {
            double d0 = this.entity.getDistanceSq(livingentity.posX, livingentity.getBoundingBox().minY, livingentity.posZ);
            boolean flag = this.entity.getEntitySenses().canSee(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.maxAttackDistance) && this.seeTime >= 20) {
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.entity.getNavigator().tryMoveToEntityLiving(livingentity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(livingentity, 30.0F, 30.0F);
            } else {
                this.entity.getLookController().setLookPositionWithEntity(livingentity, 30.0F, 30.0F);
            }

            if (this.entity.isHandActive()) {
                if (!flag && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                }else if (flag) {
                    int i = this.entity.getItemInUseMaxCount();
                    if (i >= 0) {
                        this.entity.resetActiveHand();
                        ((IRangedAttackMob)this.entity).attackEntityWithRangedAttack(livingentity, KingBow.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.entity.setActiveHand(ProjectileHelper.getHandWith(this.entity, ItemList.kingBow));
            }

        }
    }
}
