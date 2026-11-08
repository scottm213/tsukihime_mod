package waddle.plays.tsukihime_mod.item.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.model.GeoModel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.custom.NekoArcEars;

import java.util.function.Consumer;

public class NekoArcEarsModel extends GeoModel<NekoArcEars> {

    @Override
    public ResourceLocation getModelResource(NekoArcEars animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "geo/neko_arc_ears.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NekoArcEars animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "textures/item/neko_arc_ears.png");
    }




    @Override
    public ResourceLocation getAnimationResource(NekoArcEars animatable) {
        return null;
    }
}

