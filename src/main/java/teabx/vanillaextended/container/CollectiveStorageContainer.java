package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.tileentities.CSTile;

import java.util.ArrayList;

public class CollectiveStorageContainer extends Container {

    public CSTile tile;
    private PlayerInventory pi;

    public CollectiveStorageContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(BlockList.CSContainerType, id);
        this.tile = (CSTile) world.getTileEntity(pos);
        this.pi = playerInventory;

        updateSlots(0);
    }

    public void updateSlots(int offset){
        inventorySlots.clear();
        int startingIndex = offset*9;
        ArrayList<Slot> slots = tile.getSb().getSlots();

        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++){
                this.addSlot(new Slot(pi, j+i*9+9, 198 + j*18, 28 + i*18));
            }
        }

        for(int i=0; i<9; i++){
            this.addSlot(new Slot( pi, i, 198 + i*18, 88));
        }

        for(int i=0; i<6; i++) {
            for (int j = 0; j < 9; j++) {
                if(startingIndex > getInvSize() || startingIndex < 0) return;
                Slot slot = slots.get(startingIndex++);
                slot.xPos = 9 + j * 18;
                slot.yPos = 18 + i * 18;
                this.addSlot(slot);
            }
        }
    }

    public int getInvSize(){
        return tile.getSb().getSlots().size() - 1 ;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerIn, BlockList.collectiveStorage);
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