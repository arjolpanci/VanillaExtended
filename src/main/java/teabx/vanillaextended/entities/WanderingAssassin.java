package teabx.vanillaextended.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import teabx.vanillaextended.container.WanderingAssassinContainer;

import javax.annotation.Nullable;


public class WanderingAssassin extends CreatureEntity implements INamedContainerProvider {

    private PlayerEntity customer;
    private final NonNullList<ItemStack> slots = NonNullList.withSize(2, ItemStack.EMPTY);
    private Inventory inventory = new Inventory(2);

    protected WanderingAssassin(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.customer = null;
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        this.setCustomer(player);
        WanderingAssassin wanderingAssassin = (WanderingAssassin) world.getEntityByID(this.getEntityId());
        int id = wanderingAssassin.getEntityId();
        if(!world.isRemote){
            NetworkHooks.openGui((ServerPlayerEntity) player, wanderingAssassin, new BlockPos(id,id,id));
        }
        return super.processInteract(player, hand);
    }

    public void setCustomer(PlayerEntity customer) {
        this.customer = customer;
    }

    public PlayerEntity getCustomer() {
        return customer;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new WanderingAssassinContainer(id, playerInventory, (WanderingAssassin) world.getEntityByID(this.getEntityId()));
    }
}
