package teabx.vanillaextended.blocks;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import teabx.vanillaextended.tileentities.CSTile;
import teabx.vanillaextended.tileentities.TPTile;

import java.util.ArrayList;

public class StorageBlock {
    private ArrayList<TileEntity> blocks;

    public StorageBlock(ArrayList<TileEntity> blocks) {
        this.blocks = blocks;
    }

    public StorageBlock(CSTile master){
        blocks = new ArrayList<>();
        blocks.add(master);
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
            if (te instanceof IInventory) chests.add((IInventory) te);
        }
        return chests;
    }

    public void addBlocks(ArrayList<TileEntity> blocks) {
        for (TileEntity te : blocks) {
            this.blocks.add(te);
        }
    }
}
