package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;
import teabx.vanillaextended.entities.AssassinOffer;
import teabx.vanillaextended.entities.WanderingAssassin;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.network.UpdateClientOfferList;

import java.util.ArrayList;


public class WanderingAssassinContainer extends Container {

    public WanderingAssassin wanderingAssassin;
    public ArrayList<AssassinOffer> offerList;
    public AssassinOffer currentOffer;
    public boolean tradeConfirmed = false;
    private int entityID;
    public PlayerEntity playerEntity;

    public WanderingAssassinContainer(int id, PlayerInventory playerInventory, World world, BlockPos pos) {
        super(ContainerTypes.wanderingAssassinContainerType, id);
        this.entityID = pos.getX();
        this.wanderingAssassin = (WanderingAssassin) world.getEntityByID(pos.getX());
        this.offerList = wanderingAssassin.getOfferList();

        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++){
                this.addSlot(new Slot(playerInventory, j+i*9+9, 108 + j*18, 84 + i*18));
            }
        }
        for(int i=0; i<9; i++){
            this.addSlot(new Slot(playerInventory, i, 108 + i*18, 142));
        }

        this.addSlot(new CustomSlot(this.wanderingAssassin.getInventory(), 0, 153, 41, true, false, this));
        this.addSlot(new CustomSlot(this.wanderingAssassin.getInventory(), 1, 201, 41, true, true, this));
        this.addSlot(new CustomSlot(this.wanderingAssassin.getInventory(), 2, 112, 22, false, false, this));
    }

    public WanderingAssassinContainer(int id, PlayerInventory playerInventory, World world, BlockPos pos, PlayerEntity player) {
        this(id, playerInventory, world, pos);
        this.playerEntity = player;
    }

    public void validateTradeInput(ItemStack stack){
        if(playerEntity.openContainer instanceof WanderingAssassinContainer && this.canInteractWith(playerEntity)){
            if(stack.getItem() == Items.GOLD_INGOT && (stack.getCount() >= this.currentOffer.getPrice())){
                this.inventorySlots.get(37).putStack(this.currentOffer.getItem());
                this.detectAndSendChanges();
            }else{
                this.inventorySlots.get(37).putStack(ItemStack.EMPTY);
                this.detectAndSendChanges();
            }
        }
    }

    public void processTradeRequest(){
        if(playerEntity.openContainer instanceof WanderingAssassinContainer && this.canInteractWith(playerEntity)){
            if(this.inventorySlots.get(36).getStack().getItem() == Items.GOLD_INGOT && (this.inventorySlots.get(36).getStack().getCount() >= this.currentOffer.getPrice())){
                for(int i=0; i<this.offerList.size(); i++) {
                    if (this.offerList.get(i) == this.currentOffer) {
                        this.offerList.get(i).setAvailable(false);
                    }
                }
                this.wanderingAssassin.setOfferList(this.offerList);
                this.inventorySlots.get(36).decrStackSize(this.currentOffer.getPrice());
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerEntity), new UpdateClientOfferList(this.wanderingAssassin));
                this.detectAndSendChanges();
            }else{
                this.inventorySlots.get(37).putStack(ItemStack.EMPTY);
                this.detectAndSendChanges();
            }
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        if(wanderingAssassin == null) return false;
        return this.wanderingAssassin.getCustomer() == playerIn;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index < 45) {
                if (!this.mergeItemStack(itemstack1, 45, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 45, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

}
