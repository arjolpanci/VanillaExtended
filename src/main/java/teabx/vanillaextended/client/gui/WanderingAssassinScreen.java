package teabx.vanillaextended.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import teabx.vanillaextended.container.WanderingAssassinContainer;
import teabx.vanillaextended.main.VanillaExtended;

public class WanderingAssassinScreen extends ContainerScreen<WanderingAssassinContainer> {

    private static final ResourceLocation WANDERING_ASSASSIN_GUI = new ResourceLocation(VanillaExtended.MODID, "textures/gui/wandering_asssing_gui.png");

    public WanderingAssassinScreen(WanderingAssassinContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.xSize = 276;
        this.ySize = 166;
        this.setSize(512, 512);
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
