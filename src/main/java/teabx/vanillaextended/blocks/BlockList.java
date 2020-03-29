package teabx.vanillaextended.blocks;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import teabx.vanillaextended.container.StorageControllerContainer;

public class BlockList {
    @ObjectHolder("vanillaextended:storage_controller")
    public static Block storageController;

    @ObjectHolder("vanillaextended:storage_connector")
    public static Block storageConnector;

    //TileEntity Types
    public static TileEntityType<?> storageControllerTileType;
    public static TileEntityType<?> storageConnectorTileType;
}
