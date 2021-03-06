package teabx.vanillaextended.blocks;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.tileentities.StorageConnectorTile;
import teabx.vanillaextended.tileentities.StorageControllerTile;

import java.io.Serializable;
import java.util.*;

public class StorageBlock implements Serializable {

    private ArrayList<TileEntity> blocks;
    private LinkedHashSet<Slot> invSlots;

    public StorageBlock(ArrayList<TileEntity> blocks) {
        this.blocks = blocks;
        invSlots = new LinkedHashSet<>();
    }

    public StorageBlock(StorageControllerTile master){
        blocks = new ArrayList<>();
        blocks.add(master);
        invSlots = new LinkedHashSet<>();
    }

    public void add(TileEntity te){
        blocks.add(te);
    }

    public ArrayList<StorageConnectorTile> getPipes() {
        ArrayList<StorageConnectorTile> pipes = new ArrayList<>();
        for (TileEntity te : blocks) {
            if (te instanceof StorageConnectorTile) pipes.add((StorageConnectorTile) te);
        }
        return pipes;
    }

    public ArrayList<IInventory> getInventories() {
        Set<IInventory> inventories = new HashSet<>();
        for(TileEntity te : blocks){
            if(te instanceof IStorageBlockPart){
                inventories.addAll(((IStorageBlockPart) te).getConnectedInventories());
            }
        }

        return new ArrayList<>(inventories);
    }

    public int getInventorySize(){
        return (invSlots.size())/9;
    }

    public int getFreeSlots() {
        update();
        int space = 0;
        for(Slot s : invSlots){
            if(s.getStack().isEmpty()) space++;
        }
        return space;
    }


    public LinkedHashSet<Slot> getInvSlots(){
        update();
        return invSlots;
    }

    public ArrayList<TileEntity> getTiles() {
        return blocks;
    }

    public void update(){
        Set<TileEntity> set = new HashSet<>(blocks);
        blocks.clear();
        blocks.addAll(set);

        ArrayList<Slot> emptySlots = new ArrayList<>();
        invSlots.clear();

        for(IInventory i : getInventories()){
            if(i != null){
                for(int j=0; j<i.getSizeInventory(); j++){
                    Slot slot = new Slot(i, j, -2000, -2000);
                    if(slot.getStack().isEmpty()){
                        emptySlots.add(slot);
                    }else{
                        invSlots.add(slot);
                    }
                }
            }
        }
        invSlots.addAll(emptySlots);
    }

    public void addBlocks(ArrayList<TileEntity> blocks) {
        for (TileEntity te : blocks) {
            this.blocks.add(te);
        }
    }
}
