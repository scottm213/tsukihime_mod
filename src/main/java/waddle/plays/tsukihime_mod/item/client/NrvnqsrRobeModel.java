package waddle.plays.tsukihime_mod.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.custom.NrvnqsrRobe;


public class NrvnqsrRobeModel extends GeoModel<NrvnqsrRobe> {

    @Override
    public ResourceLocation getModelResource(NrvnqsrRobe animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "geo/nrvnqsr_robe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NrvnqsrRobe animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "textures/item/nrvnqsr_robe.png");
    }




    @Override
    public ResourceLocation getAnimationResource(NrvnqsrRobe animatable) {
        return null;
    }
}

