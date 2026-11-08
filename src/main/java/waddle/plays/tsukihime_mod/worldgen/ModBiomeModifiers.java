package waddle.plays.tsukihime_mod.worldgen;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import waddle.plays.tsukihime_mod.Tsukihime_mod;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_DIVINE_ORE= registerKey("add_divine_ore");
    public static final ResourceKey<BiomeModifier> ADD_IKAITE_ORE = registerKey("add_ikaite_ore");

    public static void bootstrap(BootstapContext<BiomeModifier> context){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        context.register(ADD_DIVINE_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(
                        placedFeatures.getOrThrow(ModPlacedFeatures.DIVINE_ORE_PLACED_KEY),
                        placedFeatures.getOrThrow(ModPlacedFeatures.DIVINE_ORE_LARGE_PLACED_KEY),
                        placedFeatures.getOrThrow(ModPlacedFeatures.DIVINE_ORE_BURIED_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_IKAITE_ORE, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(
                        biomes.getOrThrow(Biomes.TAIGA),
                        biomes.getOrThrow(Biomes.COLD_OCEAN),
                        biomes.getOrThrow(Biomes.ICE_SPIKES),
                        biomes.getOrThrow(Biomes.SNOWY_BEACH),
                        biomes.getOrThrow(Biomes.SNOWY_PLAINS),
                        biomes.getOrThrow(Biomes.SNOWY_TAIGA),
                        biomes.getOrThrow(Biomes.SNOWY_SLOPES),
                        biomes.getOrThrow(Biomes.GROVE)),
                HolderSet.direct(
                        placedFeatures.getOrThrow(ModPlacedFeatures.IKAITE_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

    }

    //biomes.getOrThrow(Biomes.TAIGA);
    // MAKE ORE ONLY SPAWN IN CERTAIN BIOME


    private static ResourceKey<BiomeModifier> registerKey(String name){
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Tsukihime_mod.MOD_ID, name));
    }


}
