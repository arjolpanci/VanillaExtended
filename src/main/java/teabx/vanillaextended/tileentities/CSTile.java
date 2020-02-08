package teabx.vanillaextended.tileentities;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import teabx.vanillaextended.blocks.StorageBlock;
import java.util.ArrayList;
import static teabx.vanillaextended.blocks.BlockList.CSTileType;

public class CSTile extends TileEntity {

    private ArrayList<TileEntity> connectedTiles = new ArrayList<>();
    private StorageBlock sb;

    public CSTile() {
        super(CSTileType);
    }

    public ArrayList<TileEntity> getConnectedTiles(){
        update();
        return connectedTiles;
    }

    public void update(){
        connectedTiles.clear();
        connectedTiles.add(this);
        for(int i=0; i<3; i++){
            for(int j=-1; j<=1; j++){
                if(j==0) continue;
                TileEntity tile = this.world.getTileEntity(getPosFromIndex(i, j));
                if(tile != null){
                    if(tile instanceof TPTile || tile instanceof CSTile || tile instanceof IInventory){
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

    public void setSb(StorageBlock sb) {
        this.sb = sb;
    }

    public StorageBlock getSb() {
        return sb;
    }
}
