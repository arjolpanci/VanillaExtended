package teabx.vanillaextended.client.renders;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import teabx.vanillaextended.entities.LostMiner;
import teabx.vanillaextended.entities.SkeletonKing;


@OnlyIn(Dist.CLIENT)
public class RenderRegistry {

    public static void registerEntityRenders(){
        RenderingRegistry.registerEntityRenderingHandler(LostMiner.class, new LostMinerRenderer.RenderFactory());
        RenderingRegistry.registerEntityRenderingHandler(SkeletonKing.class, new SkeletonKingRenderer.RenderFactory());
    }

}
