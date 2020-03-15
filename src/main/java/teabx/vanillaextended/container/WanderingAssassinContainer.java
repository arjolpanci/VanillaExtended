package teabx.vanillaextended.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.World;
import teabx.vanillaextended.entities.WanderingAssassin;


public class WanderingAssassinContainer extends Container {

    public WanderingAssassin wanderingAssassin;

    public WanderingAssassinContainer(int id, PlayerInventory playerInventory, World world) {
        super(ContainerTypes.wanderingAssassinContainerType, id);
        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++){
                this.addSlot(new Slot(playerInventory, j+i*9+9, 108 + j*18, 84 + i*18));
            }
        }
        for(int i=0; i<9; i++){
            this.addSlot(new Slot(playerInventory, i, 108 + i*18, 142));
        }
    }

    public WanderingAssassinContainer(int id, PlayerInventory playerInventory, WanderingAssassin wanderingAssassin){
        this(id, playerInventory, wanderingAssassin.getEntityWorld());
        this.wanderingAssassin = wanderingAssassin;
        this.addSlot(new Slot(wanderingAssassin.getInventory(), 0, 145, 37));
        this.addSlot(new Slot(wanderingAssassin.getInventory(), 1, 199, 37));
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        if(wanderingAssassin == null) return false;
        return this.wanderingAssassin.getCustomer() == playerIn;
    }

}
