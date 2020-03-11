package teabx.vanillaextended.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import teabx.vanillaextended.container.CollectiveStorageContainer;
import teabx.vanillaextended.main.VanillaExtended;

public class CollectiveStorageScreen extends ContainerScreen<CollectiveStorageContainer> {

    private static final ResourceLocation COLLECTIVE_STORAGE_TEXTURE = new ResourceLocation(VanillaExtended.MODID, "textures/gui/csgui.png");
    private static final ResourceLocation SCROLLBAR_TEXTURE = new ResourceLocation(VanillaExtended.MODID, "textures/gui/scrollbar.png");
    private boolean isScrolling = false;
    private double currentScroll;

    public CollectiveStorageScreen(CollectiveStorageContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 194;
        this.ySize = 226;
        this.setSize(512,512);
    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
        ImageButton scrollButton = new ImageButton(guiLeft + 175, guiTop + 18, 12, 36, 0, 0, 0,  SCROLLBAR_TEXTURE, p_onPress_1_ -> {
            //button stuff
        });
        scrollButton.visible = true;
        scrollButton.active = false;
        buttons.add(scrollButton);
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        isScrolling = false;
        return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    public boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if(this.isScrolling){
            double pos = p_mouseDragged_3_;
            double top = this.guiTop + 18;
            double bottom = this.guiTop + 124 - 36;
            pos = MathHelper.clamp(pos, top, bottom);
            buttons.get(0).y = (int) pos;
            currentScroll = (pos-top)/(bottom-top);
            int val = (int) (currentScroll * ((container.getInvSize())-6));
            container.updateSlots(val);
        }
        return super.mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if(p_mouseClicked_1_ >= guiLeft + 175 && p_mouseClicked_1_ <= guiLeft + 175 + 12
                && p_mouseClicked_3_ >= guiTop + 18 && p_mouseClicked_3_ <= guiTop + 124){
            this.isScrolling = true;
        }
        return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        currentScroll = (int) (currentScroll - (p_mouseScrolled_5_ / (container.getInvSize()/5)));
        currentScroll = MathHelper.clamp(currentScroll, this.guiTop + 18, this.guiTop + 124 - 36);
        buttons.get(0).y = (int) currentScroll;
        container.updateSlots(map(currentScroll));
        return false;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(COLLECTIVE_STORAGE_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, this.ySize, 512, 512);
    }

    private int map(double pos){
        int map_max = (container.getInvSize()+1/9) - 6;
        double l = this.guiTop + 18;
        double h = this.guiTop + 124 - 36;
        double n = (pos - l);
        double d = (h - l);
        double val = n/d;
        val = val * map_max;
        return (int) val;
    }

}
