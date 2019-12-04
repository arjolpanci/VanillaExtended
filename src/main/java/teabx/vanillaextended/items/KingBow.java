package teabx.vanillaextended.items;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class KingBow extends ShootableItem {

    public KingBow(Properties properties) {
        super(properties);
        this.addPropertyOverride(new ResourceLocation("pull"), (p_210310_0_, p_210310_1_, p_210310_2_) -> {
            if (p_210310_2_ == null) {
                return 0.0F;
            } else {
                return !(p_210310_2_.getActiveItemStack().getItem() instanceof BowItem) ? 0.0F : (float)(p_210310_0_.getUseDuration() - p_210310_2_.getItemInUseCount()) / 20.0F;
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), (p_210309_0_, p_210309_1_, p_210309_2_) -> {
            return p_210309_2_ != null && p_210309_2_.isHandActive() && p_210309_2_.getActiveItemStack() == p_210309_0_ ? 1.0F : 0.0F;
        });
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {

        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (!worldIn.isRemote) {
                float f = getArrowVelocity(this.getUseDuration(stack) - timeLeft);

                for(int i=0; i<3; i++){
                    ItemStack ammo = entityLiving.findAmmo(stack);
                    ArrowItem arrow_item = (ArrowItem) ammo.getItem();
                    AbstractArrowEntity arrow = arrow_item.createArrow(worldIn, ammo, player);

                    if(i != 0){
                        arrow.shoot(player, player.rotationPitch, player.rotationYaw + (3.0F * (float)Math.pow(-1,i)), 0.0F, f*4.0F, 1.0F);
                    }else{
                        arrow.shoot(player, player.rotationPitch, player.rotationYaw , 0.0F, f*4.0F, 1.0F);
                    }

                    stack.damageItem(1, player, (p_220009_1_) -> {
                        p_220009_1_.sendBreakAnimation(player.getActiveHand());
                    });

                    if(EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0 || player.abilities.isCreativeMode){
                        arrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                    }else{
                        ammo.split(3);
                    }

                    worldIn.addEntity(arrow);
                }
            }
            worldIn.playSound((PlayerEntity) null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT,
                    SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + 1.0F * 0.5F);
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack item = player.getHeldItem(hand);
        boolean isEmpty = player.findAmmo(item).isEmpty();

        if(!player.abilities.isCreativeMode && isEmpty){
            return !isEmpty ? new ActionResult<>(ActionResultType.PASS, item) : new ActionResult<>(ActionResultType.FAIL, item);
    }else{
            player.setActiveHand(hand);
            return new ActionResult<>(ActionResultType.SUCCESS, item);
        }
    }

    public static float getArrowVelocity(int charge) {
        float f = (float)charge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack stack) { return 72000; }

    @Override
    public UseAction getUseAction(ItemStack stack) { return UseAction.BOW; }

    @Override
    public Predicate<ItemStack> getInventoryAmmoPredicate() { return ARROWS; }

    @Override
    public Predicate<ItemStack> getAmmoPredicate() { return ARROWS; }

    public AbstractArrowEntity customeArrow(AbstractArrowEntity arrow) {
        return arrow;
    }

}
