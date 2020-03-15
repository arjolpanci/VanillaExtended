package teabx.vanillaextended.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import teabx.vanillaextended.main.VanillaExtended;

public class PacketHandler {

    public static int index = 1;
    public static SimpleChannel INSTANCE;

    public static void registerMessages(){
        INSTANCE.registerMessage(index++, UpdateAssassinOfferList.class, UpdateAssassinOfferList::encode, UpdateAssassinOfferList::new, UpdateAssassinOfferList::handle);
    }

}
