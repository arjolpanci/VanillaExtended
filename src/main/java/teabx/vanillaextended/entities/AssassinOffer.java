package teabx.vanillaextended.entities;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class AssassinOffer implements Serializable {

    /*
    INDEXES:
        TOOLS : 0 = SWORD ; 1 = PICKAXE ; 2 = AXE ; 3 = BOW ; 4 = SHOVEL

        ENCHANTMENTS : 0 = BOA ; 1 = EFFICIENCY ; 2 = FORTUNE ; 3 = LOOTING ; 4 = MENDING ;
        5 = POWER ; 6 = SHARPNESS ; 7 = UNBREAKING ; 8 = INFINITY ; 9 = SMITE
     */

    private int totalPrice;
    private int toolIndex;
    private ArrayList<Integer> enchantmentData = new ArrayList<>();

    public AssassinOffer() {
        Random random = new Random();
        int tool_idx = random.nextInt(5);
        this.toolIndex = tool_idx;

        int nr_of_enchantments = random.nextInt(8) + 2;
        if(nr_of_enchantments > 7) nr_of_enchantments = 7;

        this.totalPrice = 0;
        for(int i=0; i<nr_of_enchantments; i++){
            int enchantment_idx = random.nextInt(10);
            if(!(tool_idx == 3)){
                if(enchantment_idx == 5 || enchantment_idx == 8) enchantment_idx++;
            }

            this.totalPrice += calculatePricePerEnchantment(enchantment_idx);

            int lvl = random.nextInt(8) + 3;
            if(lvl > 7) lvl = 7;

            this.enchantmentData.add(enchantment_idx);
            this.enchantmentData.add(lvl);
        }
        if(this.totalPrice > 64) this.totalPrice = 64;
    }

    public int getPrice() { return totalPrice; }

    public ItemStack getItem(){
        ItemStack stack = new ItemStack(getItemFromIndex(this.toolIndex));

        int cnt = 0;
        while(cnt<=this.enchantmentData.size()){
            if(cnt >= this.enchantmentData.size()) break;
            Enchantment ench = getEnchantmentFromIndex(cnt);
            int lvl = this.enchantmentData.get(cnt+1);
            stack.addEnchantment(ench, lvl);
            cnt += 2;
        }

        return stack;
    }

    private Enchantment getEnchantmentFromIndex(int idx){
        switch(idx){
            case 0 : return Enchantments.BANE_OF_ARTHROPODS;
            case 1 : return Enchantments.EFFICIENCY;
            case 2 : return Enchantments.FORTUNE;
            case 3 : return Enchantments.LOOTING;
            case 4 : return Enchantments.MENDING;
            case 5 : return Enchantments.POWER;
            case 6 : return Enchantments.SHARPNESS;
            case 7 : return Enchantments.UNBREAKING;
            case 8 : return Enchantments.INFINITY;
            case 9 : return Enchantments.SMITE;
        }
        return Enchantments.UNBREAKING;
    }

    private Item getItemFromIndex(int idx){
        switch(idx){
            case 0 : return Items.DIAMOND_SWORD;
            case 1 : return Items.DIAMOND_PICKAXE;
            case 2 : return Items.DIAMOND_AXE;
            case 3 : return Items.BOW;
            case 4 : return Items.DIAMOND_SHOVEL;
        }
        return Items.DIAMOND_SWORD;
    }

    private int calculatePricePerEnchantment(int idx){
        switch(idx){
            case 0 : return 2;
            case 1 : return 5;
            case 2 : return 7;
            case 3 : return 4;
            case 4 : return 10;
            case 5 : return 5;
            case 6 : return 5;
            case 7 : return 8;
            case 8 : return 9;
            case 9 : return 2;
        }
        return 1;
    }

}
