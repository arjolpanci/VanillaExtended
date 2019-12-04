package teabx.vanillaextended.main;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import teabx.vanillaextended.capabilities.CapabilityRegistry;
import teabx.vanillaextended.capabilities.CoolDownItem;
import teabx.vanillaextended.capabilities.PlayerRank;
import teabx.vanillaextended.capabilities.interfaces.IRank;
import teabx.vanillaextended.entities.EntityRegistry;
import teabx.vanillaextended.items.LordStaff;

public class EventHandler {

    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent event){
        if(event.getEntity().getType() == EntityRegistry.LOST_MINER &&
                event.getWorld().getBlockState(new BlockPos(event.getX(), event.getY(), event.getZ())).getBlock() != Blocks.CAVE_AIR &&
                event.getWorld().getLight(new BlockPos(event.getX(), event.getY(), event.getZ())) > 7 &&
                event.getWorld().getBiome(new BlockPos(event.getX(), event.getY(), event.getZ())) == Biomes.OCEAN){
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void CapabilityAttachItemStack(AttachCapabilitiesEvent<ItemStack> event){
        if(event.getObject().getItem() instanceof LordStaff){
            event.addCapability(new ResourceLocation(VanillaExtended.modid, "cooldownitem"), new CoolDownItem());
        }
    }

    @SubscribeEvent
    public static void CapabilityAttachPlayer(AttachCapabilitiesEvent<Entity> event){
        if(event.getObject().getEntity() instanceof PlayerEntity){
            event.addCapability(VanillaExtended.rloc("playerrank"), new PlayerRank());
        }
    }

    @SubscribeEvent
    public static void LivingHurt(LivingHurtEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            IRank rank = player.getCapability(CapabilityRegistry.PLAYER_RANK, null).orElse(null);
            if(rank != null){
                int rnk = rank.getRank();
                player.attackEntityFrom(DamageSource.GENERIC, event.getAmount()*rnk);
            }

        }
    }

}
