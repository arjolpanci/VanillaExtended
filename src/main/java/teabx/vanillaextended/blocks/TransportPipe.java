package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.tileentities.TPTile;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TransportPipe extends Block{

    public TransportPipe(Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TPTile tile = (TPTile) worldIn.getTileEntity(pos);
        if(tile != null){
            tile.replaceSb(null);
            ArrayList<TileEntity> tiles = tile.getConnectedTiles();
            for(TileEntity te : tiles){
                if(te instanceof IStorageBlockPart){
                    if(((IStorageBlockPart) te).getSb() != null) tile.setSb(((IStorageBlockPart) te).getSb());
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
