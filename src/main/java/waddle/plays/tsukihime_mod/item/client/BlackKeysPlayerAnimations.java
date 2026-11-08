package waddle.plays.tsukihime_mod.item.client;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.ModItems;

@Mod.EventBusSubscriber(modid = Tsukihime_mod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class BlackKeysPlayerAnimations {
    private static final ResourceLocation THIRD_PERSON = id("black_keys_idle_third_person");
    private static final ResourceLocation FIRST_PERSON = id("black_keys_idle_first_person");
    private static final ResourceLocation LAYER = id("black_keys_idle");

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                LAYER, 100, HeldIdle::new));
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(Tsukihime_mod.MOD_ID, path);
    }

    private static final class HeldIdle implements IAnimation {
        private final AbstractClientPlayer player;
        private final FirstPersonConfiguration hands = new FirstPersonConfiguration()
                .setShowRightArm(true).setShowLeftArm(true);
        private KeyframeAnimation thirdData;
        private KeyframeAnimation firstData;
        private KeyframeAnimationPlayer third;
        private KeyframeAnimationPlayer first;
        private boolean warnedMissing;

        private HeldIdle(AbstractClientPlayer player) {
            this.player = player;
        }

        private boolean shouldPlay() {
            return player.isAlive() && !player.isSpectator()
                    && (player.getMainHandItem().is(ModItems.BLACK_KEYS.get()) || player.getMainHandItem().is(ModItems.MASTER_BLACK_KEYS.get()))
                    && !player.swinging && !player.isUsingItem()
                    && !player.isSwimming() && !player.isFallFlying() && !player.isSleeping();
        }

        private void refreshAnimations() {
            if (!shouldPlay()) {
                third = first = null;
                thirdData = firstData = null;
                return;
            }

            KeyframeAnimation nextThird = PlayerAnimationRegistry.getAnimation(THIRD_PERSON);
            KeyframeAnimation nextFirst = PlayerAnimationRegistry.getAnimation(FIRST_PERSON);
            if (nextThird == null) {
                if (!warnedMissing) Tsukihime_mod.LOGGER.warn("Missing player animation: {}", THIRD_PERSON);
                warnedMissing = true;
            } else {
                warnedMissing = false;
            }
            if (nextThird != thirdData) {
                thirdData = nextThird;
                third = nextThird == null ? null : new KeyframeAnimationPlayer(nextThird);
            }
            if (nextFirst != firstData) {
                firstData = nextFirst;
                first = nextFirst == null ? null : new KeyframeAnimationPlayer(nextFirst);
            }
        }

        private KeyframeAnimationPlayer current() {
            return FirstPersonMode.isFirstPersonPass() ? first : third;
        }

        @Override
        public boolean isActive() {
            // PlayerAnimator only ticks active layers, so initialize here as well.
            refreshAnimations();
            return shouldPlay() && ((third != null && third.isActive())
                    || (first != null && first.isActive()));
        }

        @Override
        public void tick() {
            refreshAnimations();
            if (third != null) third.tick();
            if (first != null) first.tick();
        }

        @Override
        public void setupAnim(float tickDelta) {
            refreshAnimations();
            if (third != null) third.setupAnim(tickDelta);
            if (first != null) first.setupAnim(tickDelta);
        }

        @Override
        public Vec3f get3DTransform(String bone, TransformType type, float tickDelta, Vec3f original) {
            KeyframeAnimationPlayer animation = current();
            return !shouldPlay() || animation == null || !animation.isActive() ? original
                    : animation.get3DTransform(bone, type, tickDelta, original);
        }

        @Override
        public FirstPersonMode getFirstPersonMode(float tickDelta) {
            return shouldPlay() && first != null && first.isActive()
                    ? FirstPersonMode.THIRD_PERSON_MODEL : FirstPersonMode.NONE;
        }

        @Override
        public FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
            return hands;
        }
    }
}
