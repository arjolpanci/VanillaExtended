package teabx.vanillaextended.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.container.WanderingAssassinContainer;

import java.util.function.Supplier;

public class ValidateTradeInput {

    private ItemStack slotContents;

    public ValidateTradeInput(PacketBuffer packetBuffer){
        this.slotContents = packetBuffer.readItemStack();
    }

    public ValidateTradeInput(ItemStack slotContents) {
        this.slotContents = slotContents;
    }

    void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeItemStack(this.slotContents);
    }

    static void handle(final ValidateTradeInput usasc, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(context.get().getSender().openContainer instanceof WanderingAssassinContainer){
                WanderingAssassinContainer wac = (WanderingAssassinContainer) context.get().getSender().openContainer;
                if(wac != null && wac.currentOffer != null){
                    if(usasc.isRequestValid(context.get().getSender(), wac)){
                        wac.inventorySlots.get(37).putStack(wac.currentOffer.getItem());
                        wac.detectAndSendChanges();
                    }else{
                        wac.inventorySlots.get(37).putStack(ItemStack.EMPTY);
                        wac.detectAndSendChanges();
                    }
                }
                context.get().setPacketHandled(true);
            }
        });
    }

    private boolean isRequestValid(ServerPlayerEntity player, WanderingAssassinContainer wac){
        if(!wac.canInteractWith(player)) return false;
        for(int i=0; i<wac.offerList.size(); i++){
            if(wac.offerList.get(i) == wac.currentOffer){
                if(!wac.offerList.get(i).getAvailable()) return false;
            }
        }
        if(this.slotContents != null){
            if(this.slotContents.getItem() == Items.GOLD_INGOT && (this.slotContents.getCount() >= wac.currentOffer.getPrice())) return true;
        }else{
            return false;
        }
        return false;
    }

}
