package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.fml.network.PacketDistributor;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.network.ProcessTradeRequest;
import teabx.vanillaextended.network.UpdateClientOfferList;
import teabx.vanillaextended.network.ValidateTradeInput;

public class CustomSlot extends Slot {

    private boolean canTakeStack;
    private boolean isTradeSlot;
    private WanderingAssassinContainer wanderingAssassinContainer;

    public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean canTakeStack, boolean isTradeSlot, WanderingAssassinContainer wanderingAssassinContainer) {
        super(inventoryIn, index, xPosition, yPosition);
        this.canTakeStack = canTakeStack;
        this.isTradeSlot = isTradeSlot;
        this.wanderingAssassinContainer = wanderingAssassinContainer;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        if(wanderingAssassinContainer.playerEntity != null){
            if(isTradeSlot && !wanderingAssassinContainer.playerEntity.world.isRemote()){
                wanderingAssassinContainer.processTradeRequest();
            }
        }
        //if(thePlayer.world.isRemote()){
        //    PacketHandler.INSTANCE.sendToServer(new ProcessTradeRequest(stack));
        //}
        return super.onTake(thePlayer, stack);
    }

    @Override
    public void onSlotChanged() {
        if(wanderingAssassinContainer.playerEntity != null){
            if(!wanderingAssassinContainer.playerEntity.world.isRemote() && canTakeStack && !isTradeSlot){
                wanderingAssassinContainer.validateTradeInput(this.getStack());
            }
        }
        //if(this.canTakeStack && !isTradeSlot){
        //    PacketHandler.INSTANCE.sendToServer(new ValidateTradeInput(this.getStack()));
        //}
        super.onSlotChanged();
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return canTakeStack ? super.canTakeStack(playerIn) : false;
    }
}
