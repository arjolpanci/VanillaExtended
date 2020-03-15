package teabx.vanillaextended.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.entities.AssassinOffer;
import teabx.vanillaextended.entities.WanderingAssassin;

import java.io.*;
import java.util.ArrayList;
import java.util.function.Supplier;

public class UpdateAssassinOfferList {

    private ArrayList<AssassinOffer> offerList;
    private WanderingAssassin wanderingAssassin;

    public UpdateAssassinOfferList(PacketBuffer packetBuffer){
        byte[] data = packetBuffer.readByteArray();
        if(data != null){
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                this.offerList = (ArrayList<AssassinOffer>) ois.readObject();
                ois.close(); bais.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public UpdateAssassinOfferList(WanderingAssassin wanderingAssassin) {
        this.offerList = wanderingAssassin.getOfferList();
        this.wanderingAssassin = wanderingAssassin;
    }

    void encode(PacketBuffer packetBuffer) {
        byte[] data;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(offerList);
            data = baos.toByteArray();
            packetBuffer.writeByteArray(data);
            oos.close(); baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void handle(final UpdateAssassinOfferList uao, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            uao.wanderingAssassin.setOfferList(uao.offerList);
        });
    }

}
