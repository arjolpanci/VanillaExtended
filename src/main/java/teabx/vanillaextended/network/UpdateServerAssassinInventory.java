package teabx.vanillaextended.network;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.container.WanderingAssassinContainer;

import java.util.function.Supplier;

public class UpdateServerAssassinInventory {

    private Inventory inventory = new Inventory(39);

    public UpdateServerAssassinInventory(PacketBuffer packetBuffer){
        for(int i=0; i<39; i++){
            ItemStack stack = packetBuffer.readItemStack();
            inventory.setInventorySlotContents(i, stack);
        }
    }

    public UpdateServerAssassinInventory(WanderingAssassinContainer wanderingAssassinContainer) {
        for(int i=0; i<wanderingAssassinContainer.inventorySlots.size(); i++){
            this.inventory.setInventorySlotContents(i, wanderingAssassinContainer.inventorySlots.get(i).getStack());
        }
    }

    void encode(PacketBuffer packetBuffer) {
        for(int i=0; i<inventory.getSizeInventory(); i++){
            packetBuffer.writeItemStack(inventory.getStackInSlot(i));
        }
    }

    static void handle(final UpdateServerAssassinInventory usai, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(context.get().getSender().openContainer instanceof WanderingAssassinContainer){
                WanderingAssassinContainer wac = (WanderingAssassinContainer) context.get().getSender().openContainer;
                for(int i=0; i<38; i++){
                    wac.inventorySlots.get(i).putStack(usai.inventory.getStackInSlot(i));
                }
            }
        });
    }

}
