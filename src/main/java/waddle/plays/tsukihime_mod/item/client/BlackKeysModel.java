package waddle.plays.tsukihime_mod.item.client;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.item.custom.BlackKeys;

public class BlackKeysModel extends GeoModel<BlackKeys> {
    private int visibleKeys = 1;

    public void setStackCount(int count) {
        visibleKeys = ((Math.max(1, count) - 1) % 3) + 1;
    }

    @Override
    public ResourceLocation getModelResource(BlackKeys item) {
        return switch (visibleKeys) {
            case 2 -> resource("geo/black_keys_two.geo.json");
            case 3 -> resource("geo/black_keys_three.geo.json");
            default -> resource("geo/black_keys.geo.json");
        };
    }

    @Override
    public ResourceLocation getTextureResource(BlackKeys item) {
        if (item == ModItems.MASTER_BLACK_KEYS.get()) {
            return resource("textures/item/master_black_keys.png");
        }

        return resource("textures/item/black_keys.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlackKeys item) {
        return resource("animations/black_keys.animation.json");
    }

    private static ResourceLocation resource(String path) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, path);
    }
}