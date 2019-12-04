package teabx.vanillaextended.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import teabx.vanillaextended.capabilities.CapabilityRegistry;
import teabx.vanillaextended.capabilities.interfaces.ICoolDownItem;
import teabx.vanillaextended.items.LordStaff;

import java.util.function.Supplier;

public class CoolDownPacket{

    private final int data;

    public CoolDownPacket(PacketBuffer pb) {
        this.data = pb.readInt();
    }

    public CoolDownPacket(int data) {
        this.data = data;
    }

    public void encode(PacketBuffer buf) {
        buf.writeInt(data);
    }

    public static CoolDownPacket decode(PacketBuffer buf) {
        int data = buf.readInt();
        return new CoolDownPacket(data);
    }

    public static void handle(CoolDownPacket cdpkt, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if(player!= null){
                ItemStack stack = player.getHeldItemMainhand();
                if(stack != null){
                    ICoolDownItem cap = stack.getCapability(CapabilityRegistry.COOLDOWN_ITEM, null).orElse(null);
                    cap.setCooldown(cdpkt.data);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
