package waddle.plays.tsukihime_mod.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModToolTiers {

    public static final Tier DIVINE_IRON = new ForgeTier(
            3,
            3064,
            14.0F,    // Mining speed
            4.0F,    // Attack damage bonus
            15,      // Enchantability
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.DIVINE_IRON.get())
    );

    public static final Tier VLOV_LANCE = new ForgeTier(
            3,
            4028,
            3.0F,
            10.0F,
            15,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.IKAITE.get())
    );

    public static final Tier IKAITE = new ForgeTier(
            3,
            2048,
            9.0F,
            3.0F,
            15,
            BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModItems.IKAITE.get())
    );
}