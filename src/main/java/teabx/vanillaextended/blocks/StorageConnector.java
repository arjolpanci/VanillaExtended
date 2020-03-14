package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import teabx.vanillaextended.blocks.interfaces.IStorageBlockPart;
import teabx.vanillaextended.tileentities.StorageConnectorTile;

import javax.annotation.Nullable;

public class StorageConnector extends Block{

    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static VoxelShape shape;

    public StorageConnector(Properties properties) {
        super(properties);
        shape = Block.makeCuboidShape(5,5,5,11,11,11);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, Boolean.valueOf(false)).with(DOWN, Boolean.valueOf(false))
        .with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)));
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @NotNull
    @Override
    public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return shape;
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return shape;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader ibr = context.getWorld();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState up = ibr.getBlockState(pos.up());
        BlockState down = ibr.getBlockState(pos.down());
        BlockState north = ibr.getBlockState(pos.north());
        BlockState east = ibr.getBlockState(pos.east());
        BlockState south = ibr.getBlockState(pos.south());
        BlockState west = ibr.getBlockState(pos.west());
        return super.getStateForPlacement(context).with(UP, Boolean.valueOf(getConnectedState(world, pos.up()))).with(DOWN, Boolean.valueOf(getConnectedState(world, pos.down())))
                .with(NORTH, Boolean.valueOf(getConnectedState(world, pos.north()))).with(EAST, Boolean.valueOf(getConnectedState(world, pos.east())))
                .with(SOUTH, Boolean.valueOf(getConnectedState(world, pos.south()))).with(WEST, Boolean.valueOf(getConnectedState(world, pos.west())));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState up = worldIn.getBlockState(currentPos.up());
        BlockState down = worldIn.getBlockState(currentPos.down());
        BlockState north = worldIn.getBlockState(currentPos.north());
        BlockState east = worldIn.getBlockState(currentPos.east());
        BlockState south = worldIn.getBlockState(currentPos.south());
        BlockState west = worldIn.getBlockState(currentPos.west());
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos).with(UP, Boolean.valueOf(getConnectedState(worldIn, currentPos.up())))
                .with(DOWN, Boolean.valueOf(getConnectedState(worldIn, currentPos.down()))).with(NORTH, Boolean.valueOf(getConnectedState(worldIn, currentPos.north()))).with(EAST, Boolean.valueOf(getConnectedState(worldIn, currentPos.east())))
                .with(SOUTH, Boolean.valueOf(getConnectedState(worldIn, currentPos.south()))).with(WEST, Boolean.valueOf(getConnectedState(worldIn, currentPos.west())));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        StorageConnectorTile tile = (StorageConnectorTile) worldIn.getTileEntity(pos);
        if(tile != null){
            for(TileEntity te : tile.getConnectedTiles()){
                if(te instanceof IStorageBlockPart){
                    StorageBlock sb = ((IStorageBlockPart) te).getSb();
                    if(sb != null) tile.setSb(sb);
                }
            }
        }
    }

    private boolean getConnectedState(IWorld world, BlockPos pos){
        TileEntity te = world.getTileEntity(pos);
        return (te instanceof IStorageBlockPart || te instanceof IInventory) ? true : false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new StorageConnectorTile();
    }
}
