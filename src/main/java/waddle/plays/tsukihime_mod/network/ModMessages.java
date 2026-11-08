package waddle.plays.tsukihime_mod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import waddle.plays.tsukihime_mod.Tsukihime_mod;

import java.util.Optional;

public final class ModMessages {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL =
            NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(Tsukihime_mod.MOD_ID, "main"),
                    () -> PROTOCOL_VERSION,
                    PROTOCOL_VERSION::equals,
                    PROTOCOL_VERSION::equals
            );

    public static void register() {
        int id = 0;

        CHANNEL.registerMessage(
                id++,
                ActivateRobePacket.class,
                ActivateRobePacket::encode,
                ActivateRobePacket::decode,
                ActivateRobePacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}