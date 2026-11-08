package waddle.plays.tsukihime_mod.item.client;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.sound.ModSounds;

@Mod.EventBusSubscriber(modid = Tsukihime_mod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ShikiKnifePlayerAnimation {
    private static final ResourceLocation CLIP = new ResourceLocation(Tsukihime_mod.MOD_ID, "shiki_knife_slash");
    private static final ResourceLocation LAYER = new ResourceLocation(Tsukihime_mod.MOD_ID, "shiki_knife_slash_layer");

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(LAYER, 110, Slash::new));
    }

    private static Slash layer(AbstractClientPlayer player) {
        var animation = PlayerAnimationAccess.getPlayerAssociatedData(player).get(LAYER);
        return animation instanceof Slash slash ? slash : null;
    }

    @Mod.EventBusSubscriber(modid = Tsukihime_mod.MOD_ID, value = Dist.CLIENT)
    public static final class ClientEvents {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void attack(InputEvent.InteractionKeyMappingTriggered event) {
            var minecraft = Minecraft.getInstance();
            var player = minecraft.player;
            if (!event.isAttack() || !event.shouldSwingHand() || player == null) return;
            if (!player.getMainHandItem().is(ModItems.SHIKI_KNIFE.get())) return;
            if (minecraft.hitResult == null || minecraft.hitResult.getType() == HitResult.Type.MISS) {

                event.setSwingHand(false);
                event.setCanceled(true);
                return;
            }
            Slash slash = layer(player);
            if (slash != null) slash.start();

        }

        @SubscribeEvent
        public static void tick(TickEvent.ClientTickEvent event) {
            var minecraft = Minecraft.getInstance();
            if (event.phase != TickEvent.Phase.END || minecraft.isPaused() || minecraft.player == null) return;
            Slash local = layer(minecraft.player);
            if (local != null) local.update(false);
            if (minecraft.level == null) return;
            for (AbstractClientPlayer player : minecraft.level.players()) {
                if (player == minecraft.player) continue;
                Slash remote = layer(player);
                if (remote != null) remote.update(true);
            }
        }
    }

    private static final class Slash implements IAnimation {
        private final AbstractClientPlayer player;
        private final FirstPersonConfiguration hands = new FirstPersonConfiguration()
                .setShowRightArm(true).setShowLeftArm(false);
        private KeyframeAnimationPlayer animation;
        private boolean wasSwinging;
        private int previousSwingTime;
        private boolean warnedMissing;

        private Slash(AbstractClientPlayer player) {
            this.player = player;
        }

        private boolean canPlay() {
            return player.isAlive() && !player.isSpectator()
                    && player.getMainHandItem().is(ModItems.SHIKI_KNIFE.get())
                    && !player.isUsingItem() && !player.isSleeping()
                    && !player.isSwimming() && !player.isFallFlying();
        }

        private void start() {
            if (!canPlay() || isActive()) return;
            var data = PlayerAnimationRegistry.getAnimation(CLIP);
            if (data == null) {
                if (!warnedMissing) Tsukihime_mod.LOGGER.warn("Missing Shiki slash animation: {}", CLIP);
                warnedMissing = true;
                return;
            }
            warnedMissing = false;
            animation = new KeyframeAnimationPlayer(data);
        }

        private void update(boolean observeSwing) {
            if (!canPlay()) animation = null;
            boolean swinging = player.swinging && player.swingingArm == InteractionHand.MAIN_HAND;
            if (observeSwing && swinging && (!wasSwinging || player.swingTime < previousSwingTime)) start();
            wasSwinging = swinging;
            previousSwingTime = player.swingTime;
        }

        @Override
        public void tick() {
            if (!canPlay()) {
                animation = null;
            } else if (animation != null) {
                animation.tick();
                if (animation.getCurrentTick() == 5
                        && player == Minecraft.getInstance().player) {
                    player.level().playLocalSound(
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            ModSounds.SHIKI_SWING.get(),
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F,
                            false
                    );
                }
                if (!animation.isActive()) {
                    animation = null;

                }
            }
        }

        @Override
        public boolean isActive() {
            return canPlay() && animation != null && animation.isActive();
        }

        @Override
        public void setupAnim(float tickDelta) {
            if (animation != null) animation.setupAnim(tickDelta);
        }

        @Override
        public Vec3f get3DTransform(String bone, TransformType type, float tickDelta, Vec3f original) {
            if (!isActive() || !bone.equals("rightArm")) return original;
            Vec3f pose = animation.get3DTransform(bone, type, tickDelta, original);
            if (type != TransformType.ROTATION) return pose;

            float time = animation.getCurrentTick() + tickDelta;
            float remaining = animation.getData().endTick - time;
            float weight = Mth.clamp(Math.min(time, remaining) / 4.0F, 0.0F, 1.0F);
            weight = weight * weight * (3.0F - 2.0F * weight);
            float pitch = Mth.clamp(player.getViewXRot(tickDelta), -75.0F, 75.0F);
            return new Vec3f(pose.getX() + pitch * Mth.DEG_TO_RAD * weight,
                    pose.getY(), pose.getZ());
        }

        @Override
        public FirstPersonMode getFirstPersonMode(float tickDelta) {
            return isActive() ? FirstPersonMode.THIRD_PERSON_MODEL : FirstPersonMode.NONE;
        }

        @Override
        public FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
            return hands;
        }
    }
}
