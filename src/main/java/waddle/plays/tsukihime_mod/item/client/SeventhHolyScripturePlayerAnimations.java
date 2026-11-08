package waddle.plays.tsukihime_mod.item.client;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.ModItems;

@Mod.EventBusSubscriber(modid = Tsukihime_mod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class SeventhHolyScripturePlayerAnimations {
    private static final ResourceLocation IDLE = new ResourceLocation(Tsukihime_mod.MOD_ID, "seventh_base_idle");
    private static final ResourceLocation LAYER = new ResourceLocation(Tsukihime_mod.MOD_ID, "seventh_holy_scripture_idle");

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                LAYER, 100, HeldIdle::new));
    }

    private static final class HeldIdle implements IAnimation {
        private final AbstractClientPlayer player;
        private KeyframeAnimation data;
        private KeyframeAnimationPlayer animation;
        private boolean warnedMissing;
        private boolean loggedLoaded;

        private HeldIdle(AbstractClientPlayer player) {
            this.player = player;
        }

        private boolean shouldPlay() {
            return player.isAlive() && !player.isSpectator()
                    && player.getMainHandItem().is(ModItems.SEVENTH_HOLY_SCRIPTURE_BASE.get())
                    && !player.swinging && !player.isUsingItem()
                    && !player.isSwimming() && !player.isFallFlying() && !player.isSleeping();
        }

        private void refreshAnimation() {
            if (!shouldPlay()) {
                animation = null;
                data = null;
                return;
            }
            KeyframeAnimation next = PlayerAnimationRegistry.getAnimation(IDLE);
            if (next == null) {
                if (!warnedMissing) Tsukihime_mod.LOGGER.warn("Missing player animation: {}", IDLE);
                warnedMissing = true;
            } else {
                warnedMissing = false;
                if (!loggedLoaded && player == Minecraft.getInstance().player) {

                    loggedLoaded = true;
                }
            }
            if (next != data) {
                data = next;
                animation = next == null ? null : new KeyframeAnimationPlayer(next);
            }

        }

        @Override
        public boolean isActive() {

            refreshAnimation();
            return shouldPlay() && animation != null && animation.isActive();
        }

        @Override
        public void tick() {
            refreshAnimation();
            if (animation != null) animation.tick();
        }

        @Override
        public void setupAnim(float tickDelta) {
            refreshAnimation();
            if (animation != null) animation.setupAnim(tickDelta);
        }

        @Override
        public Vec3f get3DTransform(String bone, TransformType type, float tickDelta, Vec3f original) {
            return !isActive() || FirstPersonMode.isFirstPersonPass() ? original
                    : animation.get3DTransform(bone, type, tickDelta, original);
        }

        @Override
        public FirstPersonMode getFirstPersonMode(float tickDelta) {
            return FirstPersonMode.NONE;
        }
    }
}
