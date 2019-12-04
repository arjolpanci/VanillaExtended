package teabx.vanillaextended.entities;

import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import teabx.vanillaextended.entities.goals.CustomBowAttackGoal;
import teabx.vanillaextended.items.ItemList;
import teabx.vanillaextended.items.KingBow;

import javax.annotation.Nullable;

public class SkeletonKing extends MonsterEntity implements IRangedAttackMob {

    protected SkeletonKing(EntityType<? extends MonsterEntity> type, World world) {
        super((EntityType<? extends MonsterEntity>) EntityRegistry.SKELETON_KING, world);
    }

    private final ServerBossInfo bossInfo = (ServerBossInfo)(new ServerBossInfo(this.getDisplayName(),
            BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private final CustomBowAttackGoal arrowAttack = new CustomBowAttackGoal(this, 1.0D, 25, 20.0F);

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(4, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.3F);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ItemList.king_bow));
        this.setActiveHand(Hand.MAIN_HAND);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.goalSelector.addGoal(2, this.arrowAttack);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.findAmmo(new ItemStack(ItemList.king_bow));
        for(int i=0; i<3; i++){
            AbstractArrowEntity arrow = this.gen_arrow(itemstack, distanceFactor);
            arrow = ((KingBow)this.getHeldItemMainhand().getItem()).customeArrow(arrow);

            double d0 = target.posX - this.posX;
            double d1 = target.getBoundingBox().minY + (double)(target.getHeight() / 3.0F) - arrow.posY;
            double d2 = target.posZ - this.posZ;
            double d3 = (double) MathHelper.sqrt((d0*d0 + d2*d2));

            if(i!=2){
                arrow.shoot(d0 + Math.pow(2.0D, i), d1 + d3 * (double)0.2F, d2 , 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
            }else{
                arrow.shoot(d0, d1 + d3 * (double)0.2F, d2 + 2.0F, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
            }
            this.world.addEntity(arrow);
        }

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F/(this.getRNG().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if(this.getHealth() / this.getMaxHealth() < 0.5){
            this.arrowAttack.setAttackCooldown(8);
            this.goalSelector.addGoal(2, this.arrowAttack);
        }

        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());

    }

    protected AbstractArrowEntity gen_arrow(ItemStack stack, float distanceFactor) {
        return ProjectileHelper.func_221272_a(this, stack, distanceFactor);
    }

    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    public boolean isNonBoss() { return false; }

    @Override
    public boolean isEntityUndead() { return true; }

    @Override
    public double getYOffset() { return -0.6D; }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return super.getSize(poseIn).scale(1.5F);
    }

    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) { return 2.64F; }

}
