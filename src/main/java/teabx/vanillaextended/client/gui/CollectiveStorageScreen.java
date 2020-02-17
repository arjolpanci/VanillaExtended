package teabx.vanillaextended.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import teabx.vanillaextended.container.CollectiveStorageContainer;
import teabx.vanillaextended.main.VanillaExtended;

public class CollectiveStorageScreen extends ContainerScreen<CollectiveStorageContainer> {

    private static final ResourceLocation COLLECTIVE_STORAGE_TEXTURE = new ResourceLocation(VanillaExtended.MODID, "textures/gui/csgui.png");
    private boolean isScrolling = false;
    private int currentScroll;

    public CollectiveStorageScreen(CollectiveStorageContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 367;
        this.ySize = 132;
        this.setSize(512,512);
    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
        Button scrollButton = new Button(guiLeft + 175, guiTop + 18, 12, 36, "|", p_onPress_1_ -> {
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
            if(p_mouseDragged_3_ >= guiTop + 18 && p_mouseDragged_3_ <= guiTop + 124 - 36){
                currentScroll = (int) p_mouseDragged_3_;
                currentScroll = MathHelper.clamp(currentScroll, this.guiTop + 18, this.guiTop + 124 - 24);
                buttons.get(0).y = currentScroll;
                container.updateSlots(map(currentScroll));
            }
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
        currentScroll = (int) (currentScroll - (p_mouseScrolled_5_ / (container.getInvSize()/8)));
        currentScroll = MathHelper.clamp(currentScroll, this.guiTop + 18, this.guiTop + 124 - 24);
        buttons.get(0).y = currentScroll;
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

    private int map(int pos){
        int map_max = (container.getInvSize()/9) - 5;
        double l = this.guiTop + 18;
        double h = this.guiTop + 124 - 36;
        double n = (pos - l);
        double d = (h - l);
        double val = n/d;
        val = val * map_max;
        return (int) val;
    }

}
