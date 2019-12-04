package teabx.vanillaextended.client.renders;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import teabx.vanillaextended.client.models.SkeletonKingModel;
import teabx.vanillaextended.entities.SkeletonKing;
import teabx.vanillaextended.main.VanillaExtended;

import javax.annotation.Nullable;


@OnlyIn(Dist.CLIENT)
public class SkeletonKingRenderer extends LivingRenderer<SkeletonKing, SkeletonKingModel> {

    public SkeletonKingRenderer(EntityRendererManager manager) {
        super(manager, new SkeletonKingModel(), 0.5F);
        this.addLayer(new HeldItemLayer(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SkeletonKing entity) {
        return VanillaExtended.rloc("textures/entity/skeleton_king.png");
    }

    public static class RenderFactory implements IRenderFactory<SkeletonKing> {

        @Override
        public EntityRenderer<? super SkeletonKing> createRenderFor(EntityRendererManager manager) {
            return new SkeletonKingRenderer(manager);
        }
    }
}
