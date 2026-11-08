package waddle.plays.tsukihime_mod.sound;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import waddle.plays.tsukihime_mod.Tsukihime_mod;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(
                    ForgeRegistries.SOUND_EVENTS,
                    Tsukihime_mod.MOD_ID);


    public static final RegistryObject<SoundEvent> SHIKI_SWING =
            SOUNDS.register("shiki_slash_sound", () ->
                    SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(
                                    Tsukihime_mod.MOD_ID,
                                    "shiki_slash_sound")));


    public static final RegistryObject<SoundEvent> SHIKI_SLASH =
            SOUNDS.register("smash", () ->
                    SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(
                                    Tsukihime_mod.MOD_ID,
                                    "smash")));

    public static final RegistryObject<SoundEvent> VLOV_EXPLOSION =
            SOUNDS.register("vlov_explosion", () ->
                    SoundEvent.createVariableRangeEvent(
                            new ResourceLocation(
                                    Tsukihime_mod.MOD_ID,
                                    "vlov_explosion"
                            )
                    ));


    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}