package waddle.plays.tsukihime_mod.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.custom.ShikiKnife;

public class ShikiKnifeModel extends GeoModel<ShikiKnife> {
    @Override
    public ResourceLocation getModelResource(ShikiKnife animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "geo/shiki_knife.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ShikiKnife animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "textures/item/shiki_knife.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ShikiKnife animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "animations/shiki_knife.animation.json");
    }
}
