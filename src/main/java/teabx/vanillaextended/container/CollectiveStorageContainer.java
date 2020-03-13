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
    private ArrayList<Slot> invSlots;

    public CollectiveStorageContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(BlockList.CSContainerType, id);
        this.tile = (CSTile) world.getTileEntity(pos);

        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++){
                this.addSlot(new Slot(playerInventory, j+i*9+9, 9 + j*18, 142 + i*18));
            }
        }
        for(int i=0; i<9; i++){
            this.addSlot(new Slot(playerInventory, i, 9 + i*18, 202));
        }

        int cnt = 36;
        invSlots = new ArrayList<>(tile.getSb().getInvSlots());

        for(Slot s : invSlots){
            this.addSlot(s);
        }

        for(int i=0; i<6; i++){
            for(int j=0; j<9; j++){
                if(cnt >= inventorySlots.size()) return;
                Slot slot = inventorySlots.get(cnt++);
                slot.xPos = 9 + j * 18;
                slot.yPos = 18 + i * 18;
            }
        }
    }

    public void updateSlots(int offset){
        if(offset <= 0) offset = 0;
        int startingIndex = ((offset)*9) + 36;

        for(int i=36; i<inventorySlots.size(); i++){
            Slot s = inventorySlots.get(i);
            s.xPos = -2000;
            s.yPos = -2000;
        }

        for(int i=0; i<6; i++){
            for(int j=0; j<9; j++){
                if(startingIndex >= inventorySlots.size()) return;
                Slot slot = inventorySlots.get(startingIndex++);
                slot.xPos = 9 + j * 18;
                slot.yPos = 18 + i * 18;
            }
        }

    }

    public int getInvSize(){
        return tile.getSb().getInventorySize();
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
