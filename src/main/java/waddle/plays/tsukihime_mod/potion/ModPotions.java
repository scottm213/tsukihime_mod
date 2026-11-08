package waddle.plays.tsukihime_mod.potion;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.effect.ModEffects;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, Tsukihime_mod.MOD_ID);


    public static final RegistryObject<Potion> ANEMIA = POTIONS.register("anemia", () -> new Potion(new MobEffectInstance(ModEffects.ANEMIA.get(), 400,0)));


    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
