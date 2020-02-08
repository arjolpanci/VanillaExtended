package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import teabx.vanillaextended.tileentities.CSTile;
import teabx.vanillaextended.tileentities.TPTile;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TransportPipe extends Block{

    public TransportPipe(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        /*TPTile tile = (TPTile) worldIn.getTileEntity(pos);
        if(tile != null){
            ArrayList<Block> blist = tile.getConnectedBloks();
            if(!worldIn.isRemote){
                for(Block b : blist){
                    System.out.println(b);
                }
            }
            System.out.println(blist.size());
        }*/
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TPTile tile = (TPTile) worldIn.getTileEntity(pos);
        if(tile != null){
            ArrayList<TileEntity> tiles = tile.getConnectedTiles();
            for(TileEntity te : tiles){
                if(te instanceof TPTile){
                    if(((TPTile) te).getSb() != null){
                        ((TPTile) te).getSb().addBlocks(tiles);
                        tile.setSb(((TPTile) te).getSb());
                    }
                }
            }
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TPTile();
    }
}
