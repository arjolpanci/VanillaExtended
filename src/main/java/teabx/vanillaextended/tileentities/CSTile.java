package teabx.vanillaextended.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.blocks.StorageBlock;
import teabx.vanillaextended.container.CollectiveStorageContainer;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;

public class CSTile extends TileEntity implements IStorageBlockPart, INamedContainerProvider, Serializable {

    private ArrayList<TileEntity> connectedTiles = new ArrayList<>();
    private StorageBlock sb;

    public CSTile() {
        super(BlockList.CSTileType);
    }

    public ArrayList<TileEntity> getConnectedTiles(){
        update();
        return connectedTiles;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        byte[] data = compound.getByteArray("StorageBlock");
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            this.sb = (StorageBlock) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(sb);
            oos.flush();
            byte[] data = bos.toByteArray();
            compound.putByteArray("StorageBlock", data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.write(compound);
    }

    @Override
    public ArrayList<IInventory> getConnectedInventories() {
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
    public void updateStorageBlock(StorageBlock sb) {
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
