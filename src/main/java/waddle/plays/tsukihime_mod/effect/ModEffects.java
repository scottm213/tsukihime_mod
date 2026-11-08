package waddle.plays.tsukihime_mod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import waddle.plays.tsukihime_mod.Tsukihime_mod;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(
                    ForgeRegistries.MOB_EFFECTS,
                    Tsukihime_mod.MOD_ID
            );


    public static final RegistryObject<MobEffect> ANEMIA =
            EFFECTS.register("anemia", AnemiaEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}