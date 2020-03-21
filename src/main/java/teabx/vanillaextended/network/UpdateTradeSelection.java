package teabx.vanillaextended.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.container.WanderingAssassinContainer;

import java.util.function.Supplier;

public class UpdateTradeSelection {

    private int index;

    public UpdateTradeSelection(PacketBuffer packetBuffer){
        this.index = packetBuffer.readInt();
    }

    public UpdateTradeSelection(WanderingAssassinContainer wanderingAssassinContainer, int index) {
        this.index = index;
    }

    void encode(PacketBuffer packetBuffer) {
        packetBuffer.writeInt(this.index);
    }

    static void handle(final UpdateTradeSelection usai, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(context.get().getSender().openContainer instanceof WanderingAssassinContainer){
                WanderingAssassinContainer wac = (WanderingAssassinContainer) context.get().getSender().openContainer;
                if(usai.index != -1){
                    wac.inventorySlots.get(wac.inventorySlots.size()-1).putStack(wac.offerList.get(usai.index).getItem());
                    wac.currentOffer = wac.offerList.get(usai.index);
                }else{
                    wac.inventorySlots.get(wac.inventorySlots.size()-1).putStack(ItemStack.EMPTY);
                    wac.inventorySlots.get(37).putStack(ItemStack.EMPTY);
                }
                context.get().setPacketHandled(true);
            }
        });
    }

}
