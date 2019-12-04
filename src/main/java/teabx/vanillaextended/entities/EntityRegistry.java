package teabx.vanillaextended.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import teabx.vanillaextended.main.VanillaExtended;

public class EntityRegistry {


    //Entities
    public static EntityType<?> LOST_MINER = EntityType.Builder.create(LostMiner::new, EntityClassification.MONSTER)
            .build("lost_miner").setRegistryName(VanillaExtended.rloc("lost_miner"));
    public static EntityType<?> SKELETON_KING = EntityType.Builder.create(SkeletonKing::new, EntityClassification.MONSTER)
            .build("skeleton_king").setRegistryName(VanillaExtended.rloc("skeleton_king"));

    //Item Eggs
    public static Item lost_miner_egg;
    public static Item skeleton_king_egg;

    public static void registerEntitySpawn(){
        getEntitySpawn(LOST_MINER, EntityClassification.MONSTER);
    }

    public static void getEntitySpawn(EntityType<?> e, EntityClassification e_class){
        //Biome[] biomes = {Biomes.BADLANDS, Biomes.BADLANDS_PLATEAU, Biomes.BAMBOO_JUNGLE, Biomes.BAMBOO_JUNGLE_HILLS,
        //Biomes.BIRCH_FOREST, Biomes.BIRCH_FOREST_HILLS, Biomes.DARK_FOREST, Biomes.DARK_FOREST_HILLS, Biomes.DESERT, Biomes.DESERT_HILLS,
        //Biomes.END_BARRENS, Biomes.PLAINS};
        for(Biome biome : ForgeRegistries.BIOMES.getValues()){
            if(biome != null && biome != Biomes.OCEAN){
                biome.getSpawns(e_class).add(new Biome.SpawnListEntry(e, 10, 1, 8));
            }
        }
    }




    public static Item generateSpawnEgg(EntityType<?> e, int col1, int col2, String name){
        SpawnEggItem egg = new SpawnEggItem(e, col1, col2, new Item.Properties().group(ItemGroup.MISC));
        egg.setRegistryName(VanillaExtended.rloc(name));
        return egg;
    }

    public static void registerSpawnEggs(final RegistryEvent.Register<Item> e){
        e.getRegistry().registerAll(
                lost_miner_egg = generateSpawnEgg(LOST_MINER, 0x3273a8, 0xf7f411, "lost_miner_egg"),
                skeleton_king_egg = generateSpawnEgg(SKELETON_KING, 0x3273a8, 0xf7f411, "skeleton_king_egg")
        );
    }

}
