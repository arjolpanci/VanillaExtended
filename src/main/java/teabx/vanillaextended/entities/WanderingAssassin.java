package teabx.vanillaextended.entities;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.network.UpdateAssassinOfferList;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.function.Supplier;


public class WanderingAssassin extends CreatureEntity implements INamedContainerProvider {

    private PlayerEntity customer;
    private final NonNullList<ItemStack> slots = NonNullList.withSize(2, ItemStack.EMPTY);
    private Inventory inventory = new Inventory(2);
    private ArrayList<AssassinOffer> offerList = new ArrayList<>();

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
            PacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), new UpdateAssassinOfferList(this));
        }
        //if(!world.isRemote){
        //    for(AssassinOffer ao : offerList){
        //        System.out.println(ao.getPrice() + " " + ao.getItem());
        //    }
        //}
        return super.processInteract(player, hand);
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        int offers = (int)(Math.random() * 10) + 1;
        for(int i=0; i<offers; i++){ offerList.add(new AssassinOffer()); }
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        byte[] data = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this.offerList);
            data = baos.toByteArray();
            oos.close(); baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        compound.putByteArray("offer_list", data);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        byte[] data = compound.getByteArray("offer_list");
        if(data != null){
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                this.offerList = (ArrayList<AssassinOffer>) ois.readObject();
                ois.close(); bais.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public ArrayList<AssassinOffer> getOfferList() {
        return offerList;
    }

    public void setOfferList(ArrayList<AssassinOffer> offerList) {
        this.offerList = offerList;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        int entityId = this.getEntityId();
        return new WanderingAssassinContainer(id, playerInventory, this.getEntityWorld(), new BlockPos(entityId,entityId,entityId));
    }
}
