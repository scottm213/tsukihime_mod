package waddle.plays.tsukihime_mod.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import waddle.plays.tsukihime_mod.Tsukihime_mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(
        modid = Tsukihime_mod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE
)
public class NrvnqsrSummons extends SavedData {
    public static final int MAX_WOLVES = 5;
    public static final int LIFETIME_TICKS = 20 * 120;

    private static final String DATA_NAME = "tsukihime_nrvnqsr_summons";


    private static final UUID HEALTH_MODIFIER_ID =
            UUID.fromString("619b0b15-9dde-475c-945a-2d6d4f8943b8");

    private record Summon(UUID owner, long expiresAt) {}

    private final Map<UUID, Summon> summons = new HashMap<>();

    public static NrvnqsrSummons get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
                NrvnqsrSummons::load,
                NrvnqsrSummons::new,
                DATA_NAME
        );
    }

    public static long now(MinecraftServer server) {
        return server.overworld().getGameTime();
    }

    public int count(UUID owner, long time) {
        return (int) summons.values().stream()
                .filter(summon -> summon.owner().equals(owner))
                .filter(summon -> summon.expiresAt() > time)
                .count();
    }

    public boolean isActive(UUID wolf, long time) {
        Summon summon = summons.get(wolf);
        return summon != null && summon.expiresAt() > time;
    }

    public void register(UUID wolf, UUID owner, long time) {
        summons.put(
                wolf,
                new Summon(owner, time + LIFETIME_TICKS)
        );
        setDirty();
    }

    public void unregister(UUID wolf) {
        if (summons.remove(wolf) != null) {
            setDirty();
        }
    }

    private void removeExpired(long time) {
        boolean changed = summons.values().removeIf(
                summon -> summon.expiresAt() <= time
        );

        if (changed) {
            setDirty();
        }
    }

    public void updateHealth(ServerPlayer player, long time) {
        var attribute = player.getAttribute(Attributes.MAX_HEALTH);
        if (attribute == null) return;

        int activeWolves = count(player.getUUID(), time);
        double penalty = -activeWolves;

        var existing = attribute.getModifier(HEALTH_MODIFIER_ID);


        if (existing != null && existing.getAmount() == penalty) {
            return;
        }

        if (existing != null) {
            attribute.removeModifier(HEALTH_MODIFIER_ID);
        }

        if (activeWolves > 0) {
            attribute.addTransientModifier(
                    new AttributeModifier(
                            HEALTH_MODIFIER_ID,
                            "Nrvnqsr summoned wolves",
                            penalty,
                            AttributeModifier.Operation.ADDITION
                    )
            );
        }


        if (player.getHealth() > player.getMaxHealth()) {
            player.setHealth(player.getMaxHealth());
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        MinecraftServer server = event.getServer();
        NrvnqsrSummons tracker = get(server);
        long time = now(server);

        tracker.removeExpired(time);

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            tracker.updateHealth(player, time);
        }
    }

    public static NrvnqsrSummons load(CompoundTag tag) {
        NrvnqsrSummons tracker = new NrvnqsrSummons();
        ListTag entries = tag.getList("Summons", Tag.TAG_COMPOUND);

        for (int i = 0; i < entries.size(); i++) {
            CompoundTag entry = entries.getCompound(i);

            tracker.summons.put(
                    entry.getUUID("Wolf"),
                    new Summon(
                            entry.getUUID("Owner"),
                            entry.getLong("ExpiresAt")
                    )
            );
        }

        return tracker;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag entries = new ListTag();

        summons.forEach((wolf, summon) -> {
            CompoundTag entry = new CompoundTag();
            entry.putUUID("Wolf", wolf);
            entry.putUUID("Owner", summon.owner());
            entry.putLong("ExpiresAt", summon.expiresAt());
            entries.add(entry);
        });

        tag.put("Summons", entries);
        return tag;
    }
}