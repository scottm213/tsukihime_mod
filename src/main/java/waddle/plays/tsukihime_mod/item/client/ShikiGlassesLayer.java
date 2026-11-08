package waddle.plays.tsukihime_mod.item.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.ModItems;

@Mod.EventBusSubscriber(
        modid = Tsukihime_mod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ShikiGlassesLayer
        extends RenderLayer<AbstractClientPlayer,
        PlayerModel<AbstractClientPlayer>> {

    public ShikiGlassesLayer(PlayerRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffers,
            int packedLight,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        var stack = player.getItemBySlot(EquipmentSlot.HEAD);

        if (!stack.is(ModItems.SHIKI_GLASSES.get())
                || player.isInvisible()) {
            return;
        }

        poseStack.pushPose();

        getParentModel().head.translateAndRotate(poseStack);

        CustomHeadLayer.translateToHead(poseStack, false);

        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.HEAD,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffers,
                player.level(),
                player.getId()
        );

        poseStack.popPose();
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            var renderer = event.getPlayerSkin(skin);

            if (renderer instanceof PlayerRenderer playerRenderer) {
                playerRenderer.addLayer(
                        new ShikiGlassesLayer(playerRenderer)
                );
            }
        }
    }
}