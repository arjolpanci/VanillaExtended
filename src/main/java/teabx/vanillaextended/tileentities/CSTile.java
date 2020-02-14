package teabx.vanillaextended.tileentities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.blocks.StorageBlock;
import teabx.vanillaextended.container.CollectiveStorageContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CSTile extends TileEntity implements IStorageBlockPart, INamedContainerProvider {

    private ArrayList<TileEntity> connectedTiles = new ArrayList<>();
    private StorageBlock sb;

    public CSTile() {
        super(BlockList.CSTileType);
    }

    public ArrayList<TileEntity> getConnectedTiles(){
        update();
        return connectedTiles;
    }

    public void update(){
        connectedTiles.clear();
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


    @Override
    public StorageBlock getSb() {
        return sb;
    }

    @Override
    public void setSb(StorageBlock sb) {
        this.sb = sb;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Collective Storage");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CollectiveStorageContainer(id, world, pos, playerInventory);
    }
}
