package teabx.vanillaextended.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import teabx.vanillaextended.container.WanderingAssassinContainer;

import java.util.function.Supplier;

public class ProcessTradeRequest {

    private ItemStack slotContents;
    private int cnt;

    public ProcessTradeRequest(PacketBuffer packetBuffer){
        this.slotContents = packetBuffer.readItemStack();
        this.cnt = slotContents.getCount();
    }

    public ProcessTradeRequest(ItemStack slotContents) {
        this.slotContents = slotContents;
        this.cnt = slotContents.getCount();
    }

    void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeItemStack(this.slotContents);
    }

    static void handle(final ProcessTradeRequest ptr, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(context.get().getSender().openContainer instanceof WanderingAssassinContainer){
                WanderingAssassinContainer wac = (WanderingAssassinContainer) context.get().getSender().openContainer;
                if(wac.currentOffer != null){
                    if(wac.canInteractWith(context.get().getSender()) && (ptr.slotContents.getItem() == wac.currentOffer.getItem().getItem())){
                        if(wac.inventorySlots.get(36).getStack().getItem() == Items.GOLD_INGOT && (wac.inventorySlots.get(36).getStack().getCount() >= wac.currentOffer.getPrice())){
                            for(int i=0; i<wac.offerList.size(); i++){
                                if(wac.offerList.get(i) == wac.currentOffer){
                                    wac.offerList.get(i).setAvailable(false);
                                }
                            }
                            wac.wanderingAssassin.setOfferList(wac.offerList);
                            wac.inventorySlots.get(36).decrStackSize(wac.currentOffer.getPrice());
                            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateClientOfferList(wac.wanderingAssassin));
                            wac.detectAndSendChanges();
                            context.get().setPacketHandled(true);
                        }
                    }
                }
                context.get().setPacketHandled(true);
            }
        });
    }
}
