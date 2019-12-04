package teabx.vanillaextended.entities.goals;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import teabx.vanillaextended.entities.StaffZombie;

import java.util.EnumSet;

public class CustomAttackGoal extends MeleeAttackGoal {

    protected final CreatureEntity attacker;
    protected int attackTick;
    private final double speedTowardsTarget;
    private final boolean longMemory;
    private Path path;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    protected final int attackInterval = 20;
    private long field_220720_k;
    private int failedPathFindingPenalty = 0;
    private boolean canPenalize = false;


    public CustomAttackGoal(CreatureEntity creature, double speedIn, boolean useLongMemory) {
        super(creature, speedIn, useLongMemory);
        this.attacker = creature;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        long i = this.attacker.world.getGameTime();
        if (i - this.field_220720_k < 20L) {
            return false;
        } else {
            this.field_220720_k = i;
            LivingEntity livingentity = this.attacker.getAttackTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if(livingentity instanceof StaffZombie || livingentity instanceof PlayerEntity){
                return false;
            }else{
                if (canPenalize) {
                    if (--this.delayCounter <= 0) {
                        this.path = this.attacker.getNavigator().getPathToEntityLiving(livingentity, 0);
                        this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
                        return this.path != null;
                    } else {
                        return true;
                    }
                }
                this.path = this.attacker.getNavigator().getPathToEntityLiving(livingentity, 0);
                if (this.path != null) {
                    return true;
                } else {
                    return this.getAttackReachSqr(livingentity) >= this.attacker.getDistanceSq(livingentity.posX, livingentity.getBoundingBox().minY, livingentity.posZ);
                }
            }
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        LivingEntity livingentity = this.attacker.getAttackTarget();
        if (livingentity == null) {
            return false;
        } else if (!livingentity.isAlive()) {
            return false;
        } else if (!this.longMemory) {
            return !this.attacker.getNavigator().noPath();
        } else if (!this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(livingentity))) {
            return false;
        } else {
            return !(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity)livingentity).isCreative() || !(livingentity instanceof StaffZombie);
        }
    }
}
