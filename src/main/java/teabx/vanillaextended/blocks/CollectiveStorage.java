package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teabx.vanillaextended.tileentities.CSTile;
import teabx.vanillaextended.tileentities.TPTile;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CollectiveStorage extends Block {

    public CollectiveStorage(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        CSTile tile = (CSTile) worldIn.getTileEntity(pos);
        tile.setSb(new StorageBlock(tile));
        ArrayList<TileEntity> connectedTiles = tile.getConnectedTiles();
        for(int i=0; i<connectedTiles.size(); i++){
            if(connectedTiles.get(i) instanceof TPTile){
                ((TPTile) connectedTiles.get(i)).setSb(tile.getSb());
            }
        }
        tile.getSb().update();
        for(IInventory s : tile.getSb().getChests()){
            if(!worldIn.isRemote){
                System.out.println(s);
                System.out.println("Size: " + tile.getSb().getTiles().size());
            }
        }
        for(ItemStack stack : tile.getSb().getItems()){
            if(!worldIn.isRemote){
                System.out.println(stack);
            }
        }
        System.out.println("Size: " + tile.getSb().getTiles());
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CSTile();
    }
}
