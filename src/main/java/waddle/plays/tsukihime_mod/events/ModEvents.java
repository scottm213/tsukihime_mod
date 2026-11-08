package waddle.plays.tsukihime_mod.events;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.effect.ModEffects;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.network.ActivateRobePacket;
import waddle.plays.tsukihime_mod.network.ModMessages;
import waddle.plays.tsukihime_mod.sound.ModSounds;

@Mod.EventBusSubscriber(modid = Tsukihime_mod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START
                || !(event.getEntity() instanceof ServerPlayer player)
                || !player.getMainHandItem().is(ModItems.SHIKI_KNIFE.get())
                || event.getUseItem() == Event.Result.DENY
                || event.getResult() == Event.Result.DENY) {
            return;
        }

        BlockPos pos = event.getPos();
        if (player.isSpectator()
                || !player.canReach(pos, 1.5)
                || player.serverLevel().isOutsideBuildHeight(pos)
                || !player.serverLevel().mayInteract(player, pos)) {
            return;
        }


        var knife = ModItems.SHIKI_KNIFE.get();


        event.setCanceled(true);



        if (player.getCooldowns().isOnCooldown(knife)) {
            return;
        }

        var targetPos = event.getPos().immutable();
        var targetDimension = player.level().dimension();
        var originalState = player.serverLevel().getBlockState(targetPos);

        ShikiPendingActions.schedule(player, finishedPlayer -> {
            var level = finishedPlayer.serverLevel();

            if (!level.dimension().equals(targetDimension)
                    || !level.hasChunkAt(targetPos)
                    || finishedPlayer.isSpectator()
                    || !finishedPlayer.canReach(targetPos, 1.5)
                    || !level.mayInteract(finishedPlayer, targetPos)
                    || !level.getBlockState(targetPos).equals(originalState)) {
                return;
            }

            boolean destroyed = level.destroyBlock(targetPos, false, finishedPlayer);
            finishedPlayer.serverLevel().playSound(
                    null, // Nobody is excluded, including you.
                    finishedPlayer.getX(),
                    finishedPlayer.getY(),
                    finishedPlayer.getZ(),
                    ModSounds.SHIKI_SLASH.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            if (destroyed) {
                if (player.getItemBySlot(EquipmentSlot.HEAD)
                        .is(ModItems.SHIKI_GLASSES.get())){
                    player.getMainHandItem().hurtAndBreak(1, player, entity ->
                            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                } else{
                    player.getMainHandItem().hurtAndBreak(6, player, entity ->
                            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                }

                player.getCooldowns().addCooldown(knife, 47);
                if (player.hasEffect(ModEffects.ANEMIA.get())){
                    MobEffectInstance anemia = player.getEffect(ModEffects.ANEMIA.get());
                    if (anemia != null && anemia.getAmplifier() == 1) {
                        player.addEffect(
                                new MobEffectInstance(ModEffects.ANEMIA.get(), 600, 2)
                        );
                    } else if (anemia != null && anemia.getAmplifier() == 0){
                        player.addEffect(
                                new MobEffectInstance(ModEffects.ANEMIA.get(), 400, 1)
                        );
                    } else if (anemia != null && anemia.getAmplifier() == 2) {
                        player.addEffect(
                                new MobEffectInstance(ModEffects.ANEMIA.get(), 600, 2)
                        );
                    }
                }else{
                    player.addEffect(
                            new MobEffectInstance(ModEffects.ANEMIA.get(), 300, 0)
                    );
                }
            }
        });

        player.connection.send(
                new ClientboundBlockUpdatePacket(player.serverLevel(), pos)
        );
    }


    @SubscribeEvent
    public static void preventAnemia(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getEffectInstance().getEffect() == ModEffects.ANEMIA.get()
                && player.getItemBySlot(EquipmentSlot.HEAD)
                .is(ModItems.SHIKI_GLASSES.get())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void clearAnemiaOnEquip(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (event.getSlot() == EquipmentSlot.HEAD
                && event.getTo().is(ModItems.SHIKI_GLASSES.get())) {
            player.removeEffect(ModEffects.ANEMIA.get());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEmptyHandRightClick(
            PlayerInteractEvent.RightClickEmpty event) {

        if (event.getHand() != InteractionHand.MAIN_HAND) {
            return;
        }

        Player player = event.getEntity();

        if (!player.getMainHandItem().isEmpty()) {
            return;
        }

        if (!player.getItemBySlot(EquipmentSlot.CHEST)
                .is(ModItems.NRVNQSR_ROBE.get())) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(ModItems.NRVNQSR_ROBE.get())) {
            return;
        }

        ModMessages.CHANNEL.sendToServer(new ActivateRobePacket());
    }


    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void attackEntityEvent(AttackEntityEvent event){
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (!player.getMainHandItem().is(ModItems.SHIKI_KNIFE.get())) {
            return;
        }

        var knife = ModItems.SHIKI_KNIFE.get();

        event.setCanceled(true);
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        if (player.getCooldowns().isOnCooldown(knife)) return;

        var targetId = event.getTarget().getUUID();
        var targetDimension = player.level().dimension();


        if (target instanceof LivingEntity livingTarget){
            livingTarget.addEffect(new MobEffectInstance(
                    MobEffects.MOVEMENT_SLOWDOWN,
                    28, //reminder: thats 28 ticks = 1.4 secs
                    9,   // and this is level 10 slowness
                    false,
                    false,
                    false
            ));
        }

        target.setDeltaMovement(Vec3.ZERO);
 //       target.setNoGravity(true);


//        serverPlayer.addEffect(new MobEffectInstance(
//                MobEffects.MOVEMENT_SLOWDOWN,
//                28,
//                9,
//                false,
//                false,
//                false
//        ));


        ShikiPendingActions.schedule((ServerPlayer) player, finishedPlayer -> {
            if (!finishedPlayer.level().dimension().equals(targetDimension)) return;

            if (target == null
                    || !target.isAlive()
                    || !finishedPlayer.canReach(target, 0)
                    || !finishedPlayer.hasLineOfSight(target)) {
                return;
            }
            if (target instanceof Slime slime) {
                if (slime.getSize() > 1) {

                    slime.setSize(1, false);
                }
            }

            target.kill();
            finishedPlayer.serverLevel().playSound(
                    null,
                    finishedPlayer.getX(),
                    finishedPlayer.getY(),
                    finishedPlayer.getZ(),
                    ModSounds.SHIKI_SLASH.get(),
                    SoundSource.PLAYERS,
                    1.0F,
                    1.0F
            );

            if (player.getItemBySlot(EquipmentSlot.HEAD)
                    .is(ModItems.SHIKI_GLASSES.get())){
                player.getMainHandItem().hurtAndBreak(1, player, entity ->
                        entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            } else{
                player.getMainHandItem().hurtAndBreak(6, player, entity ->
                        entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }

            if (player.hasEffect(ModEffects.ANEMIA.get())){
                MobEffectInstance anemia = player.getEffect(ModEffects.ANEMIA.get());
                if (anemia != null && anemia.getAmplifier() == 1) {
                    player.addEffect(
                            new MobEffectInstance(ModEffects.ANEMIA.get(), 600, 2)
                    );
                } else if (anemia != null && anemia.getAmplifier() == 0){
                    player.addEffect(
                            new MobEffectInstance(ModEffects.ANEMIA.get(), 400, 1)
                    );
                } else if (anemia != null && anemia.getAmplifier() == 2) {
                    player.addEffect(
                            new MobEffectInstance(ModEffects.ANEMIA.get(), 600, 2)
                    );
                }
            }else{
                player.addEffect(
                        new MobEffectInstance(ModEffects.ANEMIA.get(), 300, 0)
                );
            }

        });





        player.getCooldowns().addCooldown(knife, 47);


    }


}
