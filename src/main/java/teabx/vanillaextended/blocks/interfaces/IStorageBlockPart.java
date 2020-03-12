package teabx.vanillaextended.blocks.interfaces;

import net.minecraft.inventory.IInventory;
import teabx.vanillaextended.blocks.StorageBlock;

import java.util.ArrayList;

public interface IStorageBlockPart {
    StorageBlock getSb();
    void setSb(StorageBlock sb);
    void updateStorageBlock(StorageBlock sb);
    ArrayList<IInventory> getConnectedInventories();
}
