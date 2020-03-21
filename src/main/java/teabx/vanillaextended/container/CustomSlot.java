package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.network.ProcessTradeRequest;
import teabx.vanillaextended.network.ValidateTradeInput;

public class CustomSlot extends Slot {

    private boolean canTakeStack;
    private boolean isTradeSlot;

    public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canTakeStack = true;
    }

    public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean canTakeStack, boolean isTradeSlot) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canTakeStack = canTakeStack;
        this.isTradeSlot = isTradeSlot;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        if(isTradeSlot){
            if(thePlayer.world.isRemote()){
                PacketHandler.INSTANCE.sendToServer(new ProcessTradeRequest(stack));
            }
        }
        return super.onTake(thePlayer, stack);
    }

    @Override
    public void onSlotChanged() {
        if(this.canTakeStack && !isTradeSlot) PacketHandler.INSTANCE.sendToServer(new ValidateTradeInput(this.getStack()));
        super.onSlotChanged();
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return canTakeStack ? super.canTakeStack(playerIn) : false;
    }
}
