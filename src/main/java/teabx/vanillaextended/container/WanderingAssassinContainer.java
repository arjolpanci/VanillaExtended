package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teabx.vanillaextended.entities.AssassinOffer;
import teabx.vanillaextended.entities.WanderingAssassin;

import java.util.ArrayList;


public class WanderingAssassinContainer extends Container {

    public WanderingAssassin wanderingAssassin;
    public ArrayList<AssassinOffer> offerList;
    public AssassinOffer currentOffer;
    public boolean tradeConfirmed = false;
    private int entityID;

    public WanderingAssassinContainer(int id, PlayerInventory playerInventory, World world, BlockPos pos) {
        super(ContainerTypes.wanderingAssassinContainerType, id);
        this.entityID = pos.getX();
        this.wanderingAssassin = (WanderingAssassin) world.getEntityByID(pos.getX());
        this.offerList = wanderingAssassin.getOfferList();

        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++){
                this.addSlot(new Slot(playerInventory, j+i*9+9, 108 + j*18, 84 + i*18));
            }
        }
        for(int i=0; i<9; i++){
            this.addSlot(new Slot(playerInventory, i, 108 + i*18, 142));
        }

        this.addSlot(new CustomSlot(this.wanderingAssassin.getInventory(), 0, 153, 41));
        this.addSlot(new CustomSlot(this.wanderingAssassin.getInventory(), 1, 201, 41, true, true));
        this.addSlot(new CustomSlot(this.wanderingAssassin.getInventory(), 2, 112, 22, false, false));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        if(wanderingAssassin == null) return false;
        return this.wanderingAssassin.getCustomer() == playerIn;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 45) {
                if (!this.mergeItemStack(itemstack1, 45, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
