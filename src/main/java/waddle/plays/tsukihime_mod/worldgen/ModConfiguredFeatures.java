package waddle.plays.tsukihime_mod.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.fml.common.Mod;
import waddle.plays.tsukihime_mod.Tsukihime_mod;
import waddle.plays.tsukihime_mod.block.ModBlocks;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?,?>> OVERWORLD_DIVINE_ORE_KEY = registerKey("divine_iron_ore");
    public static final ResourceKey<ConfiguredFeature<?,?>> DIVINE_ORE_LARGE_KEY = registerKey("divine_iron_ore_large");
    public static final ResourceKey<ConfiguredFeature<?,?>> DIVINE_ORE_BURIED_KEY = registerKey("divine_iron_ore_buried");

    public static final ResourceKey<ConfiguredFeature<?,?>> IKAITE_ORE = registerKey("ikaite_ore");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?,?>> context){
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceable = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        List<OreConfiguration.TargetBlockState> overworldDivineOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.DIVINE_IRON_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslateReplaceable, ModBlocks.DIVINE_IRON_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> ikaiteOres = List.of(
                OreConfiguration.target(stoneReplaceable, ModBlocks.IKAITE_ORE.get().defaultBlockState())
        );


        register(context, OVERWORLD_DIVINE_ORE_KEY, Feature.ORE, new OreConfiguration(overworldDivineOres, 4, 0.25F));
        register(context, DIVINE_ORE_LARGE_KEY, Feature.ORE, new OreConfiguration(overworldDivineOres, 12, 0.4F));
        register(context, DIVINE_ORE_BURIED_KEY, Feature.ORE, new OreConfiguration(overworldDivineOres, 8, 0.75F));

        register(context, IKAITE_ORE, Feature.ORE, new OreConfiguration(ikaiteOres, 9, 0.4f));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Tsukihime_mod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?,?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration){
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }


}
