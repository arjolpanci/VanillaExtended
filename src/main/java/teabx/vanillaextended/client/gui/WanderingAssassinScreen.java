package teabx.vanillaextended.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.entities.AssassinOffer;
import teabx.vanillaextended.main.VanillaExtended;
import teabx.vanillaextended.network.PacketHandler;
import teabx.vanillaextended.network.UpdateTradeSelection;
import teabx.vanillaextended.network.ValidateTradeInput;

import java.util.ArrayList;

public class WanderingAssassinScreen extends ContainerScreen<WanderingAssassinContainer> {

    private static final ResourceLocation WANDERING_ASSASSIN_GUI = new ResourceLocation(VanillaExtended.MODID, "textures/gui/wandering_asssing_gui.png");
    private int currentScroll=0;
    private boolean isScrolling = false;
    private boolean needsScrolling = true;


    public WanderingAssassinScreen(WanderingAssassinContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 276;
        this.ySize = 166;
        this.setSize(512, 512);
        container.inventorySlots.get(38).putStack(ItemStack.EMPTY);
        container.inventorySlots.get(37).putStack(ItemStack.EMPTY);
        container.detectAndSendChanges();
    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
        ImageButton scrollButton = new ImageButton(guiLeft + 94, guiTop + 18, 6, 27, 276, 0, 0,  WANDERING_ASSASSIN_GUI, 512, 512, p_onPress_1_ -> { });

        this.addButton(scrollButton);

        for(int i=0; i<7; i++){
            int finalI = i;
            Button button = new Button(guiLeft + 5, guiTop + 18 + (20*i), 88, 20, "", p_onPress_1_ -> {
                PacketHandler.INSTANCE.sendToServer(new UpdateTradeSelection(container, finalI+currentScroll));
                PacketHandler.INSTANCE.sendToServer(new ValidateTradeInput(container.inventorySlots.get(36).getStack()));
            });
            button.visible = true;
            button.active = true;
            this.addButton(button);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int p_mouseClicked_5_) {
        if(needsScrolling){
            if(x > guiLeft + 94 && x < guiLeft + 101 && y > guiTop + 18 && y < guiTop + 159) this.isScrolling = true;
        }
        return super.mouseClicked(x, y, p_mouseClicked_5_);
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if(this.isScrolling){
            int yPos = (int) p_mouseDragged_3_;
            yPos = MathHelper.clamp(yPos, guiTop + 18, guiTop + 159 - 28);
            buttons.get(0).y = yPos;
            this.currentScroll = map(yPos);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        this.isScrolling = false;
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);

        int offset = 0;
        ArrayList<AssassinOffer> offerList = container.offerList;
        int cnt = currentScroll;
        if(currentScroll >= offerList.size() - 7) currentScroll = offerList.size() - 7;
        if(offerList.size() <= 7){
            cnt=0;
            currentScroll=0;
        }

        for(int i=currentScroll; i<currentScroll+7; i++){
            if(cnt >= offerList.size() || cnt < 0) break;
            AssassinOffer ao = offerList.get(cnt++);
            ItemStack gold = new ItemStack(Items.GOLD_INGOT);
            gold.setCount(ao.getPrice());
            ItemStack item = ao.getItem();
            this.itemRenderer.renderItemAndEffectIntoGUI(gold, guiLeft + 5 + 16, guiTop + 20 + (offset*20));
            this.itemRenderer.renderItemOverlayIntoGUI(this.font, gold, guiLeft + 5 + 16, guiTop + 20 + (offset*20), (String)null);
            this.itemRenderer.renderItemAndEffectIntoGUI(item, guiLeft + 5 + 60, guiTop + 20 + (offset*20));
            this.itemRenderer.renderItemOverlayIntoGUI(this.font, item, guiLeft + 5 + 60, guiTop + 20 + (offset*20), (String)null);
            offset++;
        }

        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    public void tick() {
        super.tick();

        ArrayList<AssassinOffer> offerList = container.offerList;
        if(currentScroll >= offerList.size() - 7) currentScroll = offerList.size() - 7;

        if(offerList.size() < 7){
            currentScroll = 0;
            for(int i=1; i<buttons.size(); i++){
                if(i<offerList.size()+1) continue;
                buttons.get(i).visible = false;
            }
            needsScrolling = false;
        }

        for(Widget w : buttons){
            w.active = true;
        }

        for(int i=currentScroll; i<currentScroll+7; i++){
            if(container.offerList.size() > 0){
                if(i < container.offerList.size()) {
                    if (!container.offerList.get(i).getAvailable()) {
                        this.buttons.get(i - currentScroll + 1).active = false;
                    }
                }
            }
        }
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(WANDERING_ASSASSIN_GUI);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize, 512, 512);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString("Trade Offers", 4.0F, 7.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 107.0F, 74.0F, 4210752);
    }

    private int map(double pos){
        int map_max = container.offerList.size() - 7;
        double l = this.guiTop + 18;
        double h = this.guiTop + 159 - 28;
        double n = (pos - l);
        double d = (h - l);
        double val = n/d;
        val = val * map_max;
        val = MathHelper.clamp(val, 0, container.offerList.size() - 7);
        return (int) val;
    }
}
