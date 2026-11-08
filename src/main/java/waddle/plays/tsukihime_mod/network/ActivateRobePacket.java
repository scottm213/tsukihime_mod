package waddle.plays.tsukihime_mod.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.network.NetworkEvent;
import waddle.plays.tsukihime_mod.entity.ModEntities;
import waddle.plays.tsukihime_mod.entity.NrvnqsrSummons;
import waddle.plays.tsukihime_mod.entity.custom.NrvnqsrWolf;
import waddle.plays.tsukihime_mod.item.ModItems;
import waddle.plays.tsukihime_mod.item.client.NrvnqsrRobeModel;

import java.util.function.Supplier;
import java.util.logging.Level;

public final class ActivateRobePacket {

    public static void encode(
            ActivateRobePacket packet,
            FriendlyByteBuf buffer) {
    }

    public static ActivateRobePacket decode(FriendlyByteBuf buffer) {
        return new ActivateRobePacket();
    }

    public static void handle(
            ActivateRobePacket packet,
            Supplier<NetworkEvent.Context> contextSupplier) {

        NetworkEvent.Context context = contextSupplier.get();

        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();

            if (player == null || !player.isAlive() || player.isSpectator()) {
                return;
            }


            if (!player.getItemBySlot(EquipmentSlot.CHEST)
                    .is(ModItems.NRVNQSR_ROBE.get())) {
                return;
            }

            if (!player.getMainHandItem().isEmpty()) {
                return;
            }

            var robe = ModItems.NRVNQSR_ROBE.get();

            if (player.getCooldowns().isOnCooldown(robe)) {
                return;
            }



            activateAbility(player);
        });

        context.setPacketHandled(true);
    }

    private static void activateAbility(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        var server = level.getServer();
        var tracker = NrvnqsrSummons.get(server);
        long time = NrvnqsrSummons.now(server);

        if (tracker.count(player.getUUID(), time)
                >= NrvnqsrSummons.MAX_WOLVES) {
            return;
        }

        BlockPos spawnPos =
                player.blockPosition().relative(player.getDirection(), 2);

        NrvnqsrWolf wolf = ModEntities.NRVNQSR_WOLF.get().spawn(
                level,
                spawnPos,
                MobSpawnType.MOB_SUMMONED
        );

        if (wolf == null || wolf.isRemoved()) return;

        wolf.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                NrvnqsrSummons.LIFETIME_TICKS,
                1,
                false,
                false
        ));

        wolf.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                NrvnqsrSummons.LIFETIME_TICKS,
                0,
                false,
                false
        ));
        wolf.addEffect(new MobEffectInstance(
                MobEffects.DAMAGE_BOOST,
                NrvnqsrSummons.LIFETIME_TICKS,
                0,
                false,
                false
        ));

        wolf.setCollarColor(DyeColor.BLACK);


        wolf.initializeSummon(player);


        wolf.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0D);
        wolf.setHealth(wolf.getMaxHealth());


        player.getMainHandItem().hurtAndBreak(1, player, entity ->
                entity.broadcastBreakEvent(EquipmentSlot.CHEST));


    }
}