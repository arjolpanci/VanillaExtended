package teabx.vanillaextended.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import teabx.vanillaextended.capabilities.interfaces.ICoolDownItem;
import teabx.vanillaextended.capabilities.interfaces.IRank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerRank implements IRank, ICapabilitySerializable<INBT> {

    private int rank = 5;

    private LazyOptional<IRank> instance = LazyOptional.of(CapabilityRegistry.PLAYER_RANK::getDefaultInstance);

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public void setRank(int rank){
        this.rank = rank;
    }

    @Override
    public void rankUp() {
        rank++;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
       return cap == CapabilityRegistry.PLAYER_RANK ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return CapabilityRegistry.PLAYER_RANK.getStorage().writeNBT(CapabilityRegistry.PLAYER_RANK,
                this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional cannot be empty")),null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityRegistry.PLAYER_RANK.getStorage().readNBT(CapabilityRegistry.PLAYER_RANK,
                this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional cannot be empty")),null, nbt);
    }
}
