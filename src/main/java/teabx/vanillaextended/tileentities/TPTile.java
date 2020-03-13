package teabx.vanillaextended.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.blocks.StorageBlock;

import java.io.Serializable;
import java.util.ArrayList;

public class TPTile extends TileEntity implements IStorageBlockPart {

    private ArrayList<TileEntity> connectedTiles = new ArrayList<>();
    private StorageBlock sb;

    public TPTile() {
        super(BlockList.TPTileType);
    }

    public ArrayList<TileEntity> getConnectedTiles(){
        update();
        return connectedTiles;
    }

    @Override
    public ArrayList<IInventory> getConnectedInventories(){
        ArrayList<IInventory> inventories = new ArrayList<>();
        for(TileEntity te : getConnectedTiles()){
            Block b = te.getBlockState().getBlock();
            if(te instanceof IInventory){
                if(b instanceof ChestBlock){
                    inventories.add(ChestBlock.getInventory(te.getBlockState(), te.getWorld(), te.getPos(), false));
                }else{
                    inventories.add((IInventory) te);
                }
            }
        }
        return inventories;
    }

    @Override
    public void updateStorageBlock(StorageBlock sb){
        this.setSb(sb);
        for(TileEntity te : getConnectedTiles()){
            if(te instanceof IStorageBlockPart){
                if(((IStorageBlockPart) te).getSb() != null) continue;
                ((IStorageBlockPart) te).setSb(sb);
                ((IStorageBlockPart) te).updateStorageBlock(sb);
            }
        }
    }

    public void update(){
        connectedTiles.clear();
        for(int i=0; i<3; i++){
            for(int j=-1; j<=1; j++){
                if(j==0) continue;
                TileEntity tile = this.world.getTileEntity(getPosFromIndex(i, j));
                if(tile != null){
                    if(tile instanceof IStorageBlockPart || tile instanceof IInventory){
                        connectedTiles.add(tile);
                    }
                }
            }
        }
    }

    private BlockPos getPosFromIndex(int idx, int offset){
        BlockPos pos = null;
        switch (idx){
            case 0:
                pos = new BlockPos(this.pos.getX() + offset, this.pos.getY(), this.pos.getZ());
                break;
            case 1:
                pos = new BlockPos(this.pos.getX(), this.pos.getY() + offset, this.pos.getZ());
                break;
            case 2:
                pos = new BlockPos(this.pos.getX(), this.pos.getY(), this.pos.getZ() + offset);
                break;
        }
        return pos;
    }

    @Override
    public void setSb(StorageBlock sb) {
        sb.add(this);
        this.sb = sb;
    }

    @Override
    public StorageBlock getSb() {
        return sb;
    }
}
