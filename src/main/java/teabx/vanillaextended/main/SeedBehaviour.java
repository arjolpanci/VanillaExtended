package teabx.vanillaextended.main;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IDispenseItemBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class SeedBehaviour implements IDispenseItemBehavior {


    @Override
    public ItemStack dispense(IBlockSource source, ItemStack item) {

        IPosition pos = DispenserBlock.getDispensePosition(source);
        BlockPos pos1 = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
        BlockPos pos2 = new BlockPos(pos.getX(), pos.getY()-1, pos.getZ());

        if(source.getWorld().getBlockState(pos2).getBlock() == Blocks.FARMLAND && source.getWorld().getBlockState(pos1).getBlock() == Blocks.AIR){
            source.getWorld().setBlockState(pos1, Blocks.WHEAT.getDefaultState());
            ItemStack stack = item.split(1);
            return item;
        }

        return item;
    }


}
