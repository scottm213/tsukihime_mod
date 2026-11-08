package waddle.plays.tsukihime_mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.entity.custom.BlackKeyProjectile;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.item.client.BlackKeysRenderer;

public class BlackKeyProjectileRenderer
        extends EntityRenderer<BlackKeyProjectile> {

    private final BlackKeysRenderer keysRenderer = new BlackKeysRenderer();



    public BlackKeyProjectileRenderer(
            EntityRendererProvider.Context context
    ) {
        super(context);
    }

    @Override
    public void render(
            BlackKeyProjectile projectile,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight
    ) {
        poseStack.pushPose();

        float yaw = Mth.rotLerp(
                partialTick,
                projectile.yRotO,
                projectile.getYRot()
        );

        float pitch = Mth.lerp(
                partialTick,
                projectile.xRotO,
                projectile.getXRot()
        );


        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));

        poseStack.scale(0.75F, 0.75F, 0.75F);

        poseStack.translate(-0.5, -0.51, -0.5);

        keysRenderer.renderByItem(
                projectile.getItem(),
                ItemDisplayContext.NONE,
                poseStack,
                bufferSource,
                packedLight,
                OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();

        super.render(
                projectile,
                entityYaw,
                partialTick,
                poseStack,
                bufferSource,
                packedLight
        );
    }

    @Override
    public ResourceLocation getTextureLocation(
            BlackKeyProjectile projectile
    ) {
        return new ResourceLocation(
                Tsukihime_mod.MOD_ID,
                projectile.isMaster()
                        ? "textures/item/master_black_keys.png"
                        : "textures/item/black_keys.png"
        );
    }
}