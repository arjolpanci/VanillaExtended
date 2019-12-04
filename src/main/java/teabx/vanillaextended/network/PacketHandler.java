package teabx.vanillaextended.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import teabx.vanillaextended.main.VanillaExtended;

import java.util.function.Supplier;

public class PacketHandler {

    public static int index = 1;

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(VanillaExtended.modid, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages(){
        INSTANCE.registerMessage(index++, CoolDownPacket.class, CoolDownPacket::encode,
                CoolDownPacket::decode, CoolDownPacket::handle);
    }

    public static void sendMessage(CoolDownPacket cdpkt){
        INSTANCE.send(PacketDistributor.ALL.noArg(), cdpkt);
    }

}
