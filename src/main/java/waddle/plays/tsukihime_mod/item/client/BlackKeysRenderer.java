package waddle.plays.tsukihime_mod.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.item.custom.BlackKeys;

public class BlackKeysRenderer extends GeoItemRenderer<BlackKeys> {
    private final BlackKeysModel keysModel;

    public BlackKeysRenderer() {
        this(new BlackKeysModel());
    }

    private BlackKeysRenderer(BlackKeysModel model) {
        super(model);
        this.keysModel = model;
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext displayContext,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        if (displayContext == ItemDisplayContext.NONE) {
            keysModel.setStackCount(1);
        } else if (stack.is(ModItems.MASTER_BLACK_KEYS.get())) {
            keysModel.setStackCount(BlackKeys.getMasterVisibleKeys(stack));
        } else {
            keysModel.setStackCount(stack.getCount());
        }

        super.renderByItem(
                    stack,
                    displayContext,
                    poseStack,
                    bufferSource,
                    packedLight,
                    packedOverlay
        );
    }
}
