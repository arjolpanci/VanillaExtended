package teabx.vanillaextended.network;

import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    public static int index = 1;
    public static SimpleChannel INSTANCE;

    public static void registerMessages(){
        INSTANCE.registerMessage(index++, UpdateClientOfferList.class, UpdateClientOfferList::encode, UpdateClientOfferList::new, UpdateClientOfferList::handle);
        INSTANCE.registerMessage(index++, UpdateTradeSelection.class, UpdateTradeSelection::encode, UpdateTradeSelection::new, UpdateTradeSelection::handle);
        INSTANCE.registerMessage(index++, UpdateServerOfferList.class, UpdateServerOfferList::encode, UpdateServerOfferList::new, UpdateServerOfferList::handle);
        INSTANCE.registerMessage(index++, ValidateTradeInput.class, ValidateTradeInput::encode, ValidateTradeInput::new, ValidateTradeInput::handle);
        INSTANCE.registerMessage(index++, ProcessTradeRequest.class, ProcessTradeRequest::encode, ProcessTradeRequest::new, ProcessTradeRequest::handle);
    }

}
