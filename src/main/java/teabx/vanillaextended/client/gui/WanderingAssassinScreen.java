package teabx.vanillaextended.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.entities.AssassinOffer;
import teabx.vanillaextended.main.VanillaExtended;

public class WanderingAssassinScreen extends ContainerScreen<WanderingAssassinContainer> {

    private static final ResourceLocation WANDERING_ASSASSIN_GUI = new ResourceLocation(VanillaExtended.MODID, "textures/gui/wandering_asssing_gui.png");
    private AssassinOffer currentOffer;

    public WanderingAssassinScreen(WanderingAssassinContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 276;
        this.ySize = 166;
        this.setSize(512, 512);

        for(int i=0; i<container.getOffers().size(); i++){
            int finalI = i;
            Button button = new Button(guiLeft + 5, guiTop + 18 + (20*i), 88, 20, "", p_onPress_1_ -> {
                this.currentOffer = container.getOffers().get(finalI);
            });
            buttons.add(button);
        }
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        int cnt=0;

        for(AssassinOffer ao : container.getOffers()){
            ItemStack gold = new ItemStack(Items.GOLD_INGOT);
            gold.setCount(ao.getPrice());
            ItemStack item = ao.getItem();
            this.itemRenderer.renderItemAndEffectIntoGUI(gold, guiLeft + 5 + 22, guiTop + 18 + 4 + (cnt*20));
            this.itemRenderer.renderItemOverlayIntoGUI(this.font, gold, guiLeft + 5 + 22, guiTop + 18 + 4 + (cnt*20), (String)null);
            this.itemRenderer.renderItemAndEffectIntoGUI(item, guiLeft + 5 + 66, guiTop + 18 + 4 + (cnt*20));
            this.itemRenderer.renderItemOverlayIntoGUI(this.font, item, guiLeft + 5 + 66, guiTop + 18 + 4 + (cnt*20), (String)null);
            cnt++;
        }
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(WANDERING_ASSASSIN_GUI);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize, 512, 512);
    }
}
