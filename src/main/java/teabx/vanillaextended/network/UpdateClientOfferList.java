package teabx.vanillaextended.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.entities.AssassinOffer;
import teabx.vanillaextended.entities.WanderingAssassin;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Supplier;

public class UpdateClientOfferList {

    private ArrayList<AssassinOffer> offerList;

    public UpdateClientOfferList(PacketBuffer packetBuffer){
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

    public UpdateClientOfferList(WanderingAssassin wanderingAssassin) {
        this.offerList = wanderingAssassin.getOfferList();
    }

    void encode(PacketBuffer packetBuffer) {
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

    static void handle(final UpdateClientOfferList uao, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if(Minecraft.getInstance().player.openContainer instanceof WanderingAssassinContainer){
                WanderingAssassinContainer was = (WanderingAssassinContainer) Minecraft.getInstance().player.openContainer;
                was.offerList = uao.offerList;
                context.get().setPacketHandled(true);
            }
        });
    }

}
