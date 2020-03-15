package teabx.vanillaextended.client.renders;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teabx.vanillaextended.client.models.WanderingAssassinModel;
import teabx.vanillaextended.entities.WanderingAssassin;

import javax.annotation.Nullable;

public class WanderingAssassinRenderer extends LivingRenderer<WanderingAssassin, WanderingAssassinModel> {

    public WanderingAssassinRenderer(EntityRendererManager manager) {
        super(manager, new WanderingAssassinModel(), 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(WanderingAssassin entity) {
        return null;
    }

    public static class RenderFactory implements IRenderFactory<WanderingAssassin> {

        @Override
        public EntityRenderer<? super WanderingAssassin> createRenderFor(EntityRendererManager manager) {
            return new WanderingAssassinRenderer(manager);
        }
    }
}
