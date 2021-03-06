package teabx.vanillaextended.main;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.blocks.StorageConnector;
import teabx.vanillaextended.client.gui.StorageControllerScreen;
import teabx.vanillaextended.client.gui.WanderingAssassinScreen;
import teabx.vanillaextended.container.ContainerTypes;
import teabx.vanillaextended.container.StorageControllerContainer;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.tileentities.StorageControllerTile;
import teabx.vanillaextended.blocks.StorageController;
import teabx.vanillaextended.capabilities.CapabilityRegistry;
import teabx.vanillaextended.client.renders.RenderRegistry;
import teabx.vanillaextended.entities.EntityRegistry;
import teabx.vanillaextended.items.KingBow;
import teabx.vanillaextended.items.LordStaff;
import teabx.vanillaextended.tileentities.StorageConnectorTile;
import teabx.vanillaextended.items.tools.ToolMaterial;

@Mod("vanillaextended")
public class VanillaExtended
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "vanillaextended";
    public static VanillaExtended instance;
    private static final String PROTOCOL_VERSION = "1";

    public static ResourceLocation rloc(String name){ return new ResourceLocation(MODID, name);}

    public VanillaExtended() {
        instance = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }


    private void setup(final FMLCommonSetupEvent event)
    {
        DispenserBlock.registerDispenseBehavior(Items.WHEAT_SEEDS, new SeedBehaviour());
        DispenserBlock.registerDispenseBehavior(Items.BEETROOT_SEEDS, new BeetRootBehavior());
        CapabilityRegistry.registerCapabilities();
        ScreenManager.registerFactory(ContainerTypes.storageControllerContainerType, StorageControllerScreen::new);
        ScreenManager.registerFactory(ContainerTypes.wanderingAssassinContainerType, WanderingAssassinScreen::new);
        PacketHandler.INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(VanillaExtended.MODID, "main"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );
        PacketHandler.registerMessages();

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        RenderRegistry.registerEntityRenders();
        EntityRegistry.registerEntitySpawn();
    }

    @Mod.EventBusSubscriber(modid = VanillaExtended.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new StorageController(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).harvestLevel(2)).setRegistryName(rloc("storage_controller")),
                    new StorageConnector(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD)).setRegistryName(rloc("storage_connector"))
            );
        }

        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
            EntityRegistry.buildEntities();
            event.getRegistry().registerAll(
                    EntityRegistry.LOST_MINER,
                    EntityRegistry.SKELETON_KING,
                    EntityRegistry.STAFF_ZOMBIE,
                    EntityRegistry.WANDERING_ASSASSIN
            );
        }


        @SubscribeEvent
        public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().registerAll(
                    ContainerTypes.storageControllerContainerType = (ContainerType<StorageControllerContainer>) IForgeContainerType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        return new StorageControllerContainer(windowId, Minecraft.getInstance().world, pos, inv);
                    }).setRegistryName(rloc("storage_controller")),
                    ContainerTypes.wanderingAssassinContainerType = (ContainerType<WanderingAssassinContainer>) IForgeContainerType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        return new WanderingAssassinContainer(windowId, inv, Minecraft.getInstance().world, pos);
                    })).setRegistryName(rloc("wandering_assassin"))
            );
        }

        @SubscribeEvent
        public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().registerAll(
                    BlockList.storageControllerTileType = TileEntityType.Builder.create(StorageControllerTile::new, BlockList.storageController).build(null).setRegistryName(rloc("storage_controller")),
                    BlockList.storageConnectorTileType = TileEntityType.Builder.create(StorageConnectorTile::new, BlockList.storageConnector).build(null).setRegistryName(rloc("storage_connector"))
            );
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    new AxeItem(ToolMaterial.flint, 6.5F, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_axe")),
                    new PickaxeItem(ToolMaterial.flint, 0, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_pickaxe")),
                    new ShovelItem(ToolMaterial.flint, 0, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_shovel")),
                    new HoeItem(ToolMaterial.flint, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_hoe")),
                    new SwordItem(ToolMaterial.flint, 0, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_sword")),
                    new BlockItem(BlockList.storageController, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(BlockList.storageController.getRegistryName()),
                    new BlockItem(BlockList.storageConnector, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(BlockList.storageConnector.getRegistryName()),
                    new KingBow(new Item.Properties().group(ItemGroup.MISC).maxDamage(200)).setRegistryName(rloc("king_bow")),
                    new LordStaff(new Item.Properties().group(ItemGroup.MISC).maxDamage(200)).setRegistryName(rloc("zombie_lord_staff"))
            );

            EntityRegistry.registerSpawnEggs(event);
        }
    }
}
