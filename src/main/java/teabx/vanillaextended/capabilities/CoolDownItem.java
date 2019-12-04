package teabx.vanillaextended.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import teabx.vanillaextended.capabilities.interfaces.ICoolDownItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CoolDownItem implements ICoolDownItem, ICapabilitySerializable<INBT> {

    private int maxCooldown = 3;
    private int cooldown=0;
    private int timer=0;

    private LazyOptional<ICoolDownItem> instance = LazyOptional.of(CapabilityRegistry.COOLDOWN_ITEM::getDefaultInstance);

    @Override
    public boolean isOffCooldown() {
        return getCooldown() == 0 ? true : false;
    }

    @Override
    public void updateCooldown() {
        if(cooldown != 0){
            if(timer %20 == 0){
                cooldown--;
                timer++;
            }else{
                timer++;
            }
        }
        if(timer == maxCooldown*20){
            timer = 0;
            return;
        }
    }

    @Override
    public void setCooldown(int cooldown){
        this.cooldown = cooldown;
    }

    @Override
    public int getCooldown(){
        return this.cooldown;
    }

    @Override
    public int getMaxCooldown() { return this.maxCooldown; }

    @Override
    public INBT serializeNBT() {
        return CapabilityRegistry.COOLDOWN_ITEM.getStorage().writeNBT(CapabilityRegistry.COOLDOWN_ITEM,
                this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional cannot be empty")),null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        CapabilityRegistry.COOLDOWN_ITEM.getStorage().readNBT(CapabilityRegistry.COOLDOWN_ITEM,
                this.instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional cannot be empty")),null, nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityRegistry.COOLDOWN_ITEM ? instance.cast() : LazyOptional.empty();
    }
}
