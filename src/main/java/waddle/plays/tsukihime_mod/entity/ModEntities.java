package waddle.plays.tsukihime_mod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.entity.custom.BlackKeyProjectile;
import waddle.plays.tsukihime_mod.entity.custom.NrvnqsrWolf;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    ForgeRegistries.ENTITY_TYPES,
                    Tsukihime_mod.MOD_ID
            );

    public static final RegistryObject<EntityType<BlackKeyProjectile>>
            BLACK_KEY_PROJECTILE = ENTITY_TYPES.register(
            "black_key_projectile",
            () -> EntityType.Builder
                    .<BlackKeyProjectile>of(
                            BlackKeyProjectile::new,
                            MobCategory.MISC
                    )
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(8)
                    .updateInterval(1)
                    .build(Tsukihime_mod.MOD_ID + ":black_key_projectile")
    );

    public static final RegistryObject<EntityType<NrvnqsrWolf>> NRVNQSR_WOLF =
            ENTITY_TYPES.register(
                    "nrvnqsr_wolf",
                    () -> EntityType.Builder
                            .<NrvnqsrWolf>of(
                                    NrvnqsrWolf::new,
                                    MobCategory.CREATURE
                            )
                            .sized(0.6F, 0.85F)
                            .clientTrackingRange(10)
                            .build(Tsukihime_mod.MOD_ID + ":nrvnqsr_wolf")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}