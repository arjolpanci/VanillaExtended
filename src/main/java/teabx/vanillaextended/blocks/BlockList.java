package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import teabx.vanillaextended.container.StorageControllerContainer;

public class BlockList {
    public static Block storageController;
    public static Block storageConnector;

    //TileEntity Types
    public static TileEntityType<?> storageControllerTileType;
    public static TileEntityType<?> storageConnectorTileType;

    //Container Types
    public static ContainerType<StorageControllerContainer> storageControllerContainerType;
}
