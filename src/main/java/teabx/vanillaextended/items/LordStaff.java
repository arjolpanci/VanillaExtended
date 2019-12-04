package teabx.vanillaextended.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import teabx.vanillaextended.capabilities.CapabilityRegistry;
import teabx.vanillaextended.capabilities.interfaces.ICoolDownItem;
import teabx.vanillaextended.entities.StaffZombie;
import teabx.vanillaextended.network.CoolDownPacket;
import teabx.vanillaextended.network.PacketHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class LordStaff extends net.minecraft.item.Item {

    public LordStaff(Properties properties) { super(properties); }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        ICoolDownItem cap = stack.getCapability(CapabilityRegistry.COOLDOWN_ITEM, null).orElse(null);
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("cooldown", cap.getCooldown());
        nbt.putInt("maxcooldown", cap.getMaxCooldown());
        return nbt;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        if(nbt != null){
            ICoolDownItem cap = stack.getCapability(CapabilityRegistry.COOLDOWN_ITEM, null).orElse(null);
            cap.setCooldown(nbt.getInt("cooldown"));
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        ICoolDownItem cap = stack.getCapability(CapabilityRegistry.COOLDOWN_ITEM, null).orElse(null);
        double cd = 0;
        if(cap != null){
            cd = cap.getCooldown();
        }
        return 1-(cd/3.0);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
        ICoolDownItem cap = stack.getCapability(CapabilityRegistry.COOLDOWN_ITEM, null).orElse(null);

        if(cap != null){
            cap.updateCooldown();
            PacketHandler.sendMessage(new CoolDownPacket(cap.getCooldown()));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        ICoolDownItem cap = stack.getCapability(CapabilityRegistry.COOLDOWN_ITEM, null).orElse(null);

        System.out.println(cap.getCooldown());

        if(!worldIn.isRemote && cap.isOffCooldown()){
            if(hasHeads(playerIn) || playerIn.abilities.isCreativeMode){
                Random rn = new Random(20);
                StaffZombie zm = new StaffZombie(worldIn, playerIn);
                zm.setPosition(playerIn.posX + rn.nextDouble() * 2, playerIn.posY, playerIn.posZ + rn.nextDouble() * 2);
                zm.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.GOLDEN_HELMET));
                worldIn.addEntity(zm);
                for(int i = 0; i<36; i++){
                    if(playerIn.inventory.getStackInSlot(i).getItem() == Items.ZOMBIE_HEAD && !playerIn.abilities.isCreativeMode){
                        playerIn.inventory.getStackInSlot(i).split(1);
                        break;
                    }
                }
                cap.setCooldown(cap.getMaxCooldown());
                return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return new ActionResult<>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
    }

    private boolean hasHeads(PlayerEntity player) {
        ItemStack head = new ItemStack(Items.ZOMBIE_HEAD);
        if(player.inventory.hasItemStack(head)){
            return true;
        }
        return false;
    }

}
