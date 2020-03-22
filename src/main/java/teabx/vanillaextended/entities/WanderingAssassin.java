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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.network.UpdateClientOfferList;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class WanderingAssassin extends CreatureEntity implements INamedContainerProvider {

    private PlayerEntity customer;
    private Inventory inventory = new Inventory(3);
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
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new UpdateClientOfferList(this));
        }
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
        compound.putInt("Size", offerList.size());
        int cnt = 0;
        for(AssassinOffer offer : offerList){
            compound.putInt("Price" + cnt, offer.getPrice());
            compound.putInt("ToolIndex" + cnt, offer.getToolIndex());
            compound.putBoolean("Available" + cnt, offer.getAvailable());
            compound.putInt("EnchDataSize" + cnt, offer.getEnchantmentData().size());
            int cnt2 = 0;
            for(int i : offer.getEnchantmentData()){
                compound.putInt("EnchData" + cnt2++, i);
            }
            cnt++;
        }
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        ArrayList<AssassinOffer> ao = new ArrayList<>();
        int cnt = compound.getInt("Size");
        for(int i=0; i<cnt; i++){
            int price = compound.getInt("Price" + i);
            int toolIndex = compound.getInt("ToolIndex" + i);
            boolean isAvailable = compound.getBoolean("Available" + i);
            int enchSize = compound.getInt("EnchDataSize" + i);
            ArrayList<Integer> enchData = new ArrayList<>();
            for(int j=0; j<enchSize; j++){
                enchData.add(compound.getInt("EnchData" + j));
            }
            AssassinOffer assassinOffer = new AssassinOffer(price, toolIndex, isAvailable, enchData);
            ao.add(assassinOffer);
        }
        this.offerList = ao;
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

    public void setInventory(Inventory inventory) { this.inventory = inventory; }

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
        return new WanderingAssassinContainer(id, playerInventory, this.getEntityWorld(), new BlockPos(entityId,entityId,entityId), playerEntity);
    }
}
