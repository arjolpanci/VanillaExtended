package teabx.vanillaextended.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.entities.AssassinOffer;

import java.util.ArrayList;
import java.util.function.Supplier;

public class UpdateServerOfferList {

    private ArrayList<AssassinOffer> offerList;

    public UpdateServerOfferList(ArrayList<AssassinOffer> offerList){
        this.offerList = offerList;
    }

    public UpdateServerOfferList(PacketBuffer packetBuffer){
        ArrayList<AssassinOffer> ao = new ArrayList<>();
        int ln = packetBuffer.readInt();
        for(int i=0; i<ln; i++){
            int price = packetBuffer.readInt();
            int toolIndex = packetBuffer.readInt();
            boolean isAvailable = packetBuffer.readBoolean();
            int enchSize = packetBuffer.readInt();
            ArrayList<Integer> enchData = new ArrayList<>();
            for(int j=0; j<enchSize; j++){
                enchData.add(packetBuffer.readInt());
            }
            AssassinOffer assassinOffer = new AssassinOffer(price, toolIndex, isAvailable, enchData);
            ao.add(assassinOffer);
        }
        this.offerList = ao;
    }

    void encode(PacketBuffer packetBuffer){
        packetBuffer.writeInt(offerList.size());
        for(AssassinOffer offer : offerList){
            packetBuffer.writeInt(offer.getPrice());
            packetBuffer.writeInt(offer.getToolIndex());
            packetBuffer.writeBoolean(offer.getAvailable());
            packetBuffer.writeInt(offer.getEnchantmentData().size());
            for(int i : offer.getEnchantmentData()){
                packetBuffer.writeInt(i);
            }
        }
    }

    static void handle(final UpdateServerOfferList uso, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(context.get().getSender().openContainer instanceof WanderingAssassinContainer){
                WanderingAssassinContainer wac = (WanderingAssassinContainer) context.get().getSender().openContainer;
                wac.wanderingAssassin.setOfferList(uso.offerList);
                context.get().setPacketHandled(true);
            }
        });
    }


}
