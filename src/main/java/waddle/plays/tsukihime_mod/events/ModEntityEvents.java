package waddle.plays.tsukihime_mod.events;

import net.minecraft.world.entity.animal.Wolf;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.entity.ModEntities;

@Mod.EventBusSubscriber(
        modid = Tsukihime_mod.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ModEntityEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.NRVNQSR_WOLF.get(),
                Wolf.createAttributes().build()
        );
    }
}