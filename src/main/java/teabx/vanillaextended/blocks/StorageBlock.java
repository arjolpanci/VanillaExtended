package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import teabx.vanillaextended.tileentities.CSTile;
import teabx.vanillaextended.tileentities.TPTile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StorageBlock {
    private ArrayList<TileEntity> blocks;

    public StorageBlock(ArrayList<TileEntity> blocks) {
        this.blocks = blocks;
    }

    public StorageBlock(CSTile master){
        blocks = new ArrayList<>();
        blocks.add(master);
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
            if(b instanceof ChestBlock){
                chests.add(((ChestBlock)b).getInventory(te.getBlockState(), te.getWorld(), te.getPos(), false));
            }
        }
        return chests;
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
