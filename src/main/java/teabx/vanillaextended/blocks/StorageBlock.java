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
import java.util.Set;

public class StorageBlock {

    private ArrayList<TileEntity> blocks;
    private ArrayList<Slot> slots;

    public StorageBlock(ArrayList<TileEntity> blocks) {
        this.blocks = blocks;
        slots = new ArrayList<>();
    }

    public StorageBlock(CSTile master){
        blocks = new ArrayList<>();
        blocks.add(master);
        slots = new ArrayList<>();
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

    public ArrayList<IInventory> getChests() {
        ArrayList<IInventory> chests = new ArrayList<>();
        for (TileEntity te : blocks) {
            Block b = te.getBlockState().getBlock();
            if(te instanceof  IInventory){
                if(b instanceof ChestBlock){
                    chests.add(((ChestBlock)b).getInventory(te.getBlockState(), te.getWorld(), te.getPos(), false));
                }else{
                    chests.add((IInventory) te);
                }
            }
        }
        return chests;
    }

    public ArrayList<Slot> getSlots(){
        slots.clear();
        ArrayList<Slot> empty = new ArrayList<>();
        for(IInventory i : getChests()){
            for(int j=0; j<i.getSizeInventory(); j++){
                Slot slot = new Slot(i, j, 0, 0);
                if(i.getStackInSlot(j).isEmpty()){
                    empty.add(slot);
                    continue;
                }
                slots.add(slot);
            }
        }
        slots.addAll(empty);
        return slots;
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
    }

    public void addBlocks(ArrayList<TileEntity> blocks) {
        for (TileEntity te : blocks) {
            this.blocks.add(te);
        }
    }
}
