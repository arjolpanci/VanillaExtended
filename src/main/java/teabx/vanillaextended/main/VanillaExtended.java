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
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import teabx.vanillaextended.blocks.BlockList;
import teabx.vanillaextended.blocks.TransportPipe;
import teabx.vanillaextended.client.gui.CollectiveStorageScreen;
import teabx.vanillaextended.container.CollectiveStorageContainer;
import teabx.vanillaextended.tileentities.CSTile;
import teabx.vanillaextended.blocks.CollectiveStorage;
import teabx.vanillaextended.capabilities.CapabilityRegistry;
import teabx.vanillaextended.client.renders.RenderRegistry;
import teabx.vanillaextended.entities.EntityRegistry;
import teabx.vanillaextended.items.ItemList;
import teabx.vanillaextended.items.KingBow;
import teabx.vanillaextended.items.LordStaff;
import teabx.vanillaextended.tileentities.TPTile;
import teabx.vanillaextended.tools.ToolList;
import teabx.vanillaextended.tools.ToolMaterial;

@Mod("vanillaextended")
public class VanillaExtended
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "vanillaextended";
    public static VanillaExtended instance;

    public static ResourceLocation rloc(String name){ return new ResourceLocation(MODID, name);};

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
        ScreenManager.registerFactory(BlockList.CSContainerType, CollectiveStorageScreen::new);
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
                    BlockList.collectiveStorage = new CollectiveStorage(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).harvestLevel(2)).setRegistryName(rloc("collective_storage")),
                    BlockList.transportPipe = new TransportPipe(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD)).setRegistryName(rloc("transport_pipe"))
            );
        }

        @SubscribeEvent
        public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().registerAll(
                    EntityRegistry.LOST_MINER,
                    EntityRegistry.SKELETON_KING,
                    EntityRegistry.STAFF_ZOMBIE
            );
        }


        @SubscribeEvent
        public static void registerContainers(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().registerAll(
                    BlockList.CSContainerType = (ContainerType<CollectiveStorageContainer>) IForgeContainerType.create((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        return new CollectiveStorageContainer(windowId, Minecraft.getInstance().world, pos, inv);
                    }).setRegistryName(rloc("collective_storage"))
            );
        }

        @SubscribeEvent
        public static void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().registerAll(
                    BlockList.CSTileType = TileEntityType.Builder.create(CSTile::new, BlockList.collectiveStorage).build(null).setRegistryName(rloc("collective_storage")),
                    BlockList.TPTileType = TileEntityType.Builder.create(TPTile::new, BlockList.transportPipe).build(null).setRegistryName(rloc("transport_pipe"))
            );
        }

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            event.getRegistry().registerAll(
                    ItemList.test_item = new Item(new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("test_item")),
                    ToolList.flint_axe = new AxeItem(ToolMaterial.flint, 6.5F, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_axe")),
                    ToolList.flint_pickaxe = new PickaxeItem(ToolMaterial.flint, 0, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_pickaxe")),
                    ToolList.flint_shovel = new ShovelItem(ToolMaterial.flint, 0, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_shovel")),
                    ToolList.flint_hoe = new HoeItem(ToolMaterial.flint, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_hoe")),
                    ToolList.flint_sword = new SwordItem(ToolMaterial.flint, 0, -3.2F, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(rloc("flint_sword")),
                    ItemList.king_bow = new KingBow(new Item.Properties().group(ItemGroup.MISC).maxDamage(200)).setRegistryName(rloc("king_bow")),
                    ItemList.lord_staff = new LordStaff(new Item.Properties().group(ItemGroup.MISC).maxDamage(200)).setRegistryName(rloc("zombie_lord_staff")),
                    ItemList.collective_storage = new BlockItem(BlockList.collectiveStorage, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(BlockList.collectiveStorage.getRegistryName()),
                    ItemList.transport_pipe = new BlockItem(BlockList.transportPipe, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(BlockList.transportPipe.getRegistryName())
            );

            EntityRegistry.registerSpawnEggs(event);
        }
    }
}
