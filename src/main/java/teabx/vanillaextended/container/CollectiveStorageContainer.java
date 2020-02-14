package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.tileentities.CSTile;


public class CollectiveStorageContainer extends Container {

    private TileEntity tile;

    public CollectiveStorageContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory) {
        super(BlockList.CSContainerType, id);
        this.tile = world.getTileEntity(pos);
        CSTile cs = (CSTile) tile;

        IInventory chest = cs.getSb().getChests().get(0);
        int index = 0;
        for(int i=0; i<6; i++) {
            for (int j = 0; j < 9; j++) {
                if(true){
                    this.addSlot(new Slot(chest, index++, 8 + j*18, 18 + i*18));
                }else{
                    this.addSlot(new Slot(cs.getSb().getChests().get(1), index++-27, 8 + j*18, 18 + i*18));
                }
            }
        }

        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++){
                this.addSlot(new Slot(playerInventory, i*j*9+9, 198 + j*18, 28 + i*18));
            }
        }

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tile.getWorld(), tile.getPos()), playerIn, BlockList.collectiveStorage);
    }
}
