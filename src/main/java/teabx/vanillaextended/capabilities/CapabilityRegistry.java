package teabx.vanillaextended.capabilities;

import net.minecraft.command.arguments.NBTTagArgument;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import teabx.vanillaextended.capabilities.interfaces.ICoolDownItem;
import teabx.vanillaextended.capabilities.interfaces.IRank;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CapabilityRegistry {

    @CapabilityInject(ICoolDownItem.class)
    public static Capability<ICoolDownItem> COOLDOWN_ITEM = null;
    @CapabilityInject(IRank.class)
    public static Capability<IRank> PLAYER_RANK = null;

    public static void registerCapabilities(){
        CapabilityManager.INSTANCE.register(ICoolDownItem.class, new CoolDownItemStorage() ,CoolDownItem::new);
        CapabilityManager.INSTANCE.register(IRank.class, new PlayerRankStorage(), PlayerRank::new);
    }

    public static class PlayerRankStorage implements Capability.IStorage<IRank>{

        @Nullable
        @Override
        public INBT writeNBT(Capability<IRank> capability, IRank instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("rank", instance.getRank());
            return nbt;
        }

        @Override
        public void readNBT(Capability<IRank> capability, IRank instance, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            instance.setRank(tag.getInt("rank"));
        }
    }

    public static class CoolDownItemStorage implements Capability.IStorage<ICoolDownItem>{

        @Nullable
        @Override
        public INBT writeNBT(Capability<ICoolDownItem> capability, ICoolDownItem instance, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("cooldown", instance.getCooldown());
            nbt.putInt("maxcooldown", instance.getMaxCooldown());
            return nbt;
        }

        @Override
        public void readNBT(Capability<ICoolDownItem> capability, ICoolDownItem instance, Direction side, INBT nbt) {
            CompoundNBT tag = (CompoundNBT) nbt;
            instance.setCooldown(tag.getInt("cooldown"));
        }
    }


}
