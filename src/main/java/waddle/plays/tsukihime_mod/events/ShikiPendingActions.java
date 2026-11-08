package waddle.plays.tsukihime_mod.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.item.ModItems;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Tsukihime_mod.MOD_ID)
public class ShikiPendingActions {
    private static final int SLASH_TICKS = 28;

    private record Pending(
            long finishTick,
            Consumer<ServerPlayer> action
    ) {}

    private static final Map<UUID, Pending> PENDING = new HashMap<>();

    public static void schedule(
            ServerPlayer player,
            Consumer<ServerPlayer> action
    ) {
        PENDING.putIfAbsent(
                player.getUUID(),
                new Pending(
                        player.serverLevel().getGameTime() + SLASH_TICKS,
                        action
                )
        );
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END
                || !(event.player instanceof ServerPlayer player)) {
            return;
        }

        Pending pending = PENDING.get(player.getUUID());
        if (pending == null) return;

        if (!player.isAlive()
                || !player.getMainHandItem().is(ModItems.SHIKI_KNIFE.get())) {
            PENDING.remove(player.getUUID());
            return;
        }

        if (player.serverLevel().getGameTime() < pending.finishTick()) {
            return;
        }

        PENDING.remove(player.getUUID());
        pending.action().accept(player);
    }
}