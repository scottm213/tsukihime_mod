
package waddle.plays.tsukihime_mod.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.custom.ShikiKnife;
import waddle.plays.tsukihime_mod.item.custom.VlovLance;

public class VlovLanceModel extends GeoModel<VlovLance> {

    @Override
    public ResourceLocation getModelResource(VlovLance animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "geo/vlov_lance.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VlovLance animatable) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, "textures/item/vlov_lance.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VlovLance animatable) {
        return null;
    }
}
