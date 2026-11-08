package waddle.plays.tsukihime_mod.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import waddle.plays.tsukihime_mod.Tsukihime_mod;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> DIVINE_ORE_PLACED_KEY = registerKey("divine_ore_placed");
    public static final ResourceKey<PlacedFeature> DIVINE_ORE_LARGE_PLACED_KEY = registerKey("divine_ore_large_placed");
    public static final ResourceKey<PlacedFeature> DIVINE_ORE_BURIED_PLACED_KEY = registerKey("divine_ore_buried_placed");
    public static final ResourceKey<PlacedFeature> IKAITE_ORE_PLACED_KEY =
            registerKey("ikaite_ore_placed");


    public static void bootstrap(BootstapContext<PlacedFeature> context){
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);



        // Vanilla diamond uses 7 small, 4 buried, and a 1-in-9 large-vein attempt.
        // Use the same depth distribution with about 20% fewer attempts overall.
        HeightRangePlacement diamondDepth = HeightRangePlacement.triangle(
                VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80));
        register(context, DIVINE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_DIVINE_ORE_KEY),
                ModOrePlacement.commonOrePlacement(6, diamondDepth));
        register(context, DIVINE_ORE_LARGE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIVINE_ORE_LARGE_KEY),
                ModOrePlacement.rareOrePlacement(11, diamondDepth));
        register(context, DIVINE_ORE_BURIED_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DIVINE_ORE_BURIED_KEY),
                ModOrePlacement.commonOrePlacement(3, diamondDepth));
        register(context, IKAITE_ORE_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.IKAITE_ORE),
                ModOrePlacement.commonOrePlacement(4,
                        HeightRangePlacement.uniform(
                                VerticalAnchor.absolute(16),
                                VerticalAnchor.absolute(64))));


    }





    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Tsukihime_mod.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context,
                                 ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?,?>> configuration,
                                 List<PlacementModifier> modifiers){
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}

