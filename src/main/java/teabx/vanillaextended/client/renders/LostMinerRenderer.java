package teabx.vanillaextended.client.renders;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teabx.vanillaextended.client.models.LostMinerModel;
import teabx.vanillaextended.entities.LostMiner;
import teabx.vanillaextended.main.VanillaExtended;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class LostMinerRenderer extends LivingRenderer<LostMiner, LostMinerModel> {

    public LostMinerRenderer(EntityRendererManager manager) {
        super(manager, new LostMinerModel(), 0.5F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LostMiner entity) {
        return VanillaExtended.rloc("textures/entity/lost_miner.png");
    }

    public static class RenderFactory implements IRenderFactory<LostMiner> {

        @Override
        public EntityRenderer<? super LostMiner> createRenderFor(EntityRendererManager manager) {
            return new LostMinerRenderer(manager);
        }
    }
}
