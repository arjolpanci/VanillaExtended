package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import teabx.vanillaextended.tileentities.CSTile;
import teabx.vanillaextended.tileentities.TPTile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class StorageBlock {

    private ArrayList<TileEntity> blocks;
    private LinkedHashSet<Slot> invSlots;

    public StorageBlock(ArrayList<TileEntity> blocks) {
        this.blocks = blocks;
        invSlots = new LinkedHashSet<>();
    }

    public StorageBlock(CSTile master){
        blocks = new ArrayList<>();
        blocks.add(master);
        invSlots = new LinkedHashSet<>();
    }

    public void add(TileEntity te){
        blocks.add(te);
    }

    public ArrayList<TPTile> getPipes() {
        ArrayList<TPTile> pipes = new ArrayList<>();
        for (TileEntity te : blocks) {
            if (te instanceof TPTile) pipes.add((TPTile) te);
        }
        return pipes;
    }

    public ArrayList<IInventory> getInventories() {
        ArrayList<IInventory> chests = new ArrayList<>();
        for (TileEntity te : blocks) {
            Block b = te.getBlockState().getBlock();
            if(te instanceof IInventory){
                if(b instanceof ChestBlock){
                    chests.add(ChestBlock.getInventory(te.getBlockState(), te.getWorld(), te.getPos(), false));
                }else{
                    chests.add((IInventory) te);
                }
            }
        }
        return chests;
    }

    public int getInventorySize(){
        return (invSlots.size())/9;
    }

    public LinkedHashSet<Slot> getInvSlots(){
        update();
        return invSlots;
    }

    public ArrayList<TileEntity> getTiles() {
        return blocks;
    }

    public void update(){
        for(TPTile tpt : getPipes()){
            tpt.updateStorageBlock();
        }
        Set<TileEntity> set = new HashSet<>(blocks);
        blocks.clear();
        blocks.addAll(set);

        ArrayList<Slot> emptySlots = new ArrayList<>();
        invSlots.clear();

        for(IInventory i : getInventories()){
            for(int j=0; j<i.getSizeInventory(); j++){
                Slot slot = new Slot(i, j, -2000, -2000);
                if(slot.getStack().isEmpty()){
                    emptySlots.add(slot);
                }else{
                    invSlots.add(slot);
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
