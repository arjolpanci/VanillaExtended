package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import teabx.vanillaextended.container.CollectiveStorageContainer;

public class BlockList {
    public static Block collectiveStorage;
    public static Block transportPipe;

    //TileEntity Types
    public static TileEntityType<?> CSTileType;
    public static TileEntityType<?> TPTileType;

    //Container Types
    public static ContainerType<CollectiveStorageContainer> CSContainerType;
}
