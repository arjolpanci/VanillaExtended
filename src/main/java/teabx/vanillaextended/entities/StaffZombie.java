package teabx.vanillaextended.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookAtEntityTask;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import teabx.vanillaextended.entities.goals.CustomAttackGoal;
import teabx.vanillaextended.entities.goals.CustomBowAttackGoal;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class StaffZombie extends ZombieEntity {

    private PlayerEntity owner;


    public StaffZombie(World worldIn, PlayerEntity player) {
        super(worldIn);
        this.owner = player;
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name.appendText("Staff Zombie"));
    }

    public StaffZombie(EntityType<Entity> entityEntityType, World world) {
        super(world);
    }

    @Override
    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new CustomAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setCallsForHelp(ZombiePigmanEntity.class));
    }

    @Override
    public void tick() {
        super.tick();
        if(owner.getLastAttackedEntity() != null) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, owner.getLastAttackedEntity().getClass(), true));
        }
    }
}
