package waddle.plays.tsukihime_mod.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;

public class NrvnqsrWolfRenderer extends WolfRenderer {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(
                    "tsukihime_mod",
                    "textures/entity/nrvnqsr_wolf.png"
            );

    public NrvnqsrWolfRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Wolf wolf) {
        return TEXTURE;
    }
}