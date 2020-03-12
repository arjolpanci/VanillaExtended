package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.tileentities.TPTile;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TransportPipe extends Block{

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;


    public TransportPipe(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, false).with(DOWN, false)
        .with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.UP);
        builder.add(BlockStateProperties.DOWN);
        builder.add(BlockStateProperties.NORTH);
        builder.add(BlockStateProperties.EAST);
        builder.add(BlockStateProperties.SOUTH);
        builder.add(BlockStateProperties.WEST);
        super.fillStateContainer(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockPos up = pos.up();
        BlockPos down = pos.down();
        BlockPos north = pos.north();
        BlockPos east = pos.east();
        BlockPos south = pos.south();
        BlockPos west = pos.west();
        return super.getStateForPlacement(context).with(UP, getConnectedState(world, up)).with(DOWN, getConnectedState(world, down))
                .with(NORTH, getConnectedState(world, north)).with(EAST, getConnectedState(world, east))
                .with(SOUTH, getConnectedState(world, south)).with(WEST, getConnectedState(world, west));
    }

    private boolean getConnectedState(World world, BlockPos pos){
        return (world.getBlockState(pos).getBlock() instanceof TransportPipe) ? true : false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        TPTile tile = (TPTile) worldIn.getTileEntity(pos);
        if(tile != null){
            for(TileEntity te : tile.getConnectedTiles()){
                if(te instanceof IStorageBlockPart){
                    StorageBlock sb = ((IStorageBlockPart) te).getSb();
                    if(sb != null) tile.setSb(sb);
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
