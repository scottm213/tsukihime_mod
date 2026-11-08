package waddle.plays.tsukihime_mod.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.custom.SeventhHolyScriptureBase;

public class SeventhHolyScriptureBaseModel extends GeoModel<SeventhHolyScriptureBase> {
    @Override
    public ResourceLocation getModelResource(SeventhHolyScriptureBase item) {
        return resource("geo/seventh_holy_scripture_base.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeventhHolyScriptureBase item) {
        return resource("textures/item/seventh_holy_scripture_base.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeventhHolyScriptureBase item) {
        return resource("animations/seventh_holy_scripture_base.animation.json");
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, path);
    }
}
